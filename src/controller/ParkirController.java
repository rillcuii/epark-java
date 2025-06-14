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

    public boolean parkirMasuk(String userId, String kendaraanId, String kodeUnik, String tokenUnik) {
        return parkirService.parkirMasuk(userId, kendaraanId, kodeUnik, tokenUnik);
    }

    public boolean parkirKeluar(String userId, String kodeUnik, String tokenKeluar) {
        return parkirService.parkirKeluar(userId, kodeUnik, tokenKeluar);
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

    public List<ParkirSatpamDto> getMahasiswaSedangParkirHariIni() {
        return parkirService.getMahasiswaSedangParkirHariIni();
    }

    public boolean tokenUnikIsValid(String userId, String tokenUnik){
        return parkirService.tokenUnikIsValid(userId, tokenUnik);
    }

    // Tambahan untuk menghitung sisa slot parkir
    public int getSisaTempatParkir() {
        return parkirService.getSisaTempatParkir();
    }
    public String getTokenUnik(String userId) {
        return parkirService.getTokenUnik(userId);
    }
}
