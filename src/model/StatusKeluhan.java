package model;

public enum StatusKeluhan {
    BELUM_DITANGANI,
    SEDANG_DITANGANI,
    SELESAI;

    public static StatusKeluhan fromString(String status) {
        return StatusKeluhan.valueOf(status.toUpperCase());
    }
}
