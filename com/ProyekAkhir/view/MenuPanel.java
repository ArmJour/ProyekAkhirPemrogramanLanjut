package com.ProyekAkhir.view;

import com.ProyekAkhir.Model.LogDenda;
import com.ProyekAkhir.Model.LogPeminjaman;
import com.ProyekAkhir.service.Perpustakaan;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.*;

public class MenuPanel extends JPanel {
    private final Perpustakaan perpustakaan;
    private final JLabel bookCountLabel;
    private final JLabel userCountLabel;
    private final JLabel loanCountLabel;
    private final JLabel fineCountLabel;
    public MenuPanel(Perpustakaan perpustakaan) {
        this.perpustakaan = perpustakaan;
        setLayout(new BorderLayout(10, 20));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel welcomeLabel = new JLabel("Selamat Datang di Sistem Manajemen Perpustakaan", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(welcomeLabel, BorderLayout.NORTH);

        JPanel statsPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        
        // Inisialisasi label yang akan menampilkan angka
        bookCountLabel = createValueLabel();
        userCountLabel = createValueLabel();
        loanCountLabel = createValueLabel();
        fineCountLabel = createValueLabel();

        statsPanel.add(createStatCard("Total Buku", bookCountLabel));
        statsPanel.add(createStatCard("Total Pengguna", userCountLabel));
        statsPanel.add(createStatCard("Peminjaman Aktif", loanCountLabel));
        statsPanel.add(createStatCard("Denda Belum Lunas", fineCountLabel));
        add(statsPanel, BorderLayout.CENTER);

        JButton refreshButton = new JButton("Refresh Statistik");
        refreshButton.addActionListener(e -> updateStats()); 

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.add(refreshButton);
        add(bottomPanel, BorderLayout.SOUTH);
        
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                updateStats();
            }
        });
        
        // Panggil sekali saat pertama kali dibuat untuk memuat data awal
        updateStats();
    }

    private void updateStats() {
        // Mengupdate total buku dan pengguna
        bookCountLabel.setText(String.valueOf(perpustakaan.getListBuku().size()));
        userCountLabel.setText(String.valueOf(perpustakaan.getListPengguna().size()));
        
        // Menghitung peminjaman aktif dengan for loop
        int activeLoans = 0;
        for (LogPeminjaman p : perpustakaan.getListPeminjaman()) {
            if (p.getTanggalKembali() == null) {
                activeLoans++;
            }
        }
        loanCountLabel.setText(String.valueOf(activeLoans));
        
        // Menghitung denda yang belum lunas dengan for loop
        int unpaidFines = 0;
        for (LogDenda d : perpustakaan.getListDenda()) {
            if (!d.isStatusPembayaran()) {
                unpaidFines++;
            }
        }
        fineCountLabel.setText(String.valueOf(unpaidFines));
    }
    
    private JLabel createValueLabel() {
        JLabel label = new JLabel("0", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 36));
        return label;
    }
    
    private JPanel createStatCard(String title, JLabel valueLabel) {
        JPanel card = new JPanel(new BorderLayout(0, 5));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY, 1, true),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        
        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        
        return card;
    }
}
