-- init.sql

CREATE TABLE IF NOT EXISTS users (
  id_user TEXT PRIMARY KEY,
  kendaraan_id TEXT UNIQUE,
  nama_user TEXT,
  nim_mahasiswa TEXT UNIQUE,
  username TEXT UNIQUE,
  password TEXT,
  role TEXT CHECK(role IN ('Admin', 'Mahasiswa', 'Satpam')),
  created_at TEXT,
  updated_at TEXT
);

CREATE TABLE IF NOT EXISTS kendaraan (
  id_kendaraan TEXT PRIMARY KEY,
  stnk_id TEXT UNIQUE,
  model_kendaraan TEXT,
  url_photo_kendaraan TEXT,
  created_at TEXT,
  updated_at TEXT,
  FOREIGN KEY(stnk_id) REFERENCES stnk(id_stnk)
);

CREATE TABLE IF NOT EXISTS stnk (
  id_stnk TEXT PRIMARY KEY,
  nomor_stnk TEXT UNIQUE,
  nama_pemilik TEXT,
  plat_motor TEXT UNIQUE,
  warna_motor TEXT,
  created_at TEXT,
  updated_at TEXT
);

CREATE TABLE IF NOT EXISTS parkir (
  id_parkir TEXT PRIMARY KEY,
  users_id TEXT UNIQUE,
  waktu_masuk TEXT,
  waktu_keluar TEXT,
  created_at TEXT,
  updated_at TEXT,
  FOREIGN KEY(users_id) REFERENCES users(id_user)
);

CREATE TABLE IF NOT EXISTS keluhan (
  id_keluhan TEXT PRIMARY KEY,
  users_id TEXT UNIQUE,
  nama_responden TEXT,
  judul_keluhan TEXT,
  keterangan_keluhan TEXT,
  photo_bukti_url TEXT,
  status_keluhan TEXT CHECK(status_keluhan IN ('Belum Ditangani', 'Sedang Ditangani', 'Selesai')),
  created_at TEXT,
  updated_at TEXT,
  FOREIGN KEY(users_id) REFERENCES users(id_user)
);
