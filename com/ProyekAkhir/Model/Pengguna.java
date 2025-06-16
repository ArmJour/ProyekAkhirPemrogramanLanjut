package com.ProyekAkhir.Model;


public class Pengguna {
    private long NIM;
    private String nama;
    private String prodi;

    public Pengguna(long NIM, String nama, String prodi) throws IllegalArgumentException {
        setNIM(NIM);
        setNama(nama);
        setProdi(prodi);
    }

    public void setNIM(long NIM) throws IllegalArgumentException {
        String nimStr = String.valueOf(NIM);
        if (nimStr.length() != 15) {
            throw new IllegalArgumentException("NIM harus 15 digit!");
        }
        this.NIM = NIM;
    }

    public void setNama(String nama) throws IllegalArgumentException {
        if (nama == null || nama.trim().isEmpty()) {
            throw new IllegalArgumentException("Nama tidak boleh kosong!");
        }

        String[] words = nama.trim().split("\\s+");
        if (words.length < 2) {
            throw new IllegalArgumentException("Nama harus terdiri dari setidaknya 2 kata!");
        }
        this.nama = nama;
    }

    public void setProdi(String prodi) throws IllegalArgumentException {
        if (prodi == null || 
            (!prodi.equals("TIF") && !prodi.equals("SI") && 
             !prodi.equals("TEKKOM") && !prodi.equals("TI") && 
             !prodi.equals("PTI"))) {
            throw new IllegalArgumentException("Prodi harus salah satu dari: TIF, SI, TEKKOM, TI, PTI");
        }
        this.prodi = prodi;
    }

    public long getNIM() {
        return NIM;
    }

    public String getNama() {
        return nama;
    }

    public String getProdi() {
        return prodi;
    }
    
    @Override
    public String toString() {
        return "Pengguna{NIM=" + NIM + ", Nama='" + nama + "', Prodi='" + prodi + "'}";
    }
    
    public String toFileString() {
        return NIM + "," + nama + "," + prodi;
    }
    
    public static Pengguna fromFileString(String line) {
        if (line == null || line.trim().isEmpty()) {
            return null;
        }
        String[] parts = line.split(",", 3);
        if (parts.length == 3) {
            try {
                return new Pengguna(Long.parseLong(parts[0].trim()), parts[1].trim(), parts[2].trim());
            } catch (NumberFormatException e) {
                System.err.println("Error parsing NIM untuk Pengguna: " + parts[0] + " - " + e.getMessage());
                return null;
            }
        }
        System.err.println("Format data Pengguna tidak sesuai: " + line);
        return null;
    }
}
