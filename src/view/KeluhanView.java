package view;

import controller.KeluhanController;
import model.Keluhan;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class KeluhanView extends JFrame {
    private KeluhanController keluhanController;
    private String usersId; // id user yang login

    private JTable tableKeluhan;
    private DefaultTableModel tableModel;

    // Form tambah keluhan
    private JTextField tfNamaResponden;
    private JTextField tfJudulKeluhan;
    private JTextArea taKeteranganKeluhan;
    private JTextField tfPhotoBuktiUrl;

    public KeluhanView(KeluhanController keluhanController, String usersId) {
        this.keluhanController = keluhanController;
        this.usersId = usersId;
        initComponents();
        loadTableData();
    }

    private void initComponents() {
        setTitle("Data Keluhan");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Layout utama
        JPanel panelMain = new JPanel(new BorderLayout(10, 10));
        panelMain.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        setContentPane(panelMain);

        // Judul
        JLabel lblTitle = new JLabel("Daftar Keluhan Saya");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        panelMain.add(lblTitle, BorderLayout.NORTH);

        // Tabel keluhan
        tableModel = new DefaultTableModel(new Object[]{"ID Keluhan", "Judul", "Status", "Tanggal Dibuat"}, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableKeluhan = new JTable(tableModel);
        JScrollPane scrollTable = new JScrollPane(tableKeluhan);
        panelMain.add(scrollTable, BorderLayout.CENTER);

        // Panel form dan tombol
        JPanel panelForm = new JPanel();
        panelForm.setLayout(new BoxLayout(panelForm, BoxLayout.Y_AXIS));
        panelForm.setBorder(BorderFactory.createTitledBorder("Tambah Keluhan Baru"));

        tfNamaResponden = new JTextField(30);
        tfJudulKeluhan = new JTextField(30);
        taKeteranganKeluhan = new JTextArea(5, 30);
        tfPhotoBuktiUrl = new JTextField(30);

        panelForm.add(new JLabel("Nama Responden:"));
        panelForm.add(tfNamaResponden);

        panelForm.add(new JLabel("Judul Keluhan:"));
        panelForm.add(tfJudulKeluhan);

        panelForm.add(new JLabel("Keterangan Keluhan:"));
        panelForm.add(new JScrollPane(taKeteranganKeluhan));

        panelForm.add(new JLabel("URL Foto Bukti (opsional):"));
        panelForm.add(tfPhotoBuktiUrl);

        JButton btnTambah = new JButton("Tambah Keluhan");
        btnTambah.addActionListener(this::handleTambahKeluhan);

        JButton btnKembali = new JButton("Kembali");
        btnKembali.addActionListener(e -> {
            dispose();
//            new MahasiswaDashboardView().setVisible(true);
        });

        JPanel panelButtons = new JPanel();
        panelButtons.add(btnTambah);
        panelButtons.add(btnKembali);

        panelForm.add(Box.createVerticalStrut(10));
        panelForm.add(panelButtons);

        panelMain.add(panelForm, BorderLayout.EAST);
    }

    private void loadTableData() {
        tableModel.setRowCount(0); // kosongkan dulu
        List<Keluhan> keluhanList = keluhanController.getKeluhanByUser(usersId);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        for (Keluhan k : keluhanList) {
            Object[] rowData = {
                    k.getIdKeluhan(),
                    k.getJudulKeluhan(),
                    k.getStatusKeluhan(),
                    k.getCreatedAt().format(dtf)
            };
            tableModel.addRow(rowData);
        }
    }

    private void handleTambahKeluhan(ActionEvent e) {
        String namaResponden = tfNamaResponden.getText().trim();
        String judulKeluhan = tfJudulKeluhan.getText().trim();
        String keteranganKeluhan = taKeteranganKeluhan.getText().trim();
        String photoBuktiUrl = tfPhotoBuktiUrl.getText().trim();

        if (namaResponden.isEmpty() || judulKeluhan.isEmpty() || keteranganKeluhan.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nama Responden, Judul, dan Keterangan tidak boleh kosong.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        keluhanController.tambahKeluhan(usersId, namaResponden, judulKeluhan, keteranganKeluhan, photoBuktiUrl);
        JOptionPane.showMessageDialog(this, "Keluhan berhasil ditambahkan.", "Informasi", JOptionPane.INFORMATION_MESSAGE);

        // Reset form dan reload data
        tfNamaResponden.setText("");
        tfJudulKeluhan.setText("");
        taKeteranganKeluhan.setText("");
        tfPhotoBuktiUrl.setText("");
        loadTableData();
    }
}
