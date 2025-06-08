package view;

import config.Database;
import controller.QrCodeController;
import model.User;
import controller.ParkirController;
import service.QrCodeService;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;

public class SatpamDashboardView extends JFrame {

    private User user;
    private ParkirController parkirController;

    private JButton btnTampilkanQRCode;
    private JButton btnDataMasuk;
    private JButton btnRiwayatParkir;
    private JButton btnLogout;

    public SatpamDashboardView(User user, ParkirController parkirController) {
        this.user = user;
        this.parkirController = parkirController;

        setTitle("Dashboard Satpam - " + user.getNamaUser());
        setSize(500, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        JLabel lblTitle = new JLabel("Selamat Datang, Satpam " + user.getNamaUser(), SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        add(lblTitle, BorderLayout.NORTH);

        JPanel panelButtons = new JPanel(new GridLayout(4, 1, 15, 15));
        panelButtons.setBorder(BorderFactory.createEmptyBorder(30, 80, 30, 80));

        btnTampilkanQRCode = new JButton("Tampilkan QR Code Parkir");
        btnDataMasuk = new JButton("Lihat Data Mahasiswa Masuk");
        btnRiwayatParkir = new JButton("Lihat Riwayat Parkir Mahasiswa");
        btnLogout = new JButton("Logout");

        panelButtons.add(btnTampilkanQRCode);
        panelButtons.add(btnDataMasuk);
        panelButtons.add(btnRiwayatParkir);
        panelButtons.add(btnLogout);

        add(panelButtons, BorderLayout.CENTER);

        // Action Listeners
        btnTampilkanQRCode.addActionListener(e -> {
            Connection conn = Database.connect();
            QrCodeService qrCodeService = new QrCodeService(conn);
            QrCodeController qrCodeController = new QrCodeController(qrCodeService);
            QrCodeGenerateView qrCodeView = new QrCodeGenerateView(qrCodeController);
            qrCodeView.setVisible(true);
        });
        btnDataMasuk.addActionListener(e -> {
            // TODO: Implementasi tampilkan data mahasiswa yang sedang parkir
            JOptionPane.showMessageDialog(this, "Fitur Data Mahasiswa Masuk belum diimplementasi.");
        });

        btnRiwayatParkir.addActionListener(e -> {
            // TODO: Implementasi tampilkan riwayat parkir mahasiswa
            JOptionPane.showMessageDialog(this, "Fitur Riwayat Parkir belum diimplementasi.");
        });

        btnLogout.addActionListener(e -> {
            dispose();
            new LoginView().setVisible(true); // Pastikan LoginView sudah ada
        });
    }
}
