package com.ProyekAkhir.Model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LogPeminjaman {
    private String kodePeminjaman;
    private LocalDateTime tanggalPinjam;
    private LocalDateTime tanggalJatuhTempo;
    private LocalDateTime tanggalKembali;
    private String NIMPengguna;
    private String kodeBuku;
    
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public LogPeminjaman(String kodePeminjaman, String NIMPengguna, String kodeBuku) throws IllegalArgumentException {
        setKodePeminjaman(kodePeminjaman);
        this.tanggalPinjam = LocalDateTime.now();
        this.tanggalJatuhTempo = this.tanggalPinjam.plusWeeks(1);
        this.tanggalKembali = null;
        this.NIMPengguna = NIMPengguna;
        this.kodeBuku = kodeBuku;
    }
    public LogPeminjaman(String kodePeminjaman, String NIMPengguna, String kodeBuku, LocalDateTime tanggalPinjam,LocalDateTime tanggalJatuhTempo,LocalDateTime tanggalKembali) throws IllegalArgumentException {
        setKodePeminjaman(kodePeminjaman);
        this.tanggalPinjam = tanggalPinjam;
        this.tanggalJatuhTempo = tanggalJatuhTempo;
        this.tanggalKembali = tanggalKembali;
        this.NIMPengguna = NIMPengguna;
        this.kodeBuku = kodeBuku;
    }

    public void setKodePeminjaman(String kodePeminjaman) throws IllegalArgumentException {
        if (kodePeminjaman == null || kodePeminjaman.trim().isEmpty()) {
            throw new IllegalArgumentException("Kode peminjaman tidak boleh kosong!");
        }

        if (kodePeminjaman.contains(" ")) {
            throw new IllegalArgumentException("Kode peminjaman harus berupa satu kata (tanpa spasi)!");
        }

        if (kodePeminjaman.length() != 6) {
            throw new IllegalArgumentException("Kode peminjaman harus sepanjang 6 digit!");
        }

        for (char c : kodePeminjaman.toCharArray()) {
            if (!Character.isDigit(c)) {
                throw new IllegalArgumentException("Kode peminjaman harus terdiri dari 6 digit angka!");
            }
        }

        this.kodePeminjaman = kodePeminjaman;
    }

    public void setTanggalKembali() {
        this.tanggalKembali = LocalDateTime.now();
    }

    public void setNIMPengguna(String NIMPengguna) {
        this.NIMPengguna = NIMPengguna;
    }

    public void setKodeBuku(String kodeBuku) {
        this.kodeBuku = kodeBuku;
    }

    public String getKodePeminjaman() {
        return kodePeminjaman;
    }

    public LocalDateTime getTanggalPinjam() {
        return tanggalPinjam;
    }

    public LocalDateTime getTanggalJatuhTempo() {
        return tanggalJatuhTempo;
    }

    public LocalDateTime getTanggalKembali() {
        return tanggalKembali;
    }

    public String getNIMPengguna() {
        return NIMPengguna;
    }

    public String getKodeBuku() {
        return kodeBuku;
    }
    
    @Override
    public String toString() {
        return "Peminjaman{Kode='" + kodePeminjaman + "', NIM='" + NIMPengguna + "', KodeBuku='" + kodeBuku +
               "', Pinjam=" + (tanggalPinjam != null ? tanggalPinjam.format(DATE_TIME_FORMATTER) : "N/A") +
               ", JatuhTempo=" + (tanggalJatuhTempo != null ? tanggalJatuhTempo.format(DATE_TIME_FORMATTER) : "N/A") +
               ", Kembali=" + (tanggalKembali != null ? tanggalKembali.format(DATE_TIME_FORMATTER) : "Belum Kembali") + "}";
    }
    
    public String toFileString() {
        return kodePeminjaman + "," +
               NIMPengguna + "," +
               kodeBuku + "," +
               (tanggalPinjam != null ? tanggalPinjam.format(DATE_TIME_FORMATTER) : "") + "," +
               (tanggalJatuhTempo != null ? tanggalJatuhTempo.format(DATE_TIME_FORMATTER) : "") + "," +
               (tanggalKembali != null ? tanggalKembali.format(DATE_TIME_FORMATTER) : "");
    }
     
    public static LogPeminjaman fromFileString(String line) {
        if (line == null || line.trim().isEmpty()) {
            return null;
        }
        // Split dengan limit -1 agar field kosong di akhir tetap dihitung
        String[] parts = line.split(",", -1); 
        if (parts.length == 6) { // Harus ada 6 bagian
            try {
                String kodePeminjaman = parts[0].trim();
                String nimPengguna = parts[1].trim();
                String kodeBuku = parts[2].trim();
                LocalDateTime tglPinjam = parts[3].trim().isEmpty() ? null : LocalDateTime.parse(parts[3].trim(), DATE_TIME_FORMATTER);
                LocalDateTime tglJatuhTempo = parts[4].trim().isEmpty() ? null : LocalDateTime.parse(parts[4].trim(), DATE_TIME_FORMATTER);
                LocalDateTime tglKembali = parts[5].trim().isEmpty() ? null : LocalDateTime.parse(parts[5].trim(), DATE_TIME_FORMATTER);
                
                if (kodePeminjaman.isEmpty() || nimPengguna.isEmpty() || kodeBuku.isEmpty() || tglPinjam == null || tglJatuhTempo == null) {
                    System.err.println("Data Peminjaman tidak lengkap (field wajib kosong): " + line);
                    return null;
                }

                return new LogPeminjaman(kodePeminjaman, nimPengguna, kodeBuku, tglPinjam, tglJatuhTempo, tglKembali);
//            } catch (DateTimeParseException e) {
//                System.err.println("Error parsing tanggal untuk Peminjaman: " + line + " - " + e.getMessage());
//                return null;
            } catch (Exception e) {
                System.err.println("Error parsing data Peminjaman: " + line + " - " + e.getMessage());
                return null;
            }
        }
        System.err.println("Format data Peminjaman tidak sesuai (jumlah field salah): " + line + ", parts: " + parts.length);
        return null;
    }
}
