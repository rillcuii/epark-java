package service;

import model.Parkir;
import model.QrCodeSession;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ParkirService {

    private Connection conn;
    private QrCodeService qrCodeService;

    public ParkirService(Connection conn, QrCodeService qrCodeService) {
        this.conn = conn;
        this.qrCodeService = qrCodeService;
    }

    public static class ParkirSatpamDto {
        private String namaUser;
        private String stnkID;
        private String modelKendaraan;
        private LocalDateTime waktuMasuk;
        private LocalDateTime waktuKeluar;

        public ParkirSatpamDto(String namaUser, String stnkID, String modelKendaraan,
                               LocalDateTime waktuMasuk, LocalDateTime waktuKeluar) {
            this.namaUser = namaUser;
            this.stnkID = stnkID;
            this.modelKendaraan = modelKendaraan;
            this.waktuMasuk = waktuMasuk;
            this.waktuKeluar = waktuKeluar;
        }

        public String getNamaUser() { return namaUser; }
        public String getstnkID() { return stnkID; }
        public String getModelKendaraan() { return modelKendaraan; }
        public LocalDateTime getWaktuMasuk() { return waktuMasuk; }
        public LocalDateTime getWaktuKeluar() { return waktuKeluar; }
    }

    public List<ParkirSatpamDto> getMahasiswaKeluarHariIni() {
        List<ParkirSatpamDto> list = new ArrayList<>();

        String sql = "SELECT u.nama_user, k.stnk_id, k.model_kendaraan, p.waktu_masuk, p.waktu_keluar " +
                "FROM parkir p " +
                "JOIN users u ON p.users_id = u.id_user " +
                "JOIN kendaraan k ON k.id_kendaraan = p.kendaraan_id " +
                "WHERE p.waktu_masuk IS NOT NULL " +
                "AND p.waktu_keluar IS NOT NULL " +
                "AND DATE(p.waktu_masuk) = DATE('now', 'localtime') " +
                "AND DATE(p.waktu_keluar) = DATE('now', 'localtime') " +
                "ORDER BY p.waktu_masuk DESC";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String namaUser = rs.getString("nama_user");
                String stnkId = rs.getString("stnk_id");
                String model = rs.getString("model_kendaraan");
                LocalDateTime masuk = rs.getString("waktu_masuk") != null ?
                        LocalDateTime.parse(rs.getString("waktu_masuk")) : null;
                LocalDateTime keluar = rs.getString("waktu_keluar") != null ?
                        LocalDateTime.parse(rs.getString("waktu_keluar")) : null;

                list.add(new ParkirSatpamDto(namaUser, stnkId, model, masuk, keluar));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }



    public List<ParkirSatpamDto> getSemuaRiwayatParkir() {
        List<ParkirSatpamDto> list = new ArrayList<>();

        String sql = "SELECT u.nama_user, k.stnk_id, k.model_kendaraan, p.waktu_masuk, p.waktu_keluar " +
                "FROM parkir p " +
                "JOIN users u ON p.users_id = u.id_user " +
                "JOIN kendaraan k ON k.id_kendaraan = p.kendaraan_id " +
                "ORDER BY p.waktu_masuk DESC"; // Tidak pakai filter keluar

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String namaUser = rs.getString("nama_user");
                String stnkId = rs.getString("stnk_id");
                String model = rs.getString("model_kendaraan");
                LocalDateTime masuk = rs.getString("waktu_masuk") != null ?
                        LocalDateTime.parse(rs.getString("waktu_masuk")) : null;
                LocalDateTime keluar = rs.getString("waktu_keluar") != null ?
                        LocalDateTime.parse(rs.getString("waktu_keluar")) : null;

                list.add(new ParkirSatpamDto(namaUser, stnkId, model, masuk, keluar));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }



    public List<Parkir> getRiwayatParkirByUser(String userId) {
        List<Parkir> list = new ArrayList<>();
        String sql = "SELECT * FROM parkir WHERE users_id = ? ORDER BY waktu_masuk DESC";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Parkir p = new Parkir();
                p.setIdParkir(rs.getString("id_parkir"));
                p.setUserId(rs.getString("users_id"));
                 p.setKendaraanId(rs.getString("kendaraan_id")); // Hapus kalau kolom belum ada

                String masukStr = rs.getString("waktu_masuk");
                if (masukStr != null) p.setWaktuMasuk(LocalDateTime.parse(masukStr));

                String keluarStr = rs.getString("waktu_keluar");
                if (keluarStr != null) p.setWaktuKeluar(LocalDateTime.parse(keluarStr));

                String createdStr = rs.getString("created_at");
                if (createdStr != null) p.setCreatedAt(LocalDateTime.parse(createdStr));

                String updatedStr = rs.getString("updated_at");
                if (updatedStr != null) p.setUpdatedAt(LocalDateTime.parse(updatedStr));

                list.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }


    public boolean hasActiveParkir(String userId) {
        String sql = "SELECT COUNT(*) FROM parkir WHERE users_id = ? AND waktu_keluar IS NULL";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean parkirMasuk(String userId, String kendaraanId, String kodeUnik, String tokenUnik) {
        if (!qrCodeService.isKodeUnikValid(kodeUnik)) {
            System.out.println("Kode unik QR Code tidak valid atau sudah expired");
            return false;
        }

        if (hasActiveParkir(userId)) {
            System.out.println("User sudah ada parkir aktif");
            return false;
        }

        String idParkir = UUID.randomUUID().toString();
        LocalDateTime now = LocalDateTime.now();

        String sql = "INSERT INTO parkir (id_parkir, users_id, kendaraan_id, waktu_masuk, created_at, updated_at, kode_unik, token_unik) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, idParkir);
            stmt.setString(2, userId);
            stmt.setString(3, kendaraanId);
            stmt.setString(4, now.toString());
            stmt.setString(5, now.toString());
            stmt.setString(6, now.toString());
            stmt.setString(7, kodeUnik);
            stmt.setString(8, tokenUnik);
            int affected = stmt.executeUpdate();
            return affected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getActiveParkirKendaraanId(String userId) {
        String sql = "SELECT kendaraan_id FROM parkir WHERE users_id = ? AND waktu_keluar IS NULL LIMIT 1";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("kendaraan_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // tidak ada parkir aktif
    }

    public boolean parkirKeluar(String userId, String kodeUnik, String tokenUnik) {
        if (!qrCodeService.isKodeUnikValid(kodeUnik)) {
            System.out.println("Kode unik QR Code tidak valid atau sudah expired");
            return false;
        }

        if (!hasActiveParkir(userId)) {
            System.out.println("User tidak ada parkir aktif");
            return false;
        }

        if(!tokenUnikIsValid(userId, tokenUnik)){
            System.out.println("Token Unik Tidak Valid");
            return false;
        }

        LocalDateTime now = LocalDateTime.now();
        String sql = "UPDATE parkir SET waktu_keluar = ?, updated_at = ? WHERE users_id = ? AND waktu_keluar IS NULL";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, now.toString());
            stmt.setString(2, now.toString());
            stmt.setString(3, userId);
            int affected = stmt.executeUpdate();
            return affected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Ambil data mahasiswa yang sedang parkir hari ini
    public List<ParkirSatpamDto> getMahasiswaSedangParkirHariIni() {
        List<ParkirSatpamDto> list = new ArrayList<>();

        String sql = "SELECT u.nama_user, k.stnk_id, k.model_kendaraan, p.waktu_masuk, p.waktu_keluar " +
                "FROM parkir p " +
                "JOIN users u ON p.users_id = u.id_user " +
                "JOIN kendaraan k ON k.id_kendaraan = p.kendaraan_id " +
                "WHERE p.waktu_keluar IS NULL AND DATE(p.waktu_masuk) = DATE('now') " +
                "ORDER BY p.waktu_masuk DESC";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String namaUser = rs.getString("nama_user");
                String stnkId = rs.getString("stnk_id");
                String model = rs.getString("model_kendaraan");
                LocalDateTime masuk = rs.getString("waktu_masuk") != null ?
                        LocalDateTime.parse(rs.getString("waktu_masuk")) : null;

                // waktu_keluar pasti null, tapi kita tetap parsing sebagai null
                LocalDateTime keluar = null;

                list.add(new ParkirSatpamDto(namaUser, stnkId, model, masuk, keluar));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // Hitung sisa tempat parkir (dari total 500 slot)
    public int getSisaTempatParkir() {
        String sql = "SELECT COUNT(*) FROM parkir WHERE waktu_keluar IS NULL AND DATE(waktu_masuk) = DATE('now')";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int sedangParkir = rs.getInt(1);
                return 500 - sedangParkir;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 500; // default kalau gagal query
    }

    public boolean tokenUnikIsValid(String userId, String tokenUnik) {
        try{
            String tokenUnikDb = getTokenUnik(userId);
            if(tokenUnikDb.equals(tokenUnik)){
                return true;
            }

            return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;


    }

    public String getTokenUnik(String userId){
        String sql = "SELECT token_unik FROM parkir WHERE users_id = ? AND waktu_keluar IS NULL";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, userId);
            ResultSet rs = ps.executeQuery();

            String tokenUnikDb = rs.getString("token_unik");

            if (tokenUnikDb == null || tokenUnikDb.isEmpty()) {
                return null;
            }

            return tokenUnikDb;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

}
