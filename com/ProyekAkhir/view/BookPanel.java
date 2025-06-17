package com.ProyekAkhir.view;

import com.ProyekAkhir.exceptions.InputKosongException;
import com.ProyekAkhir.service.Perpustakaan;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class BookPanel extends JPanel {
    private Perpustakaan perpustakaan;
    private JTable bookTable;
    private DefaultTableModel tableModel;
    
    public BookPanel(Perpustakaan perpustakaan) {
        this.perpustakaan = perpustakaan;
        initUI();
    }
    
    private void initUI() {
        setLayout(new BorderLayout());
        
        // Table model
        String[] columns = {"Kode Buku", "Judul", "Pengarang", "Status"};
        tableModel = new DefaultTableModel(columns, 0);
        bookTable = new JTable(tableModel);
        refreshBookTable();
        
        // Buttons
        JButton addButton = new JButton("Tambah Buku");
        JButton deleteButton = new JButton("Hapus Buku");
        
        addButton.addActionListener(this::addBook);
        deleteButton.addActionListener(this::deleteBook);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        
        add(new JScrollPane(bookTable), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void refreshBookTable() {
        tableModel.setRowCount(0);
        perpustakaan.getListBuku().forEach(buku -> {
            Object[] row = {
                buku.getKodeBuku(),
                buku.getJudul(),
                String.join(", ", buku.getListPengarang()),
                buku.isStatusPeminjaman() ? "Dipinjam" : "Tersedia"
            };
            tableModel.addRow(row);
        });
    }
    
    private void addBook(ActionEvent e) {
        JDialog dialog = new JDialog();
        dialog.setTitle("Tambah Buku Baru");
        dialog.setLayout(new GridLayout(4, 2, 10, 10));
        
        JTextField codeField = new JTextField();
        JTextField titleField = new JTextField();
        JTextField authorField = new JTextField();
        
        dialog.add(new JLabel("Kode Buku:"));
        dialog.add(codeField);
        dialog.add(new JLabel("Judul:"));
        dialog.add(titleField);
        dialog.add(new JLabel("Pengarang (pisahkan dengan koma):"));
        dialog.add(authorField);
        
        JButton saveButton = new JButton("Simpan");
        JButton cancelButton = new JButton("Batal");
        
        saveButton.addActionListener(ev -> {
            try {
                String code = codeField.getText();
                String title = titleField.getText();
                String[] authors = authorField.getText().split(",");
                ArrayList<String> authorList = new ArrayList<>();
                for (String author : authors) {
                    authorList.add(author.trim());
                }

                if (code.isEmpty() || title.isEmpty() || authorList.isEmpty()) {
                    throw new InputKosongException("EXCEPTION InputKosongException: Semua field harus diisi!");
                }
                
                perpustakaan.tambahBuku(code, title, authorList);
                refreshBookTable();
                dialog.dispose();
                JOptionPane.showMessageDialog(this, "Buku berhasil ditambahkan!");
            } catch (InputKosongException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Input Kosong", JOptionPane.WARNING_MESSAGE);
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
    
    private void deleteBook(ActionEvent e) {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow >= 0) {
            String bookCode = (String) tableModel.getValueAt(selectedRow, 0);
            try {
                perpustakaan.hapusBuku(bookCode);
                refreshBookTable();
                JOptionPane.showMessageDialog(this, "Buku berhasil dihapus!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Pilih buku yang akan dihapus!", "Peringatan", JOptionPane.WARNING_MESSAGE);
        }
    }
}
