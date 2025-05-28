package service;

import model.Parkir;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ParkirService {

    private Connection conn;

    public ParkirService(Connection conn) {
        this.conn = conn;
    }

    public List<Parkir> getRiwayatParkirByUser(String userId) {
        List<Parkir> list = new ArrayList<>();
        String sql = "SELECT * FROM parkir WHERE users_id = ? ORDER BY waktu_masuk DESC";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userId); // pakai String karena id di DB bertipe TEXT
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Parkir p = new Parkir();
                p.setIdParkir(rs.getInt("id_parkir"));
                p.setUserId(rs.getString("users_id"));

                Timestamp masukTs = rs.getTimestamp("waktu_masuk");
                if (masukTs != null) p.setWaktuMasuk(masukTs.toLocalDateTime());

                Timestamp keluarTs = rs.getTimestamp("waktu_keluar");
                if (keluarTs != null) p.setWaktuKeluar(keluarTs.toLocalDateTime());

                Timestamp createdTs = rs.getTimestamp("created_at");
                if (createdTs != null) p.setCreatedAt(createdTs.toLocalDateTime());

                Timestamp updatedTs = rs.getTimestamp("updated_at");
                if (updatedTs != null) p.setUpdatedAt(updatedTs.toLocalDateTime());

                list.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
}
