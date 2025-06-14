package controller;

import service.QrCodeService;

import java.time.LocalDateTime;

public class QrCodeController {

    private final QrCodeService qrCodeService;

    public QrCodeController(QrCodeService qrCodeService) {
        this.qrCodeService = qrCodeService;
    }

    public String generateAndSaveQrCode() {
        String kodeUnik = qrCodeService.generateUniqueKodeUnik();
        LocalDateTime expiredAt = LocalDateTime.now().plusMinutes(5);
        boolean sukses = qrCodeService.saveQrCodeSession(kodeUnik, expiredAt);
        if (sukses) return kodeUnik;
        else return null;
    }

}
