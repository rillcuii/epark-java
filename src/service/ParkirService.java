package service;

import model.Parkir;

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
        private String nimMahasiswa;
        private String modelKendaraan;
        private LocalDateTime waktuMasuk;
        private LocalDateTime waktuKeluar;

        public ParkirSatpamDto(String namaUser, String nimMahasiswa, String modelKendaraan,
                               LocalDateTime waktuMasuk, LocalDateTime waktuKeluar) {
            this.namaUser = namaUser;
            this.nimMahasiswa = nimMahasiswa;
            this.modelKendaraan = modelKendaraan;
            this.waktuMasuk = waktuMasuk;
            this.waktuKeluar = waktuKeluar;
        }

        public String getNamaUser() { return namaUser; }
        public String getNimMahasiswa() { return nimMahasiswa; }
        public String getModelKendaraan() { return modelKendaraan; }
        public LocalDateTime getWaktuMasuk() { return waktuMasuk; }
        public LocalDateTime getWaktuKeluar() { return waktuKeluar; }
    }

    public List<ParkirSatpamDto> getMahasiswaKeluarHariIni() {
        List<ParkirSatpamDto> list = new ArrayList<>();

        String sql = "SELECT u.nama_user, u.nim_mahasiswa, k.model_kendaraan, p.waktu_masuk, p.waktu_keluar " +
                "FROM parkir p " +
                "JOIN users u ON p.users_id = u.id_user " +
                "JOIN kendaraan k ON k.id_kendaraan = p.kendaraan_id " +
                "WHERE p.waktu_keluar IS NOT NULL " +
                "AND DATE(p.waktu_keluar) = DATE('now') " +
                "ORDER BY p.waktu_keluar DESC";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String namaUser = rs.getString("nama_user");
                String nim = rs.getString("nim_mahasiswa");
                String model = rs.getString("model_kendaraan");
                LocalDateTime masuk = rs.getString("waktu_masuk") != null ?
                        LocalDateTime.parse(rs.getString("waktu_masuk")) : null;
                LocalDateTime keluar = rs.getString("waktu_keluar") != null ?
                        LocalDateTime.parse(rs.getString("waktu_keluar")) : null;

                list.add(new ParkirSatpamDto(namaUser, nim, model, masuk, keluar));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<ParkirSatpamDto> getSemuaRiwayatParkir() {
        List<ParkirSatpamDto> list = new ArrayList<>();

        String sql = "SELECT u.nama_user, u.nim_mahasiswa, k.model_kendaraan, p.waktu_masuk, p.waktu_keluar " +
                "FROM parkir p " +
                "JOIN users u ON p.users_id = u.id_user " +
                "JOIN kendaraan k ON k.id_kendaraan = p.kendaraan_id " +
                "WHERE p.waktu_keluar IS NOT NULL " +
                "ORDER BY p.waktu_keluar DESC";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String namaUser = rs.getString("nama_user");
                String nim = rs.getString("nim_mahasiswa");
                String model = rs.getString("model_kendaraan");
                LocalDateTime masuk = rs.getString("waktu_masuk") != null ?
                        LocalDateTime.parse(rs.getString("waktu_masuk")) : null;
                LocalDateTime keluar = rs.getString("waktu_keluar") != null ?
                        LocalDateTime.parse(rs.getString("waktu_keluar")) : null;

                list.add(new ParkirSatpamDto(namaUser, nim, model, masuk, keluar));
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

    public boolean parkirMasuk(String userId, String kendaraanId, String kodeUnik) {
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

        String sql = "INSERT INTO parkir (id_parkir, users_id, kendaraan_id, waktu_masuk, created_at, updated_at, kode_unik) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, idParkir);
            stmt.setString(2, userId);
            stmt.setString(3, kendaraanId);
            stmt.setString(4, now.toString());
            stmt.setString(5, now.toString());
            stmt.setString(6, now.toString());
            stmt.setString(7, kodeUnik);
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

    public boolean parkirKeluar(String userId, String kodeUnik) {
        if (!qrCodeService.isKodeUnikValid(kodeUnik)) {
            System.out.println("Kode unik QR Code tidak valid atau sudah expired");
            return false;
        }

        if (!hasActiveParkir(userId)) {
            System.out.println("User tidak ada parkir aktif");
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
}
