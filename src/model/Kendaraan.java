package mvc.model;

import java.time.LocalDateTime;

public class Kendaraan {
    private String idKendaraan;
    private String stnkId;
    private String modelKendaraan;
    private String urlPhotoKendaraan;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Kendaraan(String idKendaraan, String stnkId, String modelKendaraan, String urlPhotoKendaraan, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.idKendaraan = idKendaraan;
        this.stnkId = stnkId;
        this.modelKendaraan = modelKendaraan;
        this.urlPhotoKendaraan = urlPhotoKendaraan;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getIdKendaraan() {
        return idKendaraan;
    }

    public void setIdKendaraan(String idKendaraan) {
        this.idKendaraan = idKendaraan;
    }

    public String getStnkId() {
        return stnkId;
    }

    public void setStnkId(String stnkId) {
        this.stnkId = stnkId;
    }

    public String getModelKendaraan() {
        return modelKendaraan;
    }

    public void setModelKendaraan(String modelKendaraan) {
        this.modelKendaraan = modelKendaraan;
    }

    public String getUrlPhotoKendaraan() {
        return urlPhotoKendaraan;
    }

    public void setUrlPhotoKendaraan(String urlPhotoKendaraan) {
        this.urlPhotoKendaraan = urlPhotoKendaraan;
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
