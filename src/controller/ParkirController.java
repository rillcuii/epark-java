package controller;

import model.Parkir;
import service.ParkirService;
import service.ParkirService.ParkirSatpamDto;

import java.util.List;

public class ParkirController {

    private ParkirService parkirService;

    public ParkirController(ParkirService parkirService) {
        this.parkirService = parkirService;
    }

    public List<Parkir> getRiwayatParkirByUser(String userId) {
        return parkirService.getRiwayatParkirByUser(userId);
    }

    public boolean parkirMasuk(String userId, String kendaraanId, String kodeUnik) {
        return parkirService.parkirMasuk(userId, kendaraanId, kodeUnik);
    }

    public boolean parkirKeluar(String userId, String kodeUnik) {
        return parkirService.parkirKeluar(userId, kodeUnik);
    }

    public boolean hasActiveParkir(String userId) {
        return parkirService.hasActiveParkir(userId);
    }

    public List<ParkirSatpamDto> getMahasiswaKeluarHariIni() {
        return parkirService.getMahasiswaKeluarHariIni();
    }

    public List<ParkirSatpamDto> getSemuaRiwayatParkir() {
        return parkirService.getSemuaRiwayatParkir();
    }

    public String getActiveParkirKendaraanId(String userId) {
        return parkirService.getActiveParkirKendaraanId(userId);
    }
}
