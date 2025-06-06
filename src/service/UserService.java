package service;

import config.Database;
import model.User;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserService {
    public User login(String username, String password) {
        try (Connection conn = Database.connect()) {
            String sql = "SELECT * FROM users WHERE username=? AND password=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapUser(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void insertUser(User user) {
        try (Connection conn = Database.connect()) {
            String sql = "INSERT INTO users (id_user, nama_user, username, password, role, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            stmt.setString(1, user.getIdUser());
            stmt.setString(2, user.getNamaUser());
            stmt.setString(3, user.getUsername());
            stmt.setString(4, user.getPassword());
            stmt.setString(5, user.getRole());

            stmt.setString(6, user.getCreatedAt().format(formatter));
            stmt.setString(7, user.getUpdatedAt().format(formatter));

            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateUser(User user) {
        try (Connection conn = Database.connect()) {
            String sql = "UPDATE users SET nama_user=?, username=?, password=?, updated_at=? WHERE id_user=?";
            PreparedStatement stmt = conn.prepareStatement(sql);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String nowFormatted = LocalDateTime.now().format(formatter);

            stmt.setString(1, user.getNamaUser());
            stmt.setString(2, user.getUsername());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, nowFormatted);
            stmt.setString(5, user.getIdUser());

            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void deleteUser(String idUser) {
        try (Connection conn = Database.connect()) {
            String sql = "DELETE FROM users WHERE id_user=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, idUser);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<User> getAllSatpam() {
        List<User> list = new ArrayList<>();
        try (Connection conn = Database.connect()) {
            String sql = "SELECT * FROM users WHERE role='Satpam'";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                list.add(mapUser(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public String generateId() {
        return "U" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private User mapUser(ResultSet rs) throws SQLException {
        String createdAtStr = rs.getString("created_at");
        String updatedAtStr = rs.getString("updated_at");
        LocalDateTime createdAt = null;
        LocalDateTime updatedAt = null;

        if (createdAtStr != null && !createdAtStr.isEmpty()) {
            createdAt = LocalDateTime.parse(createdAtStr, formatter);
        }
        if (updatedAtStr != null && !updatedAtStr.isEmpty()) {
            updatedAt = LocalDateTime.parse(updatedAtStr, formatter);
        }

        return new User(
                rs.getString("id_user"),
                rs.getString("nama_user"),
                rs.getString("username"),
                rs.getString("password"),
                rs.getString("role"),
                createdAt,
                updatedAt
        );
    }
}
