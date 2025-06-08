package model;

import java.time.LocalDateTime;

public class Kendaraan {
    private String idKendaraan;
    private String userId;
    private String stnkId;
    private String modelKendaraan;
    private String urlPhotoKendaraan;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Kendaraan(String idKendaraan, String userId, String stnkId, String modelKendaraan, String urlPhotoKendaraan, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.idKendaraan = idKendaraan;
        this.userId = userId;
        this.stnkId = stnkId;
        this.modelKendaraan = modelKendaraan;
        this.urlPhotoKendaraan = urlPhotoKendaraan;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getIdKendaraan() { return idKendaraan; }
    public String getUserId() { return userId; }
    public String getStnkId() { return stnkId; }
    public String getModelKendaraan() { return modelKendaraan; }
    public String getUrlPhotoKendaraan() { return urlPhotoKendaraan; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    @Override
    public String toString() {
        return this.getStnkId();
    }

}
