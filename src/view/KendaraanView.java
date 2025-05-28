package view;

import controller.KendaraanController;
import mvc.model.Kendaraan;
import model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class KendaraanView extends JFrame {

    private User user; // User yang login (kalau mau filter bisa dipakai)
    private KendaraanController kendaraanController;

    private JTable table;
    private DefaultTableModel tableModel;

    private JTextField txtId, txtStnkId, txtModel, txtUrlPhoto;
    private JButton btnTambah, btnUpdate, btnHapus, btnRefresh, btnKembali;

    public KendaraanView(User user, KendaraanController controller) {
        this.user = user;
        this.kendaraanController = controller;

        setTitle("CRUD Kendaraan - Mahasiswa: " + user.getNamaUser());
        setSize(700, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initComponents();
        loadTableData();
    }

    private void initComponents() {
        // Panel input form
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Form Kendaraan"));

        formPanel.add(new JLabel("ID Kendaraan (auto)"));
        txtId = new JTextField();
        txtId.setEditable(false);
        formPanel.add(txtId);

        formPanel.add(new JLabel("STNK ID"));
        txtStnkId = new JTextField();
        formPanel.add(txtStnkId);

        formPanel.add(new JLabel("Model Kendaraan"));
        txtModel = new JTextField();
        formPanel.add(txtModel);

        formPanel.add(new JLabel("URL Foto Kendaraan"));
        txtUrlPhoto = new JTextField();
        formPanel.add(txtUrlPhoto);

        // Tombol CRUD dan Kembali
        btnTambah = new JButton("Tambah");
        btnUpdate = new JButton("Update");
        btnHapus = new JButton("Hapus");
        btnRefresh = new JButton("Refresh");
        btnKembali = new JButton("Kembali");

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnPanel.add(btnTambah);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnHapus);
        btnPanel.add(btnRefresh);
        btnPanel.add(btnKembali);

        // Table
        tableModel = new DefaultTableModel(new Object[]{"ID Kendaraan", "STNK ID", "Model", "URL Foto"}, 0) {
            public boolean isCellEditable(int row, int column) {
                return false; // Table tidak bisa diedit langsung
            }
        };
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        // Layout utama
        setLayout(new BorderLayout(10, 10));
        add(formPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);

        // Event listener tombol
        btnTambah.addActionListener(e -> tambahKendaraan());
        btnUpdate.addActionListener(e -> updateKendaraan());
        btnHapus.addActionListener(e -> hapusKendaraan());
        btnRefresh.addActionListener(e -> loadTableData());
        btnKembali.addActionListener(e -> kembaliKeDashboard());

        // Event saat klik baris di table, data tampil di form
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                int selectedRow = table.getSelectedRow();
                txtId.setText(tableModel.getValueAt(selectedRow, 0).toString());
                txtStnkId.setText(tableModel.getValueAt(selectedRow, 1).toString());
                txtModel.setText(tableModel.getValueAt(selectedRow, 2).toString());
                txtUrlPhoto.setText(tableModel.getValueAt(selectedRow, 3).toString());
            }
        });
    }

    private void loadTableData() {
        tableModel.setRowCount(0); // Clear tabel dulu
        List<Kendaraan> list = kendaraanController.getSemuaKendaraan();
        for (Kendaraan k : list) {
            tableModel.addRow(new Object[]{
                    k.getIdKendaraan(),
                    k.getStnkId(),
                    k.getModelKendaraan(),
                    k.getUrlPhotoKendaraan()
            });
        }
    }

    private void tambahKendaraan() {
        String stnkId = txtStnkId.getText().trim();
        String model = txtModel.getText().trim();
        String urlPhoto = txtUrlPhoto.getText().trim();

        if (stnkId.isEmpty() || model.isEmpty()) {
            JOptionPane.showMessageDialog(this, "STNK ID dan Model Kendaraan wajib diisi", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        kendaraanController.tambahKendaraan(stnkId, model, urlPhoto);
        JOptionPane.showMessageDialog(this, "Data kendaraan berhasil ditambahkan");
        clearForm();
        loadTableData();
    }

    private void updateKendaraan() {
        String id = txtId.getText().trim();
        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Pilih data kendaraan yang ingin diupdate", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String stnkId = txtStnkId.getText().trim();
        String model = txtModel.getText().trim();
        String urlPhoto = txtUrlPhoto.getText().trim();

        if (stnkId.isEmpty() || model.isEmpty()) {
            JOptionPane.showMessageDialog(this, "STNK ID dan Model Kendaraan wajib diisi", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        kendaraanController.updateKendaraan(id, stnkId, model, urlPhoto);
        JOptionPane.showMessageDialog(this, "Data kendaraan berhasil diupdate");
        clearForm();
        loadTableData();
    }

    private void hapusKendaraan() {
        String id = txtId.getText().trim();
        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Pilih data kendaraan yang ingin dihapus", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Yakin ingin menghapus data ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            kendaraanController.hapusKendaraan(id);
            JOptionPane.showMessageDialog(this, "Data kendaraan berhasil dihapus");
            clearForm();
            loadTableData();
        }
    }

    private void clearForm() {
        txtId.setText("");
        txtStnkId.setText("");
        txtModel.setText("");
        txtUrlPhoto.setText("");
    }

    private void kembaliKeDashboard() {
        this.dispose(); // tutup window KendaraanView
        new MahasiswaDashboardView(user).setVisible(true); // buka dashboard (sesuaikan dengan nama class dashboard kamu)
    }
}
