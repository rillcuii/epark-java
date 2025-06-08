package model;

import java.time.LocalDateTime;

public class Parkir {
    private String idParkir;
    private String userId;
    private LocalDateTime waktuMasuk;
    private LocalDateTime waktuKeluar;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Getter dan Setter
    public String getIdParkir() {
        return idParkir;
    }

    public void setIdParkir(String idParkir) {
        this.idParkir = idParkir;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public LocalDateTime getWaktuMasuk() {
        return waktuMasuk;
    }

    public void setWaktuMasuk(LocalDateTime waktuMasuk) {
        this.waktuMasuk = waktuMasuk;
    }

    public LocalDateTime getWaktuKeluar() {
        return waktuKeluar;
    }

    public void setWaktuKeluar(LocalDateTime waktuKeluar) {
        this.waktuKeluar = waktuKeluar;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
