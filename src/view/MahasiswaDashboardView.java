package view;

import config.Database;
import controller.KeluhanController;
import controller.KendaraanController;
import controller.ParkirController;
import model.User;
import service.KeluhanService;
import service.KendaraanService;
import service.ParkirService;
import service.QrCodeService;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;

public class MahasiswaDashboardView extends JFrame {

    private final User user;
    private Connection conn;
    private QrCodeService qrCodeService;
    private ParkirService parkirService;

    private JButton kendaraanBtn;
    private JButton riwayatParkirBtn;
    private JButton laporKeluhanBtn;
    private JButton scanQRBtn;
    private JButton logoutBtn;

    public MahasiswaDashboardView(User user) {
        this.user = user;
        this.conn = Database.connect();
        this.qrCodeService = new QrCodeService(conn);
        this.parkirService = new ParkirService(conn, qrCodeService);

        setTitle("Dashboard Mahasiswa - " + user.getNamaUser());
        setSize(400, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initComponents();
    }

    private void initComponents() {
        String tokenUnik = parkirService.getTokenUnik(user.getIdUser());
        String displayTokenUnik = tokenUnik == null ? "" : "#" + tokenUnik;
        JLabel welcomeLabel = new JLabel("Selamat datang, " + user.getNamaUser() + displayTokenUnik , JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));

        kendaraanBtn = new JButton("Kelola Kendaraan");
        riwayatParkirBtn = new JButton("Riwayat Parkir");
        laporKeluhanBtn = new JButton("Lapor Keluhan");
        scanQRBtn = new JButton("Scan QR");
        logoutBtn = new JButton("Logout");

        JPanel panel = new JPanel(new GridLayout(6, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        panel.add(welcomeLabel);
        panel.add(kendaraanBtn);
        panel.add(riwayatParkirBtn);
        panel.add(laporKeluhanBtn);
        panel.add(scanQRBtn);
        panel.add(logoutBtn);

        add(panel);

        kendaraanBtn.addActionListener(e -> {
            dispose();
            Connection conn = Database.connect();
            KendaraanService kendaraanService = new KendaraanService(conn);
            KendaraanController kendaraanController = new KendaraanController(kendaraanService);
            new KendaraanView(user, kendaraanController).setVisible(true);
        });

        riwayatParkirBtn.addActionListener(e -> {
            dispose();
            Connection conn = Database.connect();
            QrCodeService qrCodeService = new QrCodeService(conn);
            ParkirService parkirService = new ParkirService(conn, qrCodeService);
            ParkirController parkirController = new ParkirController(parkirService);
            new RiwayatParkirView(user, parkirController).setVisible(true);
        });

        laporKeluhanBtn.addActionListener(e -> {
            dispose();
            KeluhanService keluhanService = new KeluhanService();
            KeluhanController keluhanController = new KeluhanController(keluhanService);
            new KeluhanView(user, keluhanController, user.getIdUser()).setVisible(true);
        });

        scanQRBtn.addActionListener(e -> {
            dispose();
            Connection conn = Database.connect();
            QrCodeService qrCodeService = new QrCodeService(conn);
            KendaraanService kendaraanService = new KendaraanService(conn);
            KendaraanController kendaraanController = new KendaraanController(kendaraanService);
            ParkirService parkirService = new ParkirService(conn, qrCodeService);
            ParkirController parkirController = new ParkirController(parkirService);
            new InputKodeUnikView(user, parkirController, kendaraanController, qrCodeService).setVisible(true);
        });

        logoutBtn.addActionListener(e -> {
            int konfirmasi = JOptionPane.showConfirmDialog(
                    this,
                    "Yakin ingin logout?",
                    "Konfirmasi Logout",
                    JOptionPane.YES_NO_OPTION);
            if (konfirmasi == JOptionPane.YES_OPTION) {
                new LoginView().setVisible(true);
                dispose();
            }
        });
    }
}
