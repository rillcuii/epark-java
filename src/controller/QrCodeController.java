package controller;

import service.QrCodeService;

import java.time.LocalDateTime;

public class QrCodeController {

    private final QrCodeService qrCodeService;

    public QrCodeController(QrCodeService qrCodeService) {
        this.qrCodeService = qrCodeService;
    }

    // Generate kode unik 6 digit, simpan dengan expired 5 menit
    public String generateAndSaveQrCode() {
        String kodeUnik = qrCodeService.generateUniqueKodeUnik();
        LocalDateTime expiredAt = LocalDateTime.now().plusMinutes(5);
        boolean sukses = qrCodeService.saveQrCodeSession(kodeUnik, expiredAt);
        if (sukses) return kodeUnik;
        else return null;
    }

    public boolean cekKodeValid(String kodeUnik) {
        return qrCodeService.isKodeUnikValid(kodeUnik);
    }
}
