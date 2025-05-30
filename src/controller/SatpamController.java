package controller;

import model.User;
import service.UserService;

import java.util.List;

public class SatpamController {
    private UserService userService = new UserService();

    public void tambahSatpam(String nama, String username, String password) {
        String id = userService.generateId();
        User satpam = new User(id, nama, username, password, "Satpam");
        userService.insertUser(satpam);
    }

    public void updateSatpam(User satpam) {
        userService.updateUser(satpam);
    }

    public void hapusSatpam(String idUser) {
        userService.deleteUser(idUser);
    }

    public List<User> getSemuaSatpam() {
        return userService.getAllSatpam();
    }
}
