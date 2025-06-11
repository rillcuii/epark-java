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

    public SatpamParkirKeluarView(ParkirController parkirController) {
        this.parkirController = parkirController;
        setTitle("Data Mahasiswa Keluar Parkir Hari Ini - Satpam");
        setSize(700, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initComponents();
        loadData();
    }

    private void initComponents() {
        String[] kolom = {"Nama Mahasiswa", "Nomor Polisi", "Model Kendaraan", "Waktu Masuk", "Waktu Keluar"};
        tableModel = new DefaultTableModel(kolom, 0);
        tabel = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(tabel);
        add(scrollPane, BorderLayout.CENTER);

        JPanel panelBawah = new JPanel();
        JButton btnKembali = new JButton("Kembali");
        panelBawah.add(btnKembali);

        add(panelBawah, BorderLayout.SOUTH);

        btnKembali.addActionListener(e -> {
            dispose();
        });
    }

    private void loadData() {
        List<ParkirSatpamDto> list = parkirController.getMahasiswaKeluarHariIni();
        tableModel.setRowCount(0);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

        for (ParkirSatpamDto dto : list) {
            String waktuMasukFormatted = dto.getWaktuMasuk().format(dtf);
            String waktuKeluarFormatted = dto.getWaktuKeluar().format(dtf);

            Object[] row = {
                    dto.getNamaUser(),
                    dto.getstnkID(),
                    dto.getModelKendaraan(),
                    waktuMasukFormatted,
                    waktuKeluarFormatted
            };
            tableModel.addRow(row);
        }
    }
}
