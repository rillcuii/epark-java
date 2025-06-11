package view;

import controller.KeluhanController;
import model.Keluhan;
import model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class KeluhanAdminView extends JFrame {
    private KeluhanController keluhanController;
    private User admin;

    private JTable tableKeluhan;
    private DefaultTableModel tableModel;
    private JComboBox<String> comboStatus;
    private JButton btnUpdateStatus;
    private JButton btnKembali;

    public KeluhanAdminView(KeluhanController keluhanController, User admin) {
        this.keluhanController = keluhanController;
        this.admin = admin;
        initComponents();
        loadDataKeluhan();
    }

    private void initComponents() {
        setTitle("Admin Kelola Keluhan");
        setSize(900, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        tableModel = new DefaultTableModel(new String[]{
                "ID", "Nomor", "Nama Responden", "Judul", "Keterangan", "Status", "Created At", "Updated At"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableKeluhan = new JTable(tableModel);

        tableKeluhan.getColumnModel().getColumn(0).setMinWidth(0);
        tableKeluhan.getColumnModel().getColumn(0).setMaxWidth(0);
        tableKeluhan.getColumnModel().getColumn(0).setWidth(0);

        JScrollPane scrollPane = new JScrollPane(tableKeluhan);

        comboStatus = new JComboBox<>(new String[]{
                "Belum Ditangani",
                "Sedang Ditangani",
                "Selesai"
        });

        btnUpdateStatus = new JButton("Update Status");
        btnKembali = new JButton("Kembali");

        JPanel panelBottom = new JPanel();
        panelBottom.add(new JLabel("Ubah Status:"));
        panelBottom.add(comboStatus);
        panelBottom.add(btnUpdateStatus);
        panelBottom.add(btnKembali);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        getContentPane().add(panelBottom, BorderLayout.SOUTH);

        btnUpdateStatus.addActionListener(e -> {
            int selectedRow = tableKeluhan.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this,
                        "Pilih data keluhan terlebih dahulu.",
                        "Peringatan",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            String idKeluhan = tableModel.getValueAt(selectedRow, 0).toString();
            String statusSaatIni = tableModel.getValueAt(selectedRow, 5).toString();
            String statusBaru = (String) comboStatus.getSelectedItem();

            if (statusSaatIni.equals("Selesai")) {
                JOptionPane.showMessageDialog(this,
                        "Status 'Selesai' tidak bisa diubah lagi.",
                        "Tidak Diizinkan",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (statusBaru.equals(statusSaatIni)) {
                JOptionPane.showMessageDialog(this,
                        "Status baru sama dengan status saat ini.",
                        "Tidak Ada Perubahan",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }

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

        btnKembali.addActionListener(e -> {
            new AdminDashboardView(admin).setVisible(true);
            dispose();
        });
    }

    private void loadDataKeluhan() {
        List<Keluhan> listKeluhan = keluhanController.getSemuaKeluhan();
        tableModel.setRowCount(0);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        int no = 1;

        for (Keluhan k : listKeluhan) {
            String createdAtFormatted = k.getCreatedAt() != null ? k.getCreatedAt().format(formatter) : "-";
            String updatedAtFormatted = k.getUpdatedAt() != null ? k.getUpdatedAt().format(formatter) : "-";

            tableModel.addRow(new Object[]{
                    k.getIdKeluhan(),
                    no++,
                    k.getNamaResponden(),
                    k.getJudulKeluhan(),
                    k.getKeteranganKeluhan(),
                    k.getStatusKeluhan(),
                    createdAtFormatted,
                    updatedAtFormatted
            });
        }
    }
}
