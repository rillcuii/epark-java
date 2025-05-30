package view;

import controller.KeluhanController;
import model.Keluhan;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class KeluhanAdminView extends JFrame {
    private KeluhanController keluhanController;

    private JTable tableKeluhan;
    private DefaultTableModel tableModel;
    private JComboBox<String> comboStatus;
    private JButton btnUpdateStatus;

    public KeluhanAdminView(KeluhanController keluhanController) {
        this.keluhanController = keluhanController;
        initComponents();
        loadDataKeluhan();
    }

    private void initComponents() {
        setTitle("Admin Kelola Keluhan");
        setSize(900, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Setup tabel
        tableModel = new DefaultTableModel(new String[]{
                "ID Keluhan", "Nama Responden", "Judul", "Keterangan", "Status", "Created At", "Updated At"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // tabel tidak bisa diedit langsung
            }
        };
        tableKeluhan = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tableKeluhan);

        // ComboBox status
        comboStatus = new JComboBox<>(new String[]{
                "Belum Ditangani",
                "Sedang Ditangani",
                "Selesai"
        });

        // Tombol update status
        btnUpdateStatus = new JButton("Update Status");

        // Panel bawah untuk kontrol status
        JPanel panelBottom = new JPanel();
        panelBottom.add(new JLabel("Ubah Status:"));
        panelBottom.add(comboStatus);
        panelBottom.add(btnUpdateStatus);

        // Layout frame
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        getContentPane().add(panelBottom, BorderLayout.SOUTH);

        // Event tombol update
        btnUpdateStatus.addActionListener(e -> {
            int selectedRow = tableKeluhan.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this,
                        "Pilih data keluhan terlebih dahulu.",
                        "Peringatan",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            String idKeluhan = (String) tableModel.getValueAt(selectedRow, 0);
            String statusBaru = (String) comboStatus.getSelectedItem();

            int konfirmasi = JOptionPane.showConfirmDialog(this,
                    "Yakin ingin mengubah status keluhan ini menjadi \"" + statusBaru + "\"?",
                    "Konfirmasi Update",
                    JOptionPane.YES_NO_OPTION);

            if (konfirmasi == JOptionPane.YES_OPTION) {
                keluhanController.updateStatusKeluhan(idKeluhan, statusBaru);
                loadDataKeluhan();
                JOptionPane.showMessageDialog(this,
                        "Status keluhan berhasil diperbarui.",
                        "Sukses",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }

    private void loadDataKeluhan() {
        List<Keluhan> listKeluhan = keluhanController.getSemuaKeluhan();

        tableModel.setRowCount(0);

        for (Keluhan k : listKeluhan) {
            tableModel.addRow(new Object[]{
                    k.getIdKeluhan(),
                    k.getNamaResponden(),
                    k.getJudulKeluhan(),
                    k.getKeteranganKeluhan(),
                    k.getStatusKeluhan(),
                    k.getCreatedAt(),
                    k.getUpdatedAt()
            });
        }
    }
}
