package view;

import controller.KendaraanController;
import model.Kendaraan;
import model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class KendaraanView extends JFrame {

    private User user;
    private KendaraanController kendaraanController;

    private JTable table;
    private DefaultTableModel tableModel;

    private JTextField txtId, txtStnkId, txtModel;
    private JButton btnTambah, btnUpdate, btnHapus, btnKembali;

    public KendaraanView(User user, KendaraanController controller) {
        this.user = user;
        this.kendaraanController = controller;

        setTitle("CRUD Kendaraan - Mahasiswa: " + user.getNamaUser());
        setSize(700, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initComponents();
        loadTableData();
        aturTombol();
    }

    private void initComponents() {
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Form Kendaraan"));

        formPanel.add(new JLabel("ID Kendaraan (auto)"));
        txtId = new JTextField();
        txtId.setEditable(false);
        formPanel.add(txtId);

        formPanel.add(new JLabel("Nomor Polisi"));
        txtStnkId = new JTextField();
        formPanel.add(txtStnkId);

        formPanel.add(new JLabel("Model Kendaraan"));
        txtModel = new JTextField();
        formPanel.add(txtModel);

        btnTambah = new JButton("Tambah");
        btnUpdate = new JButton("Update");
        btnHapus = new JButton("Hapus");
        btnKembali = new JButton("Kembali");

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnPanel.add(btnTambah);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnHapus);
        btnPanel.add(btnKembali);

        tableModel = new DefaultTableModel(new Object[]{"No", "Nomor Polisi", "Model"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        setLayout(new BorderLayout(10, 10));
        add(formPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);

        btnTambah.addActionListener(e -> tambahKendaraan());
        btnUpdate.addActionListener(e -> updateKendaraan());
        btnHapus.addActionListener(e -> hapusKendaraan());
        btnKembali.addActionListener(e -> kembaliKeDashboard());

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    Kendaraan kendaraanTerpilih = kendaraanController.getKendaraanByUser(user.getIdUser()).get(selectedRow);
                    txtId.setText(String.valueOf(kendaraanTerpilih.getIdKendaraan()));
                    txtStnkId.setText(kendaraanTerpilih.getStnkId());
                    txtModel.setText(kendaraanTerpilih.getModelKendaraan());
                } else {
                    clearForm();
                }
                aturTombol();
            }
        });
    }

    private void loadTableData() {
        tableModel.setRowCount(0);
        List<Kendaraan> list = kendaraanController.getKendaraanByUser(user.getIdUser());

        int no = 1;
        for (Kendaraan k : list) {
            tableModel.addRow(new Object[]{
                    no++,
                    k.getStnkId(),
                    k.getModelKendaraan()
            });
        }
    }

    private void tambahKendaraan() {
        String stnkId = txtStnkId.getText().trim();
        String model = txtModel.getText().trim();

        if (stnkId.isEmpty() || model.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nomor Polisi dan Model Kendaraan wajib diisi", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        kendaraanController.tambahKendaraan(user.getIdUser(), stnkId, model, "");
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

        if (stnkId.isEmpty() || model.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nomor Polisi dan Model Kendaraan wajib diisi", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        kendaraanController.updateKendaraan(id, user.getIdUser(), stnkId, model, "");
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
        aturTombol();
    }

    private void aturTombol() {
        boolean adaDataDipilih = !txtId.getText().trim().isEmpty();

        btnTambah.setEnabled(!adaDataDipilih);
        btnUpdate.setEnabled(adaDataDipilih);
        btnHapus.setEnabled(adaDataDipilih);
    }

    private void kembaliKeDashboard() {
        dispose();
        new MahasiswaDashboardView(user).setVisible(true);
    }
}
