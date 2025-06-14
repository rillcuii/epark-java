package service;

import model.Kendaraan;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class KendaraanService {

    private Connection conn;

    public KendaraanService(Connection conn) {
        this.conn = conn;
    }

    public List<Kendaraan> getKendaraanByUser(String userId) {
        List<Kendaraan> list = new ArrayList<>();
        String sql = "SELECT * FROM kendaraan WHERE users_id = ?";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Kendaraan kendaraan = new Kendaraan(
                        rs.getString("id_kendaraan"),
                        rs.getString("users_id"),
                        rs.getString("stnk_id"),
                        rs.getString("model_kendaraan"),
                        rs.getString("url_photo_kendaraan"),
                        LocalDateTime.parse(rs.getString("created_at"), formatter),
                        LocalDateTime.parse(rs.getString("updated_at"), formatter)
                );
                list.add(kendaraan);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void tambahKendaraan(String userId, String stnkId, String model, String urlPhoto) {
        String idKendaraan = UUID.randomUUID().toString();
        LocalDateTime now = LocalDateTime.now();

        Kendaraan kendaraan = new Kendaraan(
                idKendaraan,
                userId,
                stnkId,
                model,
                urlPhoto,
                now,
                now
        );
        insert(kendaraan);
    }

    public void insert(Kendaraan kendaraan) {
        String sql = "INSERT INTO kendaraan (id_kendaraan, users_id, stnk_id, model_kendaraan, url_photo_kendaraan, created_at, updated_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, kendaraan.getIdKendaraan());
            stmt.setString(2, kendaraan.getUserId());
            stmt.setString(3, kendaraan.getStnkId());
            stmt.setString(4, kendaraan.getModelKendaraan());
            stmt.setString(5, kendaraan.getUrlPhotoKendaraan());
            stmt.setString(6, kendaraan.getCreatedAt().format(formatter));
            stmt.setString(7, kendaraan.getUpdatedAt().format(formatter));

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateKendaraan(String idKendaraan, String userId, String stnkId, String model, String urlPhoto) {
        String sql = "UPDATE kendaraan SET users_id = ?, stnk_id = ?, model_kendaraan = ?, url_photo_kendaraan = ?, updated_at = CURRENT_TIMESTAMP WHERE id_kendaraan = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, userId);
            ps.setString(2, stnkId);
            ps.setString(3, model);
            ps.setString(4, urlPhoto);
            ps.setString(5, idKendaraan);

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void hapusKendaraan(String idKendaraan) {
        String sql = "DELETE FROM kendaraan WHERE id_kendaraan = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, idKendaraan);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
