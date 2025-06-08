package view;

import javax.swing.*;
import java.awt.*;

import controller.KeluhanController;
import model.User;
import service.KeluhanService;

public class AdminDashboardView extends JFrame {
    private User admin;

    public AdminDashboardView(User admin) {
        this.admin = admin;

        setTitle("Dashboard Admin");
        setSize(400, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton satpamButton = new JButton("Kelola Data Satpam");
        JButton keluhanButton = new JButton("Kelola Data Keluhan");
        JButton logoutButton = new JButton("Logout");

        satpamButton.addActionListener(e -> {
            SatpamCrudView crudView = new SatpamCrudView();
            crudView.setVisible(true);
            dispose();
        });
        keluhanButton.addActionListener(e -> {
            KeluhanService keluhanService = new KeluhanService();
            KeluhanController keluhanController = new KeluhanController(keluhanService);
            KeluhanAdminView keluhanAdminView = new KeluhanAdminView(keluhanController, admin);
            keluhanAdminView.setVisible(true);
            dispose();
        });
        logoutButton.addActionListener(e -> {
            int konfirmasi = JOptionPane.showConfirmDialog(this,
                    "Yakin ingin logout?",
                    "Konfirmasi Logout",
                    JOptionPane.YES_NO_OPTION);
            if (konfirmasi == JOptionPane.YES_OPTION) {
                new LoginView().setVisible(true); // Pastikan LoginView tersedia
                dispose();
            }
        });
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1, 10, 10));
        panel.add(satpamButton);
        panel.add(keluhanButton);
        panel.add(logoutButton);

        add(panel);
        setVisible(true);
    }
}
