package view;

import controller.ParkirController;
import service.ParkirService.ParkirSatpamDto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class SatpamParkirKeluarView extends JFrame {

    private ParkirController parkirController;
    private JTable tabel;
    private DefaultTableModel tableModel;
    private JComboBox<String> filterComboBox;
    private JLabel lblSisaParkir;

    public SatpamParkirKeluarView(ParkirController parkirController) {
        this.parkirController = parkirController;
        setTitle("Data Mahasiswa Parkir - Satpam");
        setSize(900, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initComponents();
        loadData("Sedang Parkir Hari Ini"); // Default load filter ini
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // Panel atas
        JPanel panelAtas = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblFilter = new JLabel("Filter: ");
        filterComboBox = new JComboBox<>(new String[]{
                "Riwayat Parkir",
                "Sedang Parkir Hari Ini",
                "Keluar Masuk Parkir Hari Ini"
        });
        lblSisaParkir = new JLabel(); // Untuk menampilkan sisa parkir

        panelAtas.add(lblFilter);
        panelAtas.add(filterComboBox);
        panelAtas.add(lblSisaParkir);
        add(panelAtas, BorderLayout.NORTH);

        // Tabel
        String[] kolom = {"Nama Mahasiswa", "Nomor Polisi", "Model Kendaraan", "Waktu Masuk", "Waktu Keluar"};
        tableModel = new DefaultTableModel(kolom, 0);
        tabel = new JTable(tableModel);
        add(new JScrollPane(tabel), BorderLayout.CENTER);

        // Panel bawah
        JPanel panelBawah = new JPanel();
        JButton btnKembali = new JButton("Kembali");
        panelBawah.add(btnKembali);
        add(panelBawah, BorderLayout.SOUTH);

        // Listener
        filterComboBox.addActionListener(e -> {
            String selected = (String) filterComboBox.getSelectedItem();
            loadData(selected);
        });

        btnKembali.addActionListener(e -> dispose());
    }

    private void loadData(String filter) {
        List<ParkirSatpamDto> list = switch (filter) {
            case "Riwayat Parkir" -> parkirController.getSemuaRiwayatParkir();
            case "Sedang Parkir Hari Ini" -> parkirController.getMahasiswaSedangParkirHariIni();
            case "Keluar Masuk Parkir Hari Ini" -> parkirController.getMahasiswaKeluarHariIni();
            default -> List.of();
        };

        tableModel.setRowCount(0); // Reset tabel
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

        for (ParkirSatpamDto dto : list) {
            String masuk = dto.getWaktuMasuk() != null ? dto.getWaktuMasuk().format(dtf) : "";
            String keluar = dto.getWaktuKeluar() != null ? dto.getWaktuKeluar().format(dtf) : "";
            tableModel.addRow(new Object[]{
                    dto.getNamaUser(),
                    dto.getstnkID(),
                    dto.getModelKendaraan(),
                    masuk,
                    keluar
            });
        }

        // Hanya tampilkan sisa parkir jika filter adalah "Sedang Parkir Hari Ini"
        if (filter.equals("Sedang Parkir Hari Ini")) {
            int sisa = parkirController.getSisaTempatParkir();
            lblSisaParkir.setText("   Sisa Tempat Parkir: " + sisa);
        } else {
            lblSisaParkir.setText("");
        }
    }
}
