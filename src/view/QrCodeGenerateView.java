package view;

import com.google.zxing.qrcode.QRCodeWriter;
import controller.QrCodeController;
import com.google.zxing.*;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import javax.swing.*;
import java.awt.*;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class QrCodeGenerateView extends JFrame {

    private QrCodeController qrCodeController;

    private JButton btnGenerate;
    private JLabel lblQrCode;
    private JLabel lblStatus;

    public QrCodeGenerateView(QrCodeController qrCodeController) {
        this.qrCodeController = qrCodeController;

        setTitle("Generate QR Code Parkir");
        setSize(350, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10,10));

        btnGenerate = new JButton("Generate QR Code");
        add(btnGenerate, BorderLayout.NORTH);

        lblQrCode = new JLabel("", SwingConstants.CENTER);
        lblQrCode.setPreferredSize(new Dimension(300, 300));
        lblQrCode.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        add(lblQrCode, BorderLayout.CENTER);

        lblStatus = new JLabel(" ", SwingConstants.CENTER);
        lblStatus.setForeground(Color.BLUE);
        add(lblStatus, BorderLayout.SOUTH);

        btnGenerate.addActionListener(e -> generateQrCode());
    }

    private void generateQrCode() {
        String kodeUnik = qrCodeController.generateAndSaveQrCode();

        if (kodeUnik == null) {
            lblStatus.setText("Gagal generate QR Code");
            lblQrCode.setIcon(null);
            return;
        }

        lblStatus.setText("Kode Unik: " + kodeUnik);

        try {
            BufferedImage qrImage = createQrCodeImage(kodeUnik, 300, 300);
            lblQrCode.setIcon(new ImageIcon(qrImage));
        } catch (WriterException | IOException ex) {
            ex.printStackTrace();
            lblStatus.setText("Gagal buat gambar QR Code");
            lblQrCode.setIcon(null);
        }
    }

    private BufferedImage createQrCodeImage(String text, int width, int height) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }
}
