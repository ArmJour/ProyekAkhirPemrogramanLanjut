package com.ProyekAkhir.view;

import com.ProyekAkhir.exceptions.InputKosongException;
import com.ProyekAkhir.service.Perpustakaan;
import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class UserPanel extends JPanel {
    private Perpustakaan perpustakaan;
    private JTable userTable;
    private DefaultTableModel tableModel;
    
    public UserPanel(Perpustakaan perpustakaan) {
        this.perpustakaan = perpustakaan;
        initUI();
    }
    
    private void initUI() {
        setLayout(new BorderLayout());
        
        // Table model
        String[] columns = {"NIM", "Nama", "Prodi"};
        tableModel = new DefaultTableModel(columns, 0);
        userTable = new JTable(tableModel);
        refreshUserTable();
        
        // Buttons
        JButton addButton = new JButton("Tambah Pengguna");
        JButton deleteButton = new JButton("Hapus Pengguna");
        
        addButton.addActionListener(this::addUser);
        deleteButton.addActionListener(this::deleteUser);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        
        add(new JScrollPane(userTable), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void refreshUserTable() {
        tableModel.setRowCount(0);
        perpustakaan.getListPengguna().forEach(pengguna -> {
            Object[] row = {
                pengguna.getNIM(),
                pengguna.getNama(),
                pengguna.getProdi()
            };
            tableModel.addRow(row);
        });
    }
    
    private void addUser(ActionEvent e) {
        JDialog dialog = new JDialog();
        dialog.setTitle("Tambah Pengguna Baru");
        dialog.setLayout(new GridLayout(4, 2, 10, 10));
        
        JTextField nimField = new JTextField();
        JTextField nameField = new JTextField();
        JComboBox<String> prodiCombo = new JComboBox<>(new String[]{"TIF", "SI", "TEKKOM", "TI", "PTI"});
        
        dialog.add(new JLabel("NIM:"));
        dialog.add(nimField);
        dialog.add(new JLabel("Nama:"));
        dialog.add(nameField);
        dialog.add(new JLabel("Prodi:"));
        dialog.add(prodiCombo);
        
        JButton saveButton = new JButton("Simpan");
        JButton cancelButton = new JButton("Batal");
        
        saveButton.addActionListener(ev -> {
            try {
                long nim = Long.parseLong(nimField.getText());
                String name = nameField.getText();
                String prodi = (String) prodiCombo.getSelectedItem();

                if (String.valueOf(nim).isEmpty() || name.isEmpty() || prodi == null) {
                    throw new InputKosongException("EXCEPTION InputKosongException: Semua field harus diisi!");
                }
                
                perpustakaan.tambahPengguna(nim, name, prodi);
                refreshUserTable();
                dialog.dispose();
                JOptionPane.showMessageDialog(this, "Pengguna berhasil ditambahkan!");
            } catch (InputKosongException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "NIM harus berupa angka!", "Error", JOptionPane.ERROR_MESSAGE);
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
    
    private void deleteUser(ActionEvent e) {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow >= 0) {
            long nim = (Long) tableModel.getValueAt(selectedRow, 0);
            try {
                perpustakaan.hapusPengguna(nim);
                refreshUserTable();
                JOptionPane.showMessageDialog(this, "Pengguna berhasil dihapus!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Pilih pengguna yang akan dihapus!", "Peringatan", JOptionPane.WARNING_MESSAGE);
        }
    }
}
