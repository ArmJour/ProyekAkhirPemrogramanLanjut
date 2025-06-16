package com.ProyekAkhir.Model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;


public class LogDenda extends LogPeminjaman {
    private int besarDenda;
    private boolean statusPembayaran;

    public LogDenda(String kodePeminjaman, LocalDateTime tanggalPinjam, LocalDateTime tanggalJatuhTempo, LocalDateTime tanggalKembali,
                    String nimPengguna, String kodeBuku, int besarDenda, boolean statusPembayaran) {
        super(kodePeminjaman, nimPengguna, kodeBuku, tanggalPinjam, tanggalJatuhTempo, tanggalKembali); // Correct parameters passed to superclass
        this.besarDenda = besarDenda;
        this.statusPembayaran = statusPembayaran;
    }

    public int getBesarDenda() {
        return besarDenda;
    }

    public void setBesarDenda(int besarDenda) {
        this.besarDenda = besarDenda;
    }

    public boolean isStatusPembayaran() {
        return statusPembayaran;
    }

    public void setStatusPembayaran(boolean statusPembayaran) {
        this.statusPembayaran = statusPembayaran;
    }

    public void hitungDenda() {
        try {
            if (getTanggalKembali() != null && getTanggalJatuhTempo() != null) {
                // Parse the dates using the formatter
                LocalDate tanggalJatuhTempo = getTanggalJatuhTempo().toLocalDate();
                LocalDate tanggalKembali = getTanggalKembali().toLocalDate();

                long diff = tanggalKembali.toEpochDay() - tanggalJatuhTempo.toEpochDay();

                if (diff > 0) {
                    this.besarDenda = (int) diff * 1000;
                } else {
                    this.besarDenda = 0;
                }
            } else {
                System.err.println("Tanggal kembali atau tanggal jatuh tempo tidak valid.");
                this.besarDenda = 0;
            }
        } catch (DateTimeParseException e) {
            System.err.println("Error parsing date: " + e.getMessage());
            this.besarDenda = 0;
        }
    }

    @Override
    public String toString() {
        return "LogDenda [kodePeminjaman=" + getKodePeminjaman() + ", nimPengguna=" + getNIMPengguna() +
                ", kodeBuku=" + getKodeBuku() + ", besarDenda=" + besarDenda + ", statusPembayaran=" + statusPembayaran + "]";
    }

    public String toFileString() {
        String parentString = super.toFileString();

        return parentString + "," +
            this.besarDenda + "," +
            this.statusPembayaran;
    }

    public static LogDenda fromFileString(String line) {
        if (line == null || line.trim().isEmpty()) {
            return null;
        }

        String[] parts = line.split(",", -1);
        if (parts.length == 8) {
            try {
                String kodePeminjaman = parts[0].trim();
                String nimPengguna = parts[1].trim();
                String kodeBuku = parts[2].trim();
                LocalDateTime tglPinjam = parts[3].trim().isEmpty() ? null : LocalDateTime.parse(parts[3].trim(), DATE_TIME_FORMATTER);
                LocalDateTime tglJatuhTempo = parts[4].trim().isEmpty() ? null : LocalDateTime.parse(parts[4].trim(), DATE_TIME_FORMATTER);
                LocalDateTime tglKembali = parts[5].trim().isEmpty() ? null : LocalDateTime.parse(parts[5].trim(), DATE_TIME_FORMATTER);

                int besarDenda = Integer.parseInt(parts[6].trim());
                boolean statusPembayaran = Boolean.parseBoolean(parts[7].trim());

                if (kodePeminjaman.isEmpty() || nimPengguna.isEmpty() || kodeBuku.isEmpty()) {
                    System.err.println("Data Denda tidak lengkap (field wajib kosong): " + line);
                    return null;
                }

                return new LogDenda(kodePeminjaman, tglPinjam, tglJatuhTempo, tglKembali, nimPengguna, kodeBuku, besarDenda, statusPembayaran);

            } catch (DateTimeParseException | NumberFormatException e) {
                System.err.println("Error parsing data Denda: " + line + " - " + e.getMessage());
                return null;
            }
        }
        System.err.println("Format data Denda tidak sesuai (jumlah field salah): " + line);
        return null;
    }
}