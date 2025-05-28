package model;

public class User {
    private String idUser, namaUser, username, password, role;

    public User(String idUser, String namaUser, String username, String password, String role) {
        this.idUser = idUser;
        this.namaUser = namaUser;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public String getIdUser() { return idUser; }
    public String getNamaUser() { return namaUser; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
}
