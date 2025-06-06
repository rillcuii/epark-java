package model;

import java.time.LocalDateTime;

public class Keluhan {
    private String idKeluhan;
    private String userId;
    private String namaResponden;
    private String judulKeluhan;
    private String keteranganKeluhan;
    private String photoBuktiUrl;
    private String statusKeluhan;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Keluhan(String idKeluhan, String userId, String namaResponden, String judulKeluhan, String keteranganKeluhan,
                   String photoBuktiUrl, String statusKeluhan, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.idKeluhan = idKeluhan;
        this.userId = userId;
        this.namaResponden = namaResponden;
        this.judulKeluhan = judulKeluhan;
        this.keteranganKeluhan = keteranganKeluhan;
        this.photoBuktiUrl = photoBuktiUrl;
        this.statusKeluhan = statusKeluhan;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getIdKeluhan() { return idKeluhan; }
    public void setIdKeluhan(String idKeluhan) { this.idKeluhan = idKeluhan; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getNamaResponden() { return namaResponden; }
    public void setNamaResponden(String namaResponden) { this.namaResponden = namaResponden; }

    public String getJudulKeluhan() { return judulKeluhan; }
    public void setJudulKeluhan(String judulKeluhan) { this.judulKeluhan = judulKeluhan; }

    public String getKeteranganKeluhan() { return keteranganKeluhan; }
    public void setKeteranganKeluhan(String keteranganKeluhan) { this.keteranganKeluhan = keteranganKeluhan; }

    public String getPhotoBuktiUrl() { return photoBuktiUrl; }
    public void setPhotoBuktiUrl(String photoBuktiUrl) { this.photoBuktiUrl = photoBuktiUrl; }

    public String getStatusKeluhan() { return statusKeluhan; }
    public void setStatusKeluhan(String statusKeluhan) { this.statusKeluhan = statusKeluhan; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
