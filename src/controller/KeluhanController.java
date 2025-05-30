package controller;

import model.Keluhan;
import service.KeluhanService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class KeluhanController {
    private KeluhanService keluhanService;

    public KeluhanController(KeluhanService keluhanService) {
        this.keluhanService = keluhanService;
    }

    // Tambah keluhan dengan status default "Belum Ditangani"
    public void tambahKeluhan(String usersId, String namaResponden, String judulKeluhan,
                              String keteranganKeluhan, String photoBuktiUrl) {
        String idKeluhan = UUID.randomUUID().toString();
        LocalDateTime now = LocalDateTime.now();

        Keluhan keluhan = new Keluhan(
                idKeluhan,
                usersId,
                namaResponden,
                judulKeluhan,
                keteranganKeluhan,
                photoBuktiUrl,
                "Belum Ditangani", // status default string sesuai DB
                now,
                now
        );

        keluhanService.insert(keluhan);
    }

    // Ambil semua keluhan berdasarkan userId
    public List<Keluhan> getKeluhanByUser(String usersId) {
        return keluhanService.getAllByUserId(usersId);
    }

    // Update status keluhan berdasarkan id dan status baru
    public void updateStatusKeluhan(String idKeluhan, String statusBaru) {
        // Bisa tambahkan validasi statusBaru disini jika mau
        keluhanService.updateStatusKeluhan(idKeluhan, statusBaru, LocalDateTime.now());
    }

    public List<Keluhan> getSemuaKeluhan() {
        return keluhanService.getAllKeluhan();
    }
}
