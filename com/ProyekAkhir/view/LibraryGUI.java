package com.ProyekAkhir.view;

import com.ProyekAkhir.service.Perpustakaan;
import javax.swing.*;

public class LibraryGUI extends JFrame {
    private Perpustakaan perpustakaan;
    
    public LibraryGUI() {
        this.perpustakaan = new Perpustakaan();
        initUI();
    }
    
    private void initUI() {
        setTitle("Sistem Manajemen Perpustakaan");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Create tabbed pane
        JTabbedPane tabbedPane = new JTabbedPane();
        
        // Add panels to tabbed pane
        tabbedPane.addTab("Menu Utama", new MenuPanel(perpustakaan));
        tabbedPane.addTab("Manajemen Buku", new BookPanel(perpustakaan));
        tabbedPane.addTab("Manajemen Pengguna", new UserPanel(perpustakaan));
        tabbedPane.addTab("Transaksi", new TransactionPanel(perpustakaan));
        tabbedPane.addTab("Denda", new FinePanel(perpustakaan));
        
        add(tabbedPane);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LibraryGUI frame = new LibraryGUI();
            frame.setVisible(true);
        });
    }
}