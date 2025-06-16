# ProyekAkhirPemrogramanLanjur

# Sistem Manajemen Perpustakaan

Proyek ini adalah sistem manajemen perpustakaan sederhana yang dibangun dengan Java. Sistem ini memungkinkan pengelolaan data pengguna, buku, peminjaman, pengembalian, dan denda.

## Fitur Utama

1. **Manajemen Pengguna**:
   - Tambah pengguna baru (dengan NIM, nama, dan prodi)
   - Hapus pengguna (jika tidak memiliki pinjaman aktif)

2. **Manajemen Buku**:
   - Tambah buku baru (dengan kode buku, judul, dan daftar pengarang)
   - Hapus buku (jika tidak sedang dipinjam)

3. **Peminjaman dan Pengembalian**:
   - Peminjaman buku oleh pengguna
   - Pengembalian buku (dengan perhitungan denda jika terlambat)
   - Pembayaran denda

## Struktur File Data

- `data_buku.txt`: Menyimpan data buku (kode, judul, pengarang, status peminjaman)
- `data_pengguna.txt`: Menyimpan data pengguna (NIM, nama, prodi)
- `data_peminjaman.txt`: Menyimpan data peminjaman (kode peminjaman, NIM, kode buku, tanggal)
- `data_denda.txt`: Menyimpan data denda (kode peminjaman, besar denda, status pembayaran)

## Struktur Kode

### Model
- `Buku`: Merepresentasikan entitas buku
- `Pengguna`: Merepresentasikan entitas pengguna (mahasiswa)
- `LogPeminjaman`: Merepresentasikan log peminjaman buku
- `LogDenda`: Merepresentasikan log denda

### Repository
- `FileManager`: Menangani operasi baca/tulis data ke/dari file

### Service
- `Perpustakaan`: Menyediakan layanan untuk operasi perpustakaan

### Exceptions
- Berbagai kelas exception kustom untuk penanganan kesalahan spesifik

## Cara Menjalankan

1. Pastikan Java Development Kit (JDK) terinstal
2. Kompilasi semua file Java:
   ```
   javac -d bin src/com/ProyekAkhir/*.java src/com/ProyekAkhir/Main/*.java src/com/ProyekAkhir/Model/*.java src/com/ProyekAkhir/repository/*.java src/com/ProyekAkhir/service/*.java src/com/ProyekAkhir/exceptions/*.java
   ```
3. Jalankan aplikasi:
   ```
   java -cp bin com.ProyekAkhir.Main.MainApp
   ```

## Validasi Data

### Pengguna
- NIM harus 15 digit
- Nama minimal 2 kata
- Prodi harus salah satu dari: TIF, SI, TEKKOM, TI, PTI

### Buku
- Kode buku harus 5 karakter
- Judul tidak boleh kosong
- Pengarang minimal 1 orang dengan nama minimal 2 kata

### Peminjaman
- Kode peminjaman harus 6 digit angka
- Buku tidak boleh dalam status dipinjam

## Contoh Penggunaan

### Menambahkan Pengguna
```java
Perpustakaan perpustakaan = new Perpustakaan();
perpustakaan.tambahPengguna(123456789012345L, "Nama Lengkap", "TIF");
```

### Menambahkan Buku
```java
ArrayList<String> pengarang = new ArrayList<>();
pengarang.add("Dr. Linda");
perpustakaan.tambahBuku("BK002", "Judul Buku", pengarang);
```

### Meminjam Buku
```java
perpustakaan.pinjamBuku(123456789012345L, "BK002");
```

### Mengembalikan Buku
```java
perpustakaan.kemalikanBuku(123456789012345L, "BK002");
```

## Penanganan Kesalahan

Sistem menyediakan berbagai exception kustom untuk menangani skenario kesalahan:
- `BookCodeNotFoundException`: Buku tidak ditemukan
- `BukuSudahDipinjamException`: Buku sedang dipinjam
- `DataAlreadyExistsExceptions`: Data sudah ada
- `NIMNotFoundException`: Pengguna tidak ditemukan
- Dan lainnya

## Kontributor

- Muhammad Raditya Arsyad
- Dafid Rosydan Hanif
- Valentinus Marvel
- Muhammad Fahmi Siqqiq Aqsha
- Muhammad Rifki
