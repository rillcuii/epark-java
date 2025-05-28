package service;

import mvc.model.Kendaraan;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class KendaraanService {
    private Connection conn;

    public KendaraanService(Connection conn) {
        this.conn = conn;
    }

    public void insert(Kendaraan kendaraan) {
        String sql = "INSERT INTO kendaraan (id_kendaraan, stnk_id, model_kendaraan, url_photo_kendaraan, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, kendaraan.getIdKendaraan());
            stmt.setString(2, kendaraan.getStnkId());
            stmt.setString(3, kendaraan.getModelKendaraan());
            stmt.setString(4, kendaraan.getUrlPhotoKendaraan());
            stmt.setString(5, kendaraan.getCreatedAt().toString());
            stmt.setString(6, kendaraan.getUpdatedAt().toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(Kendaraan kendaraan) {
        String sql = "UPDATE kendaraan SET stnk_id = ?, model_kendaraan = ?, url_photo_kendaraan = ?, updated_at = ? WHERE id_kendaraan = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, kendaraan.getStnkId());
            stmt.setString(2, kendaraan.getModelKendaraan());
            stmt.setString(3, kendaraan.getUrlPhotoKendaraan());
            stmt.setString(4, kendaraan.getUpdatedAt().toString());
            stmt.setString(5, kendaraan.getIdKendaraan());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(String idKendaraan) {
        String sql = "DELETE FROM kendaraan WHERE id_kendaraan = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, idKendaraan);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Kendaraan> getAll() {
        List<Kendaraan> list = new ArrayList<>();
        String sql = "SELECT * FROM kendaraan";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Kendaraan kendaraan = new Kendaraan(
                        rs.getString("id_kendaraan"),
                        rs.getString("stnk_id"),
                        rs.getString("model_kendaraan"),
                        rs.getString("url_photo_kendaraan"),
                        LocalDateTime.parse(rs.getString("created_at")),
                        LocalDateTime.parse(rs.getString("updated_at"))
                );
                list.add(kendaraan);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Kendaraan getById(String id) {
        String sql = "SELECT * FROM kendaraan WHERE id_kendaraan = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Kendaraan(
                        rs.getString("id_kendaraan"),
                        rs.getString("stnk_id"),
                        rs.getString("model_kendaraan"),
                        rs.getString("url_photo_kendaraan"),
                        LocalDateTime.parse(rs.getString("created_at")),
                        LocalDateTime.parse(rs.getString("updated_at"))
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
