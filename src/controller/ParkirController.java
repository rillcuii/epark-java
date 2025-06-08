package controller;

import model.Parkir;
import service.ParkirService;

import java.util.List;

public class ParkirController {

    private ParkirService parkirService;

    public ParkirController(ParkirService parkirService) {
        this.parkirService = parkirService;
    }

    // Ambil riwayat parkir mahasiswa
    public List<Parkir> getRiwayatParkirByUser(String userId) {
        return parkirService.getRiwayatParkirByUser(userId);
    }

    // Proses parkir masuk dengan kode unik
    public boolean parkirMasuk(String userId, String kodeUnik) {
        return parkirService.parkirMasuk(userId, kodeUnik);
    }

    // Proses parkir keluar dengan kode unik
    public boolean parkirKeluar(String userId, String kodeUnik) {
        return parkirService.parkirKeluar(userId, kodeUnik);
    }

    // Cek apakah user punya parkir aktif
    public boolean hasActiveParkir(String userId) {
        return parkirService.hasActiveParkir(userId);
    }
}
