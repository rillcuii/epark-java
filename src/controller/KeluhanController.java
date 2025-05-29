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

    public void tambahKeluhan(String usersId, String namaResponden, String judulKeluhan, String keteranganKeluhan, String photoBuktiUrl) {
        String idKeluhan = UUID.randomUUID().toString();
        LocalDateTime now = LocalDateTime.now();
        Keluhan keluhan = new Keluhan(idKeluhan, usersId, namaResponden, judulKeluhan, keteranganKeluhan, photoBuktiUrl, "Belum Ditangani", now, now);
        keluhanService.insert(keluhan);
    }

    public List<Keluhan> getKeluhanByUser(String usersId) {
        return keluhanService.getAllByUserId(usersId);
    }
}
