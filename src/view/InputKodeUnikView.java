package view;

import controller.ParkirController;
import model.User;
import service.QrCodeService;

import javax.swing.*;
import java.awt.*;

public class InputKodeUnikView extends JFrame {

    private User user;
    private ParkirController parkirController;
    private QrCodeService qrCodeService;

    private JTextField txtKodeUnik;
    private JButton btnParkirMasuk;
    private JButton btnParkirKeluar;
    private JButton btnKembali;
    private JLabel lblStatus;

    public InputKodeUnikView(User user, ParkirController parkirController, QrCodeService qrCodeService) {
        this.user = user;
        this.parkirController = parkirController;
        this.qrCodeService = qrCodeService;

        setTitle("Input Kode Unik Parkir - Mahasiswa: " + user.getNamaUser());
        setSize(450, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        JLabel lblInstruksi = new JLabel("Masukkan Kode Unik Parkir:");
        lblInstruksi.setFont(new Font("Arial", Font.BOLD, 14));
        lblInstruksi.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblInstruksi, BorderLayout.NORTH);

        // Panel input kode unik
        JPanel panelInput = new JPanel(new BorderLayout());
        txtKodeUnik = new JTextField();
        txtKodeUnik.setFont(new Font("Arial", Font.PLAIN, 16));
        panelInput.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        panelInput.add(txtKodeUnik, BorderLayout.CENTER);
        add(panelInput, BorderLayout.CENTER);

        // Panel bawah: tombol + status
        JPanel panelBawah = new JPanel();
        panelBawah.setLayout(new BoxLayout(panelBawah, BoxLayout.Y_AXIS));
        panelBawah.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));

        // Panel tombol
        JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        btnParkirMasuk = new JButton("Parkir Masuk");
        btnParkirKeluar = new JButton("Parkir Keluar");
        btnKembali = new JButton("Kembali");
        panelButtons.add(btnParkirMasuk);
        panelButtons.add(btnParkirKeluar);
        panelButtons.add(btnKembali);

        // Label status
        lblStatus = new JLabel(" ");
        lblStatus.setHorizontalAlignment(SwingConstants.CENTER);
        lblStatus.setForeground(Color.BLUE);
        lblStatus.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelBawah.add(panelButtons);
        panelBawah.add(lblStatus);

        add(panelBawah, BorderLayout.SOUTH);

        // Action tombol Parkir Masuk
        btnParkirMasuk.addActionListener(e -> {
            String kodeUnik = txtKodeUnik.getText().trim();
            if (kodeUnik.isEmpty()) {
                showStatus("Kode unik tidak boleh kosong.", Color.RED);
                return;
            }

            if (!qrCodeService.isKodeUnikValid(kodeUnik)) {
                showStatus("Kode unik tidak ditemukan atau sudah tidak aktif.", Color.RED);
                return;
            }

            if (qrCodeService.isKodeUnikExpired(kodeUnik)) {
                showStatus("Kode unik sudah kedaluwarsa.", Color.RED);
                return;
            }

            if (parkirController.hasActiveParkir(user.getIdUser())) {
                showStatus("Anda sudah memiliki parkir aktif.", Color.RED);
                return;
            }

            boolean sukses = parkirController.parkirMasuk(user.getIdUser(), kodeUnik);
            if (sukses) {
                showStatus("Parkir masuk berhasil!", Color.GREEN.darker());
            } else {
                showStatus("Parkir masuk gagal, coba lagi.", Color.RED);
            }
        });

        // Action tombol Parkir Keluar
        btnParkirKeluar.addActionListener(e -> {
            String kodeUnik = txtKodeUnik.getText().trim();
            if (kodeUnik.isEmpty()) {
                showStatus("Kode unik tidak boleh kosong.", Color.RED);
                return;
            }

            if (!qrCodeService.isKodeUnikValid(kodeUnik)) {
                showStatus("Kode unik tidak ditemukan atau sudah tidak aktif.", Color.RED);
                return;
            }

            if (qrCodeService.isKodeUnikExpired(kodeUnik)) {
                showStatus("Kode unik sudah kedaluwarsa.", Color.RED);
                return;
            }

            if (!parkirController.hasActiveParkir(user.getIdUser())) {
                showStatus("Anda belum melakukan parkir masuk.", Color.RED);
                return;
            }

            boolean sukses = parkirController.parkirKeluar(user.getIdUser(), kodeUnik);
            if (sukses) {
                showStatus("Parkir keluar berhasil!", Color.GREEN.darker());
            } else {
                showStatus("Parkir keluar gagal, coba lagi.", Color.RED);
            }
        });

        // Action tombol kembali
        btnKembali.addActionListener(e -> {
            dispose();
            new MahasiswaDashboardView(user).setVisible(true);
        });
    }

    private void showStatus(String message, Color color) {
        lblStatus.setText(message);
        lblStatus.setForeground(color);
    }
}
