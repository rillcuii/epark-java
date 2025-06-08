package model;

public class QrCodeSession {
    private int idQr;
    private String kodeUnik;
    private String waktuGenerate;
    private String expiredAt;

    public QrCodeSession() {}

    public QrCodeSession(int idQr, String kodeUnik, String waktuGenerate, String expiredAt) {
        this.idQr = idQr;
        this.kodeUnik = kodeUnik;
        this.waktuGenerate = waktuGenerate;
        this.expiredAt = expiredAt;
    }

    public int getIdQr() {
        return idQr;
    }

    public void setIdQr(int idQr) {
        this.idQr = idQr;
    }

    public String getKodeUnik() {
        return kodeUnik;
    }

    public void setKodeUnik(String kodeUnik) {
        this.kodeUnik = kodeUnik;
    }

    public String getWaktuGenerate() {
        return waktuGenerate;
    }

    public void setWaktuGenerate(String waktuGenerate) {
        this.waktuGenerate = waktuGenerate;
    }

    public String getExpiredAt() {
        return expiredAt;
    }

    public void setExpiredAt(String expiredAt) {
        this.expiredAt = expiredAt;
    }
}
