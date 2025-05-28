package controller;

import mvc.model.Kendaraan;
import service.KendaraanService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class KendaraanController {

    private KendaraanService kendaraanService;

    public KendaraanController(KendaraanService kendaraanService) {
        this.kendaraanService = kendaraanService;
    }

    // Tambah kendaraan baru
    public void tambahKendaraan(String stnkId, String modelKendaraan, String urlPhoto) {
        String idKendaraan = UUID.randomUUID().toString();
        LocalDateTime now = LocalDateTime.now();
        Kendaraan kendaraan = new Kendaraan(idKendaraan, stnkId, modelKendaraan, urlPhoto, now, now);
        kendaraanService.insert(kendaraan);
    }

    // Update kendaraan
    public void updateKendaraan(String idKendaraan, String stnkId, String modelKendaraan, String urlPhoto) {
        Kendaraan kendaraan = kendaraanService.getById(idKendaraan);
        if (kendaraan != null) {
            kendaraan.setStnkId(stnkId);
            kendaraan.setModelKendaraan(modelKendaraan);
            kendaraan.setUrlPhotoKendaraan(urlPhoto);
            kendaraan.setUpdatedAt(LocalDateTime.now());
            kendaraanService.update(kendaraan);
        }
    }

    // Hapus kendaraan
    public void hapusKendaraan(String idKendaraan) {
        kendaraanService.delete(idKendaraan);
    }

    // Ambil semua data kendaraan
    public List<Kendaraan> getSemuaKendaraan() {
        return kendaraanService.getAll();
    }

    // Ambil kendaraan berdasarkan id
    public Kendaraan getKendaraanById(String idKendaraan) {
        return kendaraanService.getById(idKendaraan);
    }
}
