package service;

import model.QrCodeSession;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class QrCodeService {

    private final Connection connection;
    private final Random random;

    public QrCodeService(Connection connection) {
        this.connection = connection;
        this.random = new Random();
    }

    public String generateKodeUnik() {
        int kode = 100 + random.nextInt(900);
        return String.valueOf(kode);
    }

    public boolean isKodeUnikExist(String kodeUnik) {
        String sql = "SELECT COUNT(*) FROM qr_code_session WHERE kode_unik = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, kodeUnik);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String generateUniqueKodeUnik() {
        String kodeUnik;
        do {
            kodeUnik = generateKodeUnik();
        } while (isKodeUnikExist(kodeUnik));
        return kodeUnik;
    }

    public boolean saveQrCodeSession(String kodeUnik, LocalDateTime expiredAt) {
        String sql = "INSERT INTO qr_code_session (kode_unik, waktu_generate, expired_at) VALUES (?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            String waktuGenerate = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            ps.setString(1, kodeUnik);
            ps.setString(2, waktuGenerate);
            ps.setString(3, expiredAt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public QrCodeSession getQrCodeSessionByKode(String kodeUnik) {
        String sql = "SELECT * FROM qr_code_session WHERE kode_unik = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, kodeUnik);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                QrCodeSession session = new QrCodeSession();
                session.setIdQr(rs.getInt("id_qr"));
                session.setKodeUnik(rs.getString("kode_unik"));
                session.setWaktuGenerate(rs.getString("waktu_generate"));
                session.setExpiredAt(rs.getString("expired_at"));
                return session;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean isKodeUnikValid(String kodeUnik) {
        QrCodeSession session = getQrCodeSessionByKode(kodeUnik);
        if (session == null) return false;

        try {
            LocalDateTime expiredAt = LocalDateTime.parse(session.getExpiredAt());
            return LocalDateTime.now().isBefore(expiredAt);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isKodeUnikExpired(String kodeUnik) {
        QrCodeSession session = getQrCodeSessionByKode(kodeUnik);
        if (session == null) return true; // Kalau ga ada di DB, dianggap expired/invalid

        try {
            LocalDateTime expiredAt = LocalDateTime.parse(session.getExpiredAt());
            return LocalDateTime.now().isAfter(expiredAt);
        } catch (Exception e) {
            e.printStackTrace();
            return true; // Kalau error parsing, anggap expired
        }
    }
}
