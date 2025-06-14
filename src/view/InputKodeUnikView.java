package view;

import controller.ParkirController;
import controller.KendaraanController;
import model.Kendaraan;
import model.User;
import service.QrCodeService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.List;
import java.util.Random; // Import untuk Random

public class InputKodeUnikView extends JFrame {

    private User user;
    private ParkirController parkirController;
    private KendaraanController kendaraanController;
    private QrCodeService qrCodeService;

    private JTextField txtKodeUnik;
    private JComboBox<Kendaraan> comboKendaraan;
    private JTextField txtTokenUnik; // Tambahkan JTextField untuk token unik
    private JButton btnGenerateToken; // Tombol untuk generate token
    private JButton btnParkirMasuk;
    private JButton btnParkirKeluar;
    private JButton btnKembali;
    private JLabel lblStatus;

    private final String TOKEN_PLACEHOLDER = "Masukkan minimal 3 huruf atau angka"; // Placeholder text

    public InputKodeUnikView(User user, ParkirController parkirController, KendaraanController kendaraanController, QrCodeService qrCodeService) {
        this.user = user;
        this.parkirController = parkirController;
        this.kendaraanController = kendaraanController;
        this.qrCodeService = qrCodeService;

        setTitle("Input Kode Unik Parkir - Mahasiswa: " + user.getNamaUser());
        setSize(450, 400); // Sesuaikan ukuran frame
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initComponents();
        loadKendaraan();
        updateUIBasedOnParkirStatus();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        JLabel lblInstruksi = new JLabel("Masukkan Kode Unik Parkir:");
        lblInstruksi.setFont(new Font("Arial", Font.BOLD, 14));
        lblInstruksi.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblInstruksi, BorderLayout.NORTH);

        JPanel panelInput = new JPanel();
        panelInput.setLayout(new BoxLayout(panelInput, BoxLayout.Y_AXIS));
        panelInput.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));

        txtKodeUnik = new JTextField();
        txtKodeUnik.setFont(new Font("Arial", Font.PLAIN, 16));
        txtKodeUnik.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        panelInput.add(new JLabel("Kode Unik:"));
        panelInput.add(txtKodeUnik);

        comboKendaraan = new JComboBox<>();
        comboKendaraan.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        panelInput.add(Box.createRigidArea(new Dimension(0, 15)));
        panelInput.add(new JLabel("Pilih Kendaraan:"));
        panelInput.add(comboKendaraan);

        // Tambahkan input field dan tombol generate token
        panelInput.add(Box.createRigidArea(new Dimension(0, 15)));
        panelInput.add(new JLabel("Token Unik:"));

        // Gunakan JPanel dengan FlowLayout untuk menempatkan JTextField dan JButton secara horizontal
        JPanel panelToken = new JPanel();
        panelToken.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 0)); // Spasi 10px antar komponen, align kiri
        panelToken.setAlignmentX(Component.LEFT_ALIGNMENT); // Pastikan panel rata kiri

        txtTokenUnik = new JTextField();
        txtTokenUnik.setFont(new Font("Arial", Font.ITALIC, 14)); // Font italic untuk placeholder
        txtTokenUnik.setForeground(Color.GRAY); // Warna abu-abu untuk placeholder
        txtTokenUnik.setText(TOKEN_PLACEHOLDER); // Set placeholder awal
        txtTokenUnik.setPreferredSize(new Dimension(250, 30)); // Set ukuran preferred untuk field

        // Tambahkan FocusListener untuk placeholder
        txtTokenUnik.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (txtTokenUnik.getText().equals(TOKEN_PLACEHOLDER)) {
                    txtTokenUnik.setText("");
                    txtTokenUnik.setFont(new Font("Arial", Font.PLAIN, 16));
                    txtTokenUnik.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (txtTokenUnik.getText().isEmpty()) {
                    txtTokenUnik.setText(TOKEN_PLACEHOLDER);
                    txtTokenUnik.setFont(new Font("Arial", Font.ITALIC, 14));
                    txtTokenUnik.setForeground(Color.GRAY);
                }
            }
        });

        btnGenerateToken = new JButton("Generate");
        btnGenerateToken.setFont(new Font("Arial", Font.PLAIN, 12));
        btnGenerateToken.setPreferredSize(new Dimension(90, 30)); // Set ukuran preferred untuk tombol

        panelToken.add(txtTokenUnik);
        panelToken.add(btnGenerateToken);
        panelInput.add(panelToken);


        add(panelInput, BorderLayout.CENTER);

        JPanel panelBawah = new JPanel();
        panelBawah.setLayout(new BoxLayout(panelBawah, BoxLayout.Y_AXIS));
        panelBawah.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));

        JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        btnParkirMasuk = new JButton("Parkir Masuk");
        btnParkirKeluar = new JButton("Parkir Keluar");
        btnKembali = new JButton("Kembali");
        panelButtons.add(btnParkirMasuk);
        panelButtons.add(btnParkirKeluar);
        panelButtons.add(btnKembali);

        lblStatus = new JLabel(" ");
        lblStatus.setHorizontalAlignment(SwingConstants.CENTER);
        lblStatus.setForeground(Color.BLUE);
        lblStatus.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelBawah.add(panelButtons);
        panelBawah.add(lblStatus);

        add(panelBawah, BorderLayout.SOUTH);

        // Action Listener untuk tombol generate token
        btnGenerateToken.addActionListener(e -> {
            txtTokenUnik.setText(generateRandomCode(4)); // Generate 6 karakter acak
            txtTokenUnik.setFont(new Font("Arial", Font.PLAIN, 16)); // Kembalikan font normal setelah generate
            txtTokenUnik.setForeground(Color.BLACK); // Kembalikan warna teks normal
        });

        btnParkirMasuk.addActionListener(e -> {
            String kodeUnik = txtKodeUnik.getText().trim();
            String tokenUnik = txtTokenUnik.getText().trim(); // Ambil nilai token unik

            if (kodeUnik.isEmpty()) {
                showStatus("Kode unik tidak boleh kosong.", Color.RED);
                return;
            }

            // Validasi token unik, pastikan bukan placeholder
            if (tokenUnik.isEmpty() || tokenUnik.equals(TOKEN_PLACEHOLDER)) {
                showStatus("Token unik tidak boleh kosong atau masih placeholder.", Color.RED);
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

            Kendaraan kendaraan = (Kendaraan) comboKendaraan.getSelectedItem();
            if (kendaraan == null) {
                showStatus("Pilih kendaraan terlebih dahulu.", Color.RED);
                return;
            }

            boolean sukses = parkirController.parkirMasuk(user.getIdUser(), kendaraan.getIdKendaraan(), kodeUnik, tokenUnik);
            if (sukses) {
                showStatus("Parkir masuk berhasil!", Color.GREEN.darker());
                comboKendaraan.setEnabled(false);
                btnParkirMasuk.setEnabled(false);
                btnParkirKeluar.setEnabled(true);
                btnGenerateToken.setEnabled(false); // Nonaktifkan tombol generate setelah parkir masuk
            } else {
                showStatus("Parkir masuk gagal, coba lagi.", Color.RED);
            }
        });

        btnParkirKeluar.addActionListener(e -> {
            String kodeUnik = txtKodeUnik.getText().trim();
            String tokenUnik = txtTokenUnik.getText().trim(); // Ambil nilai token unik

            if (kodeUnik.isEmpty()) {
                showStatus("Kode unik tidak boleh kosong.", Color.RED);
                return;
            }

            // Validasi token unik, pastikan bukan placeholder
            if (tokenUnik.isEmpty() || tokenUnik.equals(TOKEN_PLACEHOLDER)) {
                showStatus("Token unik tidak boleh kosong atau masih placeholder.", Color.RED);
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

            if(!parkirController.tokenUnikIsValid(user.getIdUser(), tokenUnik)){
                showStatus("Token unik anda salah.", Color.RED);
                return;
            }

            if (!parkirController.hasActiveParkir(user.getIdUser())) {
                showStatus("Anda belum melakukan parkir masuk.", Color.RED);
                return;
            }

            boolean sukses = parkirController.parkirKeluar(user.getIdUser(), kodeUnik, tokenUnik);
            if (sukses) {
                showStatus("Parkir keluar berhasil!", Color.GREEN.darker());
                comboKendaraan.setEnabled(true);
                btnParkirMasuk.setEnabled(true);
                btnParkirKeluar.setEnabled(false);
                btnGenerateToken.setEnabled(true); // Aktifkan kembali tombol generate setelah parkir keluar
                txtTokenUnik.setText(TOKEN_PLACEHOLDER); // Set kembali ke placeholder
                txtTokenUnik.setFont(new Font("Arial", Font.ITALIC, 14)); // Font italic untuk placeholder
                txtTokenUnik.setForeground(Color.GRAY); // Warna abu-abu untuk placeholder
            } else {
                showStatus("Parkir keluar gagal, coba lagi.", Color.RED);
            }
        });

        btnKembali.addActionListener(e -> {
            dispose();
            new MahasiswaDashboardView(user).setVisible(true);
        });
    }

    // Metode untuk menghasilkan kode acak
    private String generateRandomCode(int length) {
        String chars = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }

    private void loadKendaraan() {
        List<Kendaraan> daftarKendaraan = kendaraanController.getKendaraanByUser(user.getIdUser());
        DefaultComboBoxModel<Kendaraan> model = new DefaultComboBoxModel<>();
        for (Kendaraan k : daftarKendaraan) {
            model.addElement(k);
        }
        comboKendaraan.setModel(model);
    }

    private void updateUIBasedOnParkirStatus() {
        boolean hasActiveParkir = parkirController.hasActiveParkir(user.getIdUser());
        if (hasActiveParkir) {
            String kendaraanId = parkirController.getActiveParkirKendaraanId(user.getIdUser());
            Kendaraan kendaraanAktif = null;
            for (int i = 0; i < comboKendaraan.getItemCount(); i++) {
                Kendaraan k = comboKendaraan.getItemAt(i);
                if (k.getIdKendaraan().equals(kendaraanId)) {
                    kendaraanAktif = k;
                    break;
                }
            }

            if (kendaraanAktif != null) {
                comboKendaraan.setSelectedItem(kendaraanAktif);
            }

            comboKendaraan.setEnabled(false);
            btnParkirMasuk.setEnabled(false);
            btnParkirKeluar.setEnabled(true);
            btnGenerateToken.setEnabled(false); // Nonaktifkan tombol generate
            showStatus("Anda sedang parkir dengan kendaraan: " + (kendaraanAktif != null ? kendaraanAktif.getStnkId() : "-"), Color.BLUE);
        } else {
            comboKendaraan.setEnabled(true);
            btnParkirMasuk.setEnabled(true);
            btnParkirKeluar.setEnabled(false);
            btnGenerateToken.setEnabled(true); // Aktifkan tombol generate
            txtTokenUnik.setText(TOKEN_PLACEHOLDER); // Set placeholder
            txtTokenUnik.setFont(new Font("Arial", Font.ITALIC, 14)); // Font italic untuk placeholder
            txtTokenUnik.setForeground(Color.GRAY); // Warna abu-abu untuk placeholder
            showStatus("Silakan input kode unik dan pilih kendaraan untuk parkir masuk.", Color.BLACK);
        }
    }

    private void showStatus(String message, Color color) {
        lblStatus.setText(message);
        lblStatus.setForeground(color);
    }
}
