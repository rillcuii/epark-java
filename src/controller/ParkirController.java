package controller;

import model.Parkir;
import service.ParkirService;

import java.util.List;

public class ParkirController {

    private ParkirService parkirService;

    public ParkirController(ParkirService parkirService) {
        this.parkirService = parkirService;
    }

    public List<Parkir> getRiwayatParkirByUser(String userId) {
        return parkirService.getRiwayatParkirByUser(userId);
    }
}
