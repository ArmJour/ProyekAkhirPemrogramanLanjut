package com.ProyekAkhir.repository;

import com.ProyekAkhir.Model.Buku;
import com.ProyekAkhir.Model.LogDenda;
import com.ProyekAkhir.Model.LogPeminjaman;
import com.ProyekAkhir.Model.Pengguna;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FileManager {
    public final String filePathBuku;
    public final String filePathPengguna;
    public final String filePathPeminjaman;
    public final String filePathDenda;

    
    public FileManager() {
        String relativePath = ".." + File.separator + "data";
        
        Path dataFolderPath = Paths.get(relativePath);
        try {
            if (Files.notExists(dataFolderPath)) {
                Files.createDirectories(dataFolderPath);
                System.out.println("Folder data berhasil dibuat di: " + dataFolderPath.toString());
            }
        } catch (IOException e) {
            System.err.println("Gagal membuat folder data: " + e.getMessage());
        }

        String namaFileBuku = "data_buku.txt";
        String namaFilePengguna = "data_pengguna.txt";
        String namaFilePeminjaman = "data_peminjaman.txt";
        String namaFilePathDenda = "data_denda.txt";

        this.filePathBuku = dataFolderPath.resolve(namaFileBuku).toString();
        this.filePathPengguna = dataFolderPath.resolve(namaFilePengguna).toString();
        this.filePathPeminjaman = dataFolderPath.resolve(namaFilePeminjaman).toString();
        this.filePathDenda = dataFolderPath.resolve(namaFilePathDenda).toString();

        // Membuat file jika belum ada
        createFileIfNotExists(this.filePathBuku);
        createFileIfNotExists(this.filePathPengguna);
        createFileIfNotExists(this.filePathPeminjaman);
        createFileIfNotExists(this.filePathDenda);

        System.out.println("FileManager siap. Data akan disimpan di dalam folder: " + dataFolderPath.toString());   
    }
    
    private void createFileIfNotExists(String filePathString) {
        Path filePath = Paths.get(filePathString);
        try {
            if (Files.notExists(filePath)) {
                if (filePath.getParent() != null) {
                     Files.createDirectories(filePath.getParent()); // Buat direktori jika belum ada
                }
                Files.createFile(filePath);
                System.out.println("File berhasil dibuat: " + filePathString);
            }
        } catch (IOException e) {
            System.err.println("Gagal membuat file " + filePathString + ": " + e.getMessage());
        }
    }
    
    public void menulisFile(String namaFile, String primaryKey, String dataBaru) {
        Path filePath = Paths.get(namaFile);
        List<String> lines;
        boolean updated = false;

        try {
            if (Files.notExists(filePath)) {
                createFileIfNotExists(namaFile); // Pastikan file ada
            }
            lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
            List<String> newLines = new ArrayList<>();

            for (String line : lines) {
                if (line.startsWith(primaryKey + ",")) { // Cek berdasarkan primary key
                    newLines.add(dataBaru); // Timpa baris
                    updated = true;
                } else {
                    newLines.add(line);
                }
            }

            if (!updated) {
                newLines.add(dataBaru); // Tambah baris baru jika tidak ada yang diperbarui
            }

            Files.write(filePath, newLines, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            System.err.println("Error saat menulis ke file " + namaFile + ": " + e.getMessage());
        }
    }
    
    public void hapusBaris(String namaFile, String primaryKey) {
        Path filePath = Paths.get(namaFile);
        List<String> lines;

        try {
            if (Files.notExists(filePath)) {
                System.err.println("File tidak ditemukan untuk dihapus: " + namaFile);
                return;
            }
            lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
            List<String> newLines = lines.stream()
                                         .filter(line -> !line.startsWith(primaryKey + ","))
                                         .collect(Collectors.toList());

            if (newLines.size() < lines.size()) { // Hanya tulis jika ada perubahan
                 Files.write(filePath, newLines, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            } else {
                System.out.println("Data dengan primary key '" + primaryKey + "' tidak ditemukan di file " + namaFile);
            }
        } catch (IOException e) {
            System.err.println("Error saat menghapus baris dari file " + namaFile + ": " + e.getMessage());
        }
    }
    
    public List<Pengguna> memuatPengguna() {
        List<Pengguna> penggunaList = new ArrayList<>();
        Path filePath = Paths.get(filePathPengguna);

        if (Files.notExists(filePath)) {
            System.err.println("File pengguna tidak ditemukan: " + filePathPengguna);
            return penggunaList; // Kembalikan list kosong
        }

        try (BufferedReader reader = Files.newBufferedReader(filePath, StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue; // Lewati baris kosong
                Pengguna pengguna = Pengguna.fromFileString(line);
                if (pengguna != null) {
                    penggunaList.add(pengguna); 
                }
            }
        } catch (IOException e) {
            System.err.println("Error saat memuat data pengguna: " + e.getMessage());
        }
        return penggunaList;
    }
   
    public List<Buku> memuatBuku() {
        List<Buku> bukuList = new ArrayList<>();
        Path filePath = Paths.get(filePathBuku);

        if (Files.notExists(filePath)) {
            System.err.println("File buku tidak ditemukan: " + filePathBuku);
            return bukuList;
        }
        
        try (BufferedReader reader = Files.newBufferedReader(filePath, StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                 if (line.trim().isEmpty()) continue;
                Buku buku = Buku.fromFileString(line);
                if (buku != null) {
                    bukuList.add(buku);
                }
            }
        } catch (IOException e) {
            System.err.println("Error saat memuat data buku: " + e.getMessage());
        }
        return bukuList;
    }

    public List<LogPeminjaman> memuatPeminjaman() {
        List<LogPeminjaman> LogpeminjamanList = new ArrayList<>();
        Path filePath = Paths.get(filePathPeminjaman);

        if (Files.notExists(filePath)) {
            System.err.println("File peminjaman tidak ditemukan: " + filePathPeminjaman);
            return LogpeminjamanList;
        }

        try (BufferedReader reader = Files.newBufferedReader(filePath, StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                 if (line.trim().isEmpty()) continue;
                LogPeminjaman Logpeminjaman = LogPeminjaman.fromFileString(line);
                if (Logpeminjaman != null) {
                   LogpeminjamanList.add(Logpeminjaman);
                }
            }
        } catch (IOException e) {
            System.err.println("Error saat memuat data peminjaman: " + e.getMessage());
        }
        return LogpeminjamanList;
    }

    public List<LogDenda> memuatDenda() {
        List<LogDenda> LogDendaList = new ArrayList<>();
        Path filePath = Paths.get(filePathDenda);

        if (Files.notExists(filePath)) {
            System.err.println("File denda tidak ditemukan: " + filePathDenda);
            return LogDendaList;
        }

        try (BufferedReader reader = Files.newBufferedReader(filePath, StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                LogDenda logDenda = LogDenda.fromFileString(line);
                if (logDenda != null) {
                   LogDendaList.add(logDenda);
                }
            }
        } catch (IOException e) {
            System.err.println("Error saat memuat data peminjaman: " + e.getMessage());
        }
        return LogDendaList;
    }
}
