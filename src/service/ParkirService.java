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

    // Proses parkir masuk menggunakan kode unik, cek validitas kode dulu
    public boolean parkirMasuk(String userId, String kodeUnik) {
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

        String sql = "INSERT INTO parkir (id_parkir, users_id, waktu_masuk, created_at, updated_at, kode_unik) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, idParkir);
            stmt.setString(2, userId);
            stmt.setString(3, now.toString());
            stmt.setString(4, now.toString());
            stmt.setString(5, now.toString());
            stmt.setString(6, kodeUnik);
            int affected = stmt.executeUpdate();
            return affected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Proses parkir keluar menggunakan kode unik (cek validitas dulu)
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
