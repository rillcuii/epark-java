package controller;

import model.Kendaraan;
import service.KendaraanService;
import java.util.List;

public class KendaraanController {

    private KendaraanService kendaraanService;

    public KendaraanController(KendaraanService kendaraanService) {
        this.kendaraanService = kendaraanService;
    }

    public List<Kendaraan> getKendaraanByUser(String userId) {
        return kendaraanService.getKendaraanByUser(userId);
    }

    public void tambahKendaraan(String userId, String stnkId, String model, String urlPhoto) {
        kendaraanService.tambahKendaraan(userId, stnkId, model, urlPhoto);
    }

    public void updateKendaraan(String idKendaraan, String userId, String stnkId, String model, String urlPhoto) {
        kendaraanService.updateKendaraan(idKendaraan, userId, stnkId, model, urlPhoto);
    }

    public void hapusKendaraan(String idKendaraan) {
        kendaraanService.hapusKendaraan(idKendaraan);
    }
}
