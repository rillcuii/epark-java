package view;

import controller.SatpamController;
import model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class SatpamCrudView extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField namaField, usernameField, passwordField;
    private JButton tambahButton, editButton, hapusButton, kembaliButton;

    private SatpamController controller;
    private List<User> satpamList;

    public SatpamCrudView() {
        controller = new SatpamController();
        setTitle("Kelola Data Satpam");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(new Object[]{"No", "Nama", "Username", "Role"}, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        JPanel formPanel = new JPanel(new GridLayout(4, 2));
        namaField = new JTextField();
        usernameField = new JTextField();
        passwordField = new JTextField();

        formPanel.add(new JLabel("Nama:"));
        formPanel.add(namaField);
        formPanel.add(new JLabel("Username:"));
        formPanel.add(usernameField);
        formPanel.add(new JLabel("Password:"));
        formPanel.add(passwordField);

        JPanel buttonPanel = new JPanel();
        tambahButton = new JButton("Tambah");
        editButton = new JButton("Edit");
        hapusButton = new JButton("Hapus");
        kembaliButton = new JButton("Kembali");
        buttonPanel.add(tambahButton);
        buttonPanel.add(editButton);
        buttonPanel.add(hapusButton);
        buttonPanel.add(kembaliButton);

        add(scrollPane, BorderLayout.CENTER);
        add(formPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);

        loadData();

        tambahButton.setEnabled(true);
        editButton.setEnabled(false);
        hapusButton.setEnabled(false);

        tambahButton.addActionListener(e -> {
            String nama = namaField.getText().trim();
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();
            if (!nama.isEmpty() && !username.isEmpty() && !password.isEmpty()) {
                controller.tambahSatpam(nama, username, password);
                clearForm();
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "Semua field harus diisi!");
            }
        });

        editButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                User selectedUser = satpamList.get(selectedRow);
                String id = selectedUser.getIdUser();
                String nama = namaField.getText().trim();
                String username = usernameField.getText().trim();
                String password = passwordField.getText().trim();
                if (!nama.isEmpty() && !username.isEmpty() && !password.isEmpty()) {
                    User updatedUser = new User(id, nama, username, password, "Satpam");
                    controller.updateSatpam(updatedUser);
                    clearForm();
                    loadData();
                } else {
                    JOptionPane.showMessageDialog(this, "Semua field harus diisi!");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Pilih data yang ingin diedit!");
            }
        });

        hapusButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                User selectedUser = satpamList.get(selectedRow);
                String id = selectedUser.getIdUser();
                int confirm = JOptionPane.showConfirmDialog(this,
                        "Yakin ingin menghapus data ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    controller.hapusSatpam(id);
                    clearForm();
                    loadData();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Pilih data yang ingin dihapus!");
            }
        });

        table.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = table.getSelectedRow();
            boolean adaDipilih = selectedRow >= 0 && selectedRow < satpamList.size();

            if (adaDipilih) {
                User user = satpamList.get(selectedRow);
                namaField.setText(user.getNamaUser());
                usernameField.setText(user.getUsername());
                passwordField.setText(user.getPassword());
            } else {
                clearForm();
            }

            tambahButton.setEnabled(!adaDipilih);
            editButton.setEnabled(adaDipilih);
            hapusButton.setEnabled(adaDipilih);
        });

        kembaliButton.addActionListener(e -> {
            this.dispose();
            new AdminDashboardView(null).setVisible(true);
        });

        setVisible(true);
    }

    private void loadData() {
        tableModel.setRowCount(0);
        satpamList = controller.getSemuaSatpam();
        int no = 1;
        for (User u : satpamList) {
            tableModel.addRow(new Object[]{no++, u.getNamaUser(), u.getUsername(), u.getRole()});
        }
    }

    private void clearForm() {
        namaField.setText("");
        usernameField.setText("");
        passwordField.setText("");
        table.clearSelection();
    }
}
