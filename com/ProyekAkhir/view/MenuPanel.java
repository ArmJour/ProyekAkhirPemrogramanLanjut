package com.ProyekAkhir.view;

import com.ProyekAkhir.service.Perpustakaan;
import javax.swing.*;
import java.awt.*;

public class MenuPanel extends JPanel {
    public MenuPanel(Perpustakaan perpustakaan) {
        setLayout(new BorderLayout());
        
        JLabel welcomeLabel = new JLabel("Selamat Datang di Sistem Manajemen Perpustakaan", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        
        JPanel statsPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        statsPanel.add(createStatCard("Total Buku", String.valueOf(perpustakaan.getListBuku().size())));
        statsPanel.add(createStatCard("Total Pengguna", String.valueOf(perpustakaan.getListPengguna().size())));
        statsPanel.add(createStatCard("Peminjaman Aktif", String.valueOf(
            perpustakaan.getListPeminjaman().stream()
                .filter(p -> p.getTanggalKembali() == null)
                .count()
        )));
        statsPanel.add(createStatCard("Denda Belum Lunas", String.valueOf(
            perpustakaan.getListDenda().stream()
                .filter(d -> !d.isStatusPembayaran())
                .count()
        )));
        
        add(welcomeLabel, BorderLayout.NORTH);
        add(statsPanel, BorderLayout.CENTER);
    }
    
    private JPanel createStatCard(String title, String value) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        JLabel valueLabel = new JLabel(value, SwingConstants.CENTER);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 36));
        
        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        
        return card;
    }
}
