package view;

import controller.ParkirController;
import model.Parkir;
import model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class RiwayatParkirView extends JFrame {

    private User user;
    private ParkirController parkirController;

    private JTable table;
    private DefaultTableModel tableModel;
    private JLabel lblInfo;
    private JButton btnKembali;
    private JScrollPane scrollPane;

    public RiwayatParkirView(User user, ParkirController controller) {
        this.user = user;
        this.parkirController = controller;

        setTitle("Riwayat Parkir - Mahasiswa: " + user.getNamaUser());
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initComponents();
        loadRiwayatParkir();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        lblInfo = new JLabel("", SwingConstants.CENTER);
        lblInfo.setFont(new Font("Arial", Font.BOLD, 16));
        add(lblInfo, BorderLayout.NORTH);  // Letakkan di atas supaya tidak bentrok dengan tabel

        tableModel = new DefaultTableModel(new Object[]{"ID Parkir", "Waktu Masuk", "Waktu Keluar"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);

        scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Sembunyikan tabel dan scrollPane awalnya, tampilkan jika ada data
        table.setVisible(false);
        scrollPane.setVisible(false);

        btnKembali = new JButton("Kembali ke Dashboard");
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnPanel.add(btnKembali);
        add(btnPanel, BorderLayout.SOUTH);

        btnKembali.addActionListener(e -> {
            dispose();
            new MahasiswaDashboardView(user).setVisible(true);
        });
    }

    private void loadRiwayatParkir() {
        List<Parkir> list = parkirController.getRiwayatParkirByUser(user.getIdUser());

        if (list.isEmpty()) {
            lblInfo.setText("Riwayat parkir belum ada.");
            lblInfo.setVisible(true);
            table.setVisible(false);
            scrollPane.setVisible(false);
        } else {
            lblInfo.setVisible(false);
            table.setVisible(true);
            scrollPane.setVisible(true);

            tableModel.setRowCount(0);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

            for (Parkir p : list) {
                String masuk = p.getWaktuMasuk() != null ? p.getWaktuMasuk().format(formatter) : "-";
                String keluar = p.getWaktuKeluar() != null ? p.getWaktuKeluar().format(formatter) : "-";

                tableModel.addRow(new Object[]{
                        p.getIdParkir(),
                        masuk,
                        keluar
                });
            }
        }
    }
}
