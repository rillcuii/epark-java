package view;

import config.Database;
import controller.QrCodeController;
import model.User;
import controller.ParkirController;
import service.ParkirService;
import service.QrCodeService;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;

public class SatpamDashboardView extends JFrame {

    private User user;
    private ParkirController parkirController;

    private JButton btnTampilkanQRCode;
    private JButton btnDataMasuk;
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
        btnLogout = new JButton("Logout");

        panelButtons.add(btnTampilkanQRCode);
        panelButtons.add(btnDataMasuk);
        panelButtons.add(btnLogout);

        add(panelButtons, BorderLayout.CENTER);

        btnTampilkanQRCode.addActionListener(e -> {
            Connection conn = Database.connect();
            QrCodeService qrCodeService = new QrCodeService(conn);
            QrCodeController qrCodeController = new QrCodeController(qrCodeService);
            QrCodeGenerateView qrCodeView = new QrCodeGenerateView(qrCodeController);
            qrCodeView.setVisible(true);
        });
        btnDataMasuk.addActionListener(e -> {
            Connection conn = Database.connect();
            QrCodeService qrCodeService = new QrCodeService(conn);
            ParkirService parkirService = new ParkirService(conn, qrCodeService);
            ParkirController parkirController = new ParkirController(parkirService);

            SatpamParkirKeluarView keluarView = new SatpamParkirKeluarView(parkirController);
            keluarView.setVisible(true);
        });

        btnLogout.addActionListener(e -> {
            dispose();
            new LoginView().setVisible(true);
        });
    }
}
