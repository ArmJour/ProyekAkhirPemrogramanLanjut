package com.ProyekAkhir.view;

import com.ProyekAkhir.service.Perpustakaan;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class FinePanel extends JPanel {
    private Perpustakaan perpustakaan;
    private JTable fineTable;
    private DefaultTableModel tableModel;
    
    public FinePanel(Perpustakaan perpustakaan) {
        this.perpustakaan = perpustakaan;
        initUI();
    }
    
    private void initUI() {
        setLayout(new BorderLayout());
        
        // Table model
        String[] columns = {"Kode Peminjaman", "NIM", "Kode Buku", "Besar Denda", "Status"};
        tableModel = new DefaultTableModel(columns, 0);
        fineTable = new JTable(tableModel);
        refreshFineTable();
        
        // Button
        JButton payButton = new JButton("Bayar Denda");
        payButton.addActionListener(this::payFine);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(payButton);
        
        add(new JScrollPane(fineTable), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                refreshFineTable();
            }
        });
    }
    
    private void refreshFineTable() {
        tableModel.setRowCount(0);
        perpustakaan.getListDenda().forEach(denda -> {
            Object[] row = {
                denda.getKodePeminjaman(),
                denda.getNIMPengguna(),
                denda.getKodeBuku(),
                "Rp" + denda.getBesarDenda(),
                denda.isStatusPembayaran() ? "Lunas" : "Belum Lunas"
            };
            tableModel.addRow(row);
        });
    }
    
    private void payFine(ActionEvent e) {
        int selectedRow = fineTable.getSelectedRow();
        if (selectedRow >= 0) {
            String fineCode = (String) tableModel.getValueAt(selectedRow, 0);
            try {
                perpustakaan.bayarDenda(fineCode);
                refreshFineTable();
                JOptionPane.showMessageDialog(this, "Denda berhasil dibayar!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Pilih denda yang akan dibayar!", "Peringatan", JOptionPane.WARNING_MESSAGE);
        }
    }
}
