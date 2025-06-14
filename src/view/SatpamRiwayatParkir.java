package view;

import controller.ParkirController;
import service.ParkirService.ParkirSatpamDto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class SatpamRiwayatParkir extends JFrame {

    private ParkirController parkirController;
    private JTable table;
    private DefaultTableModel tableModel;

    public SatpamRiwayatParkir(ParkirController parkirController) {
        this.parkirController = parkirController;
        initComponents();
        loadData();
    }

    private void initComponents() {
        setTitle("Riwayat Parkir Semua Mahasiswa");
        setSize(800, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        String[] columns = {"Nama Mahasiswa", "Nomor Polisi", "Model Kendaraan", "Waktu Masuk", "Waktu Keluar"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        JPanel panelButton = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnKembali = new JButton("Kembali");
        btnKembali.addActionListener(e -> {
            dispose();
        });
        panelButton.add(btnKembali);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        getContentPane().add(panelButton, BorderLayout.SOUTH);
    }

    private void loadData() {
        List<ParkirSatpamDto> riwayat = parkirController.getSemuaRiwayatParkir();
        tableModel.setRowCount(0);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

        for (ParkirSatpamDto p : riwayat) {
            String waktuMasukFormatted = p.getWaktuMasuk().format(dtf);
            String waktuKeluarFormatted = p.getWaktuKeluar().format(dtf);

            Object[] rowData = {
                    p.getNamaUser(),
                    p.getstnkID(),
                    p.getModelKendaraan(),
                    waktuMasukFormatted,
                    waktuKeluarFormatted
            };
            tableModel.addRow(rowData);
        }
    }
}
