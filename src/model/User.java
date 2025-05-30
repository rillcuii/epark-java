package model;

import java.time.LocalDateTime;

public class User {
    private String idUser, namaUser, username, password, role;
    private LocalDateTime createdAt, updatedAt;

    public User(String idUser, String namaUser, String username, String password, String role) {
        this.idUser = idUser;
        this.namaUser = namaUser;
        this.username = username;
        this.password = password;
        this.role = role;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public User(String idUser, String namaUser, String username, String password, String role, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.idUser = idUser;
        this.namaUser = namaUser;
        this.username = username;
        this.password = password;
        this.role = role;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getIdUser() { return idUser; }
    public String getNamaUser() { return namaUser; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
