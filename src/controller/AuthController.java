package controller;

import model.User;
import service.UserService;
import view.LoginView;
import view.MahasiswaDashboardView;

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
                // case "Satpam": ...
                // case "Admin": ...
                default -> System.out.println("Role tidak dikenali");
            }
        } else {
            System.out.println("Login gagal.");
        }
    }
}
