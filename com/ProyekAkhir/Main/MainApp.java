package com.ProyekAkhir.Main;

import com.ProyekAkhir.view.LibraryGUI;
import javax.swing.SwingUtilities;

public class MainApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                LibraryGUI frame = new LibraryGUI();

                frame.setVisible(true);
            }
        });
    }
}
