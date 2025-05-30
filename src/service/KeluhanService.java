package service;

import model.Keluhan;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class KeluhanService {
    private Connection conn;

    public KeluhanService() {
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:database/parkir.db");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Insert keluhan baru
    public void insert(Keluhan keluhan) {
        String sql = "INSERT INTO keluhan (id_keluhan, users_id, nama_responden, judul_keluhan, keterangan_keluhan, photo_bukti_url, status_keluhan, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, keluhan.getIdKeluhan());
            stmt.setString(2, keluhan.getUserId());
            stmt.setString(3, keluhan.getNamaResponden());
            stmt.setString(4, keluhan.getJudulKeluhan());
            stmt.setString(5, keluhan.getKeteranganKeluhan());
            stmt.setString(6, keluhan.getPhotoBuktiUrl());
            stmt.setString(7, keluhan.getStatusKeluhan());
            stmt.setString(8, keluhan.getCreatedAt().toString());
            stmt.setString(9, keluhan.getUpdatedAt().toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Ambil semua keluhan berdasarkan users_id
    public List<Keluhan> getAllByUserId(String usersId) {
        List<Keluhan> list = new ArrayList<>();
        String sql = "SELECT * FROM keluhan WHERE users_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, usersId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Keluhan keluhan = new Keluhan(
                        rs.getString("id_keluhan"),
                        rs.getString("users_id"),
                        rs.getString("nama_responden"),
                        rs.getString("judul_keluhan"),
                        rs.getString("keterangan_keluhan"),
                        rs.getString("photo_bukti_url"),
                        rs.getString("status_keluhan"),
                        LocalDateTime.parse(rs.getString("created_at")),
                        LocalDateTime.parse(rs.getString("updated_at"))
                );
                list.add(keluhan);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Update status keluhan
    public void updateStatusKeluhan(String idKeluhan, String statusKeluhanBaru, LocalDateTime updatedAt) {
        String sql = "UPDATE keluhan SET status_keluhan = ?, updated_at = ? WHERE id_keluhan = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, statusKeluhanBaru);
            stmt.setString(2, updatedAt.toString());
            stmt.setString(3, idKeluhan);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Ambil semua keluhan (untuk admin)
    public List<Keluhan> getAllKeluhan() {
        List<Keluhan> listKeluhan = new ArrayList<>();
        String sql = "SELECT * FROM keluhan ORDER BY created_at DESC";

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Keluhan keluhan = new Keluhan(
                        rs.getString("id_keluhan"),
                        rs.getString("users_id"),
                        rs.getString("nama_responden"),
                        rs.getString("judul_keluhan"),
                        rs.getString("keterangan_keluhan"),
                        rs.getString("photo_bukti_url"),
                        rs.getString("status_keluhan"),
                        LocalDateTime.parse(rs.getString("created_at")),
                        LocalDateTime.parse(rs.getString("updated_at"))
                );
                listKeluhan.add(keluhan);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listKeluhan;
    }
}
