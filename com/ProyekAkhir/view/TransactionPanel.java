package com.ProyekAkhir.view;

import com.ProyekAkhir.service.Perpustakaan;
import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class TransactionPanel extends JPanel {
    private Perpustakaan perpustakaan;
    private JTable transactionTable;
    private DefaultTableModel tableModel;
    
    public TransactionPanel(Perpustakaan perpustakaan) {
        this.perpustakaan = perpustakaan;
        initUI();
    }
    
    private void initUI() {
        setLayout(new BorderLayout());
        
        // Table model
        String[] columns = {"Kode Peminjaman", "NIM", "Kode Buku", "Tanggal Pinjam", "Jatuh Tempo", "Tanggal Kembali"};
        tableModel = new DefaultTableModel(columns, 0);
        transactionTable = new JTable(tableModel);
        refreshTransactionTable();
        
        // Buttons
        JButton borrowButton = new JButton("Pinjam Buku");
        JButton returnButton = new JButton("Kembalikan Buku");
        
        borrowButton.addActionListener(this::borrowBook);
        returnButton.addActionListener(this::returnBook);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(borrowButton);
        buttonPanel.add(returnButton);
        
        add(new JScrollPane(transactionTable), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void refreshTransactionTable() {
        tableModel.setRowCount(0);
        perpustakaan.getListPeminjaman().forEach(peminjaman -> {
            Object[] row = {
                peminjaman.getKodePeminjaman(),
                peminjaman.getNIMPengguna(),
                peminjaman.getKodeBuku(),
                peminjaman.getTanggalPinjam(),
                peminjaman.getTanggalJatuhTempo(),
                peminjaman.getTanggalKembali() != null ? peminjaman.getTanggalKembali() : "Masih Dipinjam"
            };
            tableModel.addRow(row);
        });
    }
    
    private void borrowBook(ActionEvent e) {
        JDialog dialog = new JDialog();
        dialog.setTitle("Pinjam Buku");
        dialog.setLayout(new GridLayout(3, 2, 10, 10));
        
        JComboBox<Long> nimCombo = new JComboBox<>();
        perpustakaan.getListPengguna().forEach(p -> nimCombo.addItem(p.getNIM()));
        
        JComboBox<String> bookCombo = new JComboBox<>();
        perpustakaan.getListBuku().stream()
            .filter(b -> !b.isStatusPeminjaman())
            .forEach(b -> bookCombo.addItem(b.getKodeBuku() + " - " + b.getJudul()));
        
        dialog.add(new JLabel("NIM Pengguna:"));
        dialog.add(nimCombo);
        dialog.add(new JLabel("Buku:"));
        dialog.add(bookCombo);
        
        JButton saveButton = new JButton("Pinjam");
        JButton cancelButton = new JButton("Batal");
        
        saveButton.addActionListener(ev -> {
            try {
                long nim = (Long) nimCombo.getSelectedItem();
                String bookCode = ((String) bookCombo.getSelectedItem()).split(" - ")[0];
                
                perpustakaan.pinjamBuku(nim, bookCode);
                refreshTransactionTable();
                dialog.dispose();
                JOptionPane.showMessageDialog(this, "Buku berhasil dipinjam!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        cancelButton.addActionListener(ev -> dialog.dispose());
        
        dialog.add(saveButton);
        dialog.add(cancelButton);
        
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
    
    private void returnBook(ActionEvent e) {
        int selectedRow = transactionTable.getSelectedRow();
        if (selectedRow >= 0) {
            String transactionCode = (String) tableModel.getValueAt(selectedRow, 0);
            try {
                perpustakaan.kembalikanBuku(
                    Long.parseLong((String) tableModel.getValueAt(selectedRow, 1)),
                    (String) tableModel.getValueAt(selectedRow, 2)
                );
                refreshTransactionTable();
                JOptionPane.showMessageDialog(this, "Buku berhasil dikembalikan!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Pilih transaksi yang akan dikembalikan!", "Peringatan", JOptionPane.WARNING_MESSAGE);
        }
    }
}