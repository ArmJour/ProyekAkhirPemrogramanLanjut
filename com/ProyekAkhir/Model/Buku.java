package com.ProyekAkhir.Model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Buku {
    private String judul;
    private String kodeBuku;
    private ArrayList<String> listPengarang;
    private boolean statusPeminjaman;

    public Buku(String kodeBuku, String judul, ArrayList<String> listPengarang) throws IllegalArgumentException {
        setJudul(judul);
        setKodeBuku(kodeBuku);
        setListPengarang(listPengarang);
        this.statusPeminjaman = false;
    }
        public Buku(String kodeBuku, String judul, ArrayList<String> listPengarang, boolean statusPeminjaman) throws IllegalArgumentException {
        setJudul(judul);
        setKodeBuku(kodeBuku);
        setListPengarang(listPengarang);
        this.statusPeminjaman = false;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public void setKodeBuku(String kodeBuku) throws IllegalArgumentException {
        if (kodeBuku == null || kodeBuku.trim().isEmpty()) {
            throw new IllegalArgumentException("Kode buku tidak boleh kosong!");
        }

        if (kodeBuku.contains(" ")) {
            throw new IllegalArgumentException("Kode buku harus berupa satu kata!");
        }

        if (kodeBuku.length() != 5) {
            throw new IllegalArgumentException("Kode buku harus sepanjang 5 karakter!");
        }

        this.kodeBuku = kodeBuku;
    }

    public void setListPengarang(ArrayList<String> listPengarang) throws IllegalArgumentException {
        if (listPengarang == null || listPengarang.isEmpty()) {
            throw new IllegalArgumentException("List pengarang tidak boleh kosong!");
        }

        for (String pengarang : listPengarang) {
            if (pengarang == null || pengarang.trim().isEmpty()) {
                throw new IllegalArgumentException("Nama pengarang tidak boleh kosong!");
            }

            String[] words = pengarang.trim().split("\\s+");
            if (words.length < 2) {
                throw new IllegalArgumentException("Nama pengarang '" + pengarang + "' harus terdiri dari setidaknya 2 kata!");
            }
        }

        this.listPengarang = new ArrayList<>(listPengarang);
    }

    public void setStatusPeminjaman(boolean statusPeminjaman) {
        this.statusPeminjaman = statusPeminjaman;
    }

    public String getJudul() {
        return judul;
    }

    public String getKodeBuku() {
        return kodeBuku;
    }

    public ArrayList<String> getListPengarang() {
        return new ArrayList<>(listPengarang);
    }

    public boolean isStatusPeminjaman() {
        return statusPeminjaman;
    }
    
    @Override
    public String toString() {
        return "Buku{Kode='" + kodeBuku + "', Judul='" + judul + "', Pengarang=" + listPengarang + "', Pengarang=" + statusPeminjaman + "}";
    }
    
    public String toFileString() {
        String pengarangStr = listPengarang.stream().collect(Collectors.joining(";"));
        return kodeBuku + "," + judul + "," + pengarangStr + "," + statusPeminjaman;
    }
    
    public static Buku fromFileString(String line) {
        if (line == null || line.trim().isEmpty()) {
            return null;
        }
        String[] parts = line.split(",", 4);
        if (parts.length == 4) {
            ArrayList<String> pengarang = new ArrayList<>();
            if (parts[2] != null && !parts[2].trim().isEmpty()) {
                pengarang.addAll(Arrays.asList(parts[2].trim().split(";")));
            }
            try {
                return new Buku(parts[0].trim(), parts[1].trim(), pengarang, Boolean.parseBoolean(parts[3].trim()));
            } catch (Exception e) {
                 System.err.println("Error parsing data Buku: " + line + " - " + e.getMessage());
                return null;
            }
        }
        System.err.println("Format data Buku tidak sesuai: " + line);
        return null;
    }
}
