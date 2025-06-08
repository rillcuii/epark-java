package controller;

import config.Database;
import model.User;
import service.ParkirService;
import service.QrCodeService;
import service.UserService;
import view.AdminDashboardView;
import view.LoginView;
import view.MahasiswaDashboardView;
import view.SatpamDashboardView;

import java.sql.Connection;

public class AuthController {
    private final UserService userService = new UserService();
    private LoginView loginView;

    public AuthController(LoginView loginView) {
        this.loginView = loginView;
    }

    public void login(String username, String password) {
        User user = userService.login(username, password);
        if (user != null) {
            switch (user.getRole()) {
                case "Mahasiswa" -> {
                    MahasiswaDashboardView dashboard = new MahasiswaDashboardView(user);
                    dashboard.setVisible(true);
                    loginView.dispose(); // tutup login window
                }
                case "Satpam" -> {
                    Connection conn = Database.connect();
                    QrCodeService qrCodeService = new QrCodeService(conn);  // buat objek QrCodeService
                    ParkirService parkirService = new ParkirService(conn, qrCodeService);  // kirim 2 parameter
                    ParkirController parkirController = new ParkirController(parkirService);

                    SatpamDashboardView dashboard = new SatpamDashboardView(user, parkirController);
                    dashboard.setVisible(true);
                    loginView.dispose();
                }

                case "Admin" -> {
                    System.out.println("Berhasil login sebagai ADMIN.");
                    AdminDashboardView adminDashboard = new AdminDashboardView(user);
                    adminDashboard.setVisible(true);
                    loginView.dispose();
                }
                default -> System.out.println("Role tidak dikenali: " + user.getRole());
            }
        } else {
            System.out.println("Login gagal.");
        }
    }
}
