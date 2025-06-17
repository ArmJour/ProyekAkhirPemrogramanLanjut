package com.ProyekAkhir.service;

import com.ProyekAkhir.Model.Buku;
import com.ProyekAkhir.Model.LogDenda;
import com.ProyekAkhir.Model.LogPeminjaman;
import com.ProyekAkhir.Model.Pengguna;
import com.ProyekAkhir.exceptions.*;
import com.ProyekAkhir.repository.FileManager;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class Perpustakaan {
    private List<Pengguna> listPengguna;
    private List<Buku> listBuku;
    private List<LogPeminjaman> listPeminjaman;
    private List<LogDenda> listDenda = new ArrayList<>();
    private final FileManager fileManager;
    private int peminjamanCounter;
    
    public Perpustakaan() {
        this.fileManager = new FileManager();
        this.listPengguna = new ArrayList<>(fileManager.memuatPengguna());
        this.listBuku = new ArrayList<>(fileManager.memuatBuku());
        this.listPeminjaman = new ArrayList<>(fileManager.memuatPeminjaman());
        this.listDenda = new ArrayList<>(fileManager.memuatDenda());
        this.initializePeminjamanCounter();
    }

    // =================================================================
    // USER MANAGEMENT
    // =================================================================

    /**
     * Adds a new user to the system.
     * @param nim The user's NIM (must be unique).
     * @param nama The user's full name.
     * @param prodi The user's study program.
     * @throws DataAlreadyExistsExceptions if a user with the same NIM already exists.
     */

    public void tambahPengguna(Long nim, String nama, String prodi) throws DataAlreadyExistsExceptions {
        if(listPengguna.stream().anyMatch(p -> p.getNIM() == nim)) {
            throw new DataAlreadyExistsExceptions("EXCEPTION DataAlreadyExistsExceptions: Pengguna dengan NIM '" + nim + "' sudah terdaftar.");
        }
        try {
            Pengguna penggunaBaru = new Pengguna(nim, nama, prodi);
            listPengguna.add(penggunaBaru);
            fileManager.menulisFile(fileManager.filePathPengguna, String.valueOf(nim), penggunaBaru.toFileString());
            System.out.println("SUCCESS: Pengguna baru berhasil ditambahkan: " + nama);
        } catch (IllegalArgumentException e) {
            System.err.println("ERROR: Gagal menambahkan pengguna - " + e.getMessage());
        }
    }

    /**
     * Deletes a user from the system.
     * @param nim The NIM of the user to delete.
     * @throws NIMNotFoundException if no user with the given NIM is found.
     * @throws DeleteWhenRunningException if the user has active loans.
     */

    public void hapusPengguna(Long nim) throws NIMNotFoundException, DeleteWhenRunningException {
        for (LogPeminjaman log : listPeminjaman) {
            
            if (log.getNIMPengguna().equals(String.valueOf(nim)) && log.getTanggalKembali() == null) {
                throw new DeleteWhenRunningException("EXCEPTION DeleteWhenRunningException: Tidak dapat menghapus pengguna '" + nim + "' karena masih memiliki pinjaman aktif.");
            }

        }

        Pengguna penggunaToDelete = null;
        for (Pengguna p : listPengguna) {
            
            if (p.getNIM() == nim) {

                penggunaToDelete = p;
                break;
            }

        }

        if (penggunaToDelete != null) {

            listPengguna.remove(penggunaToDelete);
            fileManager.hapusBaris(fileManager.filePathPengguna, String.valueOf(nim));
            System.out.println("SUCCESS: Pengguna dengan NIM '" + nim + "' berhasil dihapus.");

        } else {

            throw new NIMNotFoundException("EXCEPTION NIMNotFoundException: Pengguna dengan NIM '" + nim + "' tidak ditemukan.");

        }

    }

    // =================================================================
    // BOOK MANAGEMENT 
    // =================================================================

    /**
     * Adds a new book to the library.
     * @param kodeBuku The book's code (must be unique).
     * @param judul The book's title.
     * @param listPengarang A list of authors for the book.
     * @throws DataAlreadyExistsExceptions if a book with the same code already exists.
     */

    public void tambahBuku(String kodeBuku, String judul, ArrayList<String> listPengarang) throws DataAlreadyExistsExceptions {

        for (Buku b : listBuku) {

            if(b.getKodeBuku().equalsIgnoreCase(kodeBuku)) {
                throw new DataAlreadyExistsExceptions("EXCEPTION DataAlreadyExistsExceptions: Buku dengan kode '" + kodeBuku + "' sudah ada.");
            }

        }

        try {
            Buku bukuBaru = new Buku(kodeBuku, judul, listPengarang);
            listBuku.add(bukuBaru);
            fileManager.menulisFile(fileManager.filePathBuku, kodeBuku, bukuBaru.toFileString());
            System.out.println("SUCCESS: Buku baru berhasil ditambahkan: " + judul);
        } catch (IllegalArgumentException e) {
            System.err.println("ERROR: Gagal menambahkan buku - " + e.getMessage());
        }

    }


    /**
     * Deletes a book from the library.
     * @param kodeBuku The code of the book to delete.
     * @throws BookCodeNotFoundException if no book with the given code is found.
     * @throws DeleteWhenRunningException if the book is currently loaned out.
     */

    public void hapusBuku(String kodeBuku) throws BookCodeNotFoundException, DeleteWhenRunningException {

        Buku bukuToDelete = null;
        for (Buku b : listBuku) {
            if (b.getKodeBuku().equalsIgnoreCase(kodeBuku)) {
                bukuToDelete = b;
                break;
            }
        }

        if (bukuToDelete != null) {
            if (bukuToDelete.isStatusPeminjaman()) {
                throw new DeleteWhenRunningException("EXCEPTION DeleteWhenRunningException: Tidak dapat menghapus buku '" + kodeBuku + "' karena sedang dipinjam.");
            }
            listBuku.remove(bukuToDelete);
            fileManager.hapusBaris(fileManager.filePathBuku, kodeBuku);
            System.out.println("SUCCESS: Buku dengan kode '" + kodeBuku + "' berhasil dihapus.");
        } else {
            throw new BookCodeNotFoundException("EXCEPTION BookCodeNotFoundException: Buku dengan kode '" + kodeBuku + "' tidak ditemukan.");
        }

    }

    // =================================================================
    // Manajemen Peminjaman, Pengembalian, dan Denda
    // =================================================================

    /**
     * Initializes the peminjamanCounter based on existing logs.
     * This method scans through the list of peminjaman logs to find the highest
     * kodePeminjaman and sets the counter accordingly.
     */
    private void initializePeminjamanCounter() {
        int maxId = 0;
        for (LogPeminjaman log : this.listPeminjaman) {
            try {
                int currentId = Integer.parseInt(log.getKodePeminjaman());
                if (currentId > maxId) {
                    maxId = currentId;
                }
            } catch (NumberFormatException e) {
                System.err.println("Peringatan: Kode peminjaman tidak valid dan diabaikan: " + log.getKodePeminjaman());
            }
        }
        this.peminjamanCounter = maxId;
    }

    /**
     * Borrows a book for a user.
     * @param nim The user's NIM.
     * @param kodeBuku The book's code.
     * @throws NIMNotFoundException if no user with the given NIM is found.
     * @throws BookCodeNotFoundException if no book with the given code is found.
     * @throws BukuSudahDipinjamException if the book is already loaned out.
     */
    public void pinjamBuku(long nim, String kodeBuku) throws NIMNotFoundException, BookCodeNotFoundException, BukuSudahDipinjamException {
        
        Pengguna pengguna = null;
        for (Pengguna p : listPengguna) {
            
            if(p.getNIM() == nim) {
                pengguna = p;
                break;
            }

        }

        if(pengguna == null) {
            throw new NIMNotFoundException("EXCEPTION NIMNotFoundException: Pengguna dengan NIM '" + nim + "' tidak ditemukan.");
        }

        Buku buku = null;
        for (Buku b : listBuku) {
            
            if(b.getKodeBuku().equalsIgnoreCase(kodeBuku)) {
                buku = b;
                break;
            }

        }

        if(buku == null) {
            throw new BookCodeNotFoundException("EXCEPTION BookCodeNotFoundException: Buku dengan kode '" + kodeBuku + "' tidak ditemukan.");
        }

        if(buku.isStatusPeminjaman()) {
            throw new BukuSudahDipinjamException("EXCEPTION BukuSudahDipinjamException: Buku '" + buku.getJudul() + "' sedang tidak tersedia atau sudah dipinjam.");
        }

        this.peminjamanCounter++;
        String KodePeminjaman = String.valueOf(this.peminjamanCounter);
        buku.setStatusPeminjaman(true);
        LogPeminjaman logBaru = new LogPeminjaman(KodePeminjaman, String.valueOf(nim), kodeBuku, LocalDateTime.now(), LocalDateTime.now().plusWeeks(2), null);    
        listPeminjaman.add(logBaru);

        fileManager.menulisFile(fileManager.filePathBuku, buku.getKodeBuku(), buku.toFileString());
        fileManager.menulisFile(fileManager.filePathPeminjaman, logBaru.getKodePeminjaman(), logBaru.toFileString());
        System.out.println("SUCCESS: Pengguna '" + pengguna.getNama() + "' berhasil meminjam buku '" + buku.getJudul() + "'. Kode Peminjaman: " + KodePeminjaman);
    }

    /**
     * Returns a book that was borrowed by a user.
     * @param nim The user's NIM.
     * @param kodeBuku The book's code.
     * @throws NIMNotFoundException if no user with the given NIM is found.
     * @throws BookCodeNotFoundException if no book with the given code is found.
     * @throws ReturnBookNotBorrowed if the book was not borrowed by the user.
     */

    public void kemalikanBuku(long nim, String kodeBuku) throws NIMNotFoundException, BookCodeNotFoundException, ReturnBookNotBorrowed {
        
        Pengguna pengguna = null; 
        for (Pengguna p : listPengguna) if (p.getNIM() == nim) pengguna = p;
        if (pengguna == null) throw new NIMNotFoundException("EXCEPTION NIMNotFoundException: Pengguna dengan NIM '" + nim + "' tidak ditemukan.");

        Buku buku = null; 
        for (Buku b : listBuku) if (b.getKodeBuku().equalsIgnoreCase(kodeBuku)) buku = b;
        if (buku == null) throw new BookCodeNotFoundException("EXCEPTION BookCodeNotFoundException: Buku dengan kode '" + kodeBuku + "' tidak ditemukan.");

        LogPeminjaman logToUpdate = null; 
        for (LogPeminjaman log : listPeminjaman) {
            if (log.getNIMPengguna().equals(String.valueOf(nim)) && log.getKodeBuku().equalsIgnoreCase(kodeBuku) && log.getTanggalKembali() == null) {
                logToUpdate = log;
                break;
            }
        }
        if (logToUpdate == null) throw new ReturnBookNotBorrowed("EXCEPTION ReturnBookNotBorrowed: Data peminjaman tidak ditemukan.");

        if (logToUpdate.getTanggalKembali().toLocalDate().isAfter(logToUpdate.getTanggalJatuhTempo().toLocalDate())) {
            LogDenda denda = new LogDenda(logToUpdate.getKodePeminjaman(), logToUpdate.getTanggalPinjam(), logToUpdate.getTanggalJatuhTempo(), logToUpdate.getTanggalKembali(), logToUpdate.getNIMPengguna(), logToUpdate.getKodeBuku(), 0, false);
            denda.hitungDenda();
            listDenda.add(denda);
            System.out.println("WARNING: Pengembalian terlambat! Denda yang dikenakan: Rp" + denda.getBesarDenda());

            fileManager.menulisFile(fileManager.filePathDenda, denda.getKodePeminjaman(), denda.toFileString());
        }

        fileManager.menulisFile(fileManager.filePathBuku, buku.getKodeBuku(), buku.toFileString());
        fileManager.menulisFile(fileManager.filePathPeminjaman, logToUpdate.getKodePeminjaman(), logToUpdate.toFileString());
        System.out.println("SUCCESS: Buku '" + buku.getJudul() + "' berhasil dikembalikan oleh '" + pengguna.getNama() + "'.");
    }

    /**
     * Pays the fine for a specific loan.
     * @param kodePeminjaman The code of the loan for which the fine is being paid.
     * @throws Exception if the fine for the given loan is not found or has already been paid.
     */
    
    public void bayarDenda(String kodePeminjaman) throws Exception {
        LogDenda dendaToPay = null;
        for (LogDenda denda : listDenda) {
            if (denda.getKodePeminjaman().equals(kodePeminjaman) && !denda.isStatusPembayaran()) {
                dendaToPay = denda;
                break;
            }
        }

        if (dendaToPay != null) {
            dendaToPay.setStatusPembayaran(true);
            System.out.println("SUCCESS: Pembayaran denda sebesar Rp" + dendaToPay.getBesarDenda() + " untuk kode peminjaman '" + kodePeminjaman + "' berhasil.");

            fileManager.menulisFile(fileManager.filePathDenda, dendaToPay.getKodePeminjaman(), dendaToPay.toFileString());
        } else {
            throw new Exception("Denda untuk kode peminjaman '" + kodePeminjaman + "' tidak ditemukan atau sudah lunas.");
        }
    }
    
}
