package view;

import config.Database;
import controller.KeluhanController;
import controller.KendaraanController;
import controller.ParkirController;
import model.User;
import service.KeluhanService;
import service.KendaraanService;
import service.ParkirService;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;

public class MahasiswaDashboardView extends JFrame {

    private User user;

    private JButton kendaraanBtn;
    private JButton riwayatParkirBtn;
    private JButton laporKeluhanBtn;
    private JButton scanQRBtn;

    public MahasiswaDashboardView(User user) {
        this.user = user;

        setTitle("Dashboard Mahasiswa - " + user.getNamaUser());
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initComponents();
    }

    private void initComponents() {
        JLabel welcomeLabel = new JLabel("Selamat datang, " + user.getNamaUser(), JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));

        kendaraanBtn = new JButton("Kelola Kendaraan");
        riwayatParkirBtn = new JButton("Riwayat Parkir");
        laporKeluhanBtn = new JButton("Lapor Keluhan");
        scanQRBtn = new JButton("Scan QR");

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        panel.add(welcomeLabel);
        panel.add(kendaraanBtn);
        panel.add(riwayatParkirBtn);
        panel.add(laporKeluhanBtn);
        panel.add(scanQRBtn);

        add(panel);

        kendaraanBtn.addActionListener(e -> {
            dispose(); // tutup dashboard
            Connection conn = Database.connect();
            KendaraanService kendaraanService = new KendaraanService(conn);
            KendaraanController kendaraanController = new KendaraanController(kendaraanService);

            new KendaraanView(user, kendaraanController).setVisible(true);
        });

        riwayatParkirBtn.addActionListener(e -> {
            dispose();
            Connection conn = Database.connect();
            ParkirService parkirService = new ParkirService(conn);
            ParkirController parkirController = new ParkirController(parkirService);

            new RiwayatParkirView(user, parkirController).setVisible(true);
        });

        laporKeluhanBtn.addActionListener(e -> {
            dispose(); // tutup dashboard

            Connection conn = Database.connect();
            KeluhanService keluhanService = new KeluhanService(conn);
            KeluhanController keluhanController = new KeluhanController(keluhanService);

            new KeluhanView(keluhanController, user.getIdUser()).setVisible(true);
        });

        scanQRBtn.addActionListener(e -> {
            System.out.println("Tombol Scan QR diklik");
        });
    }
}