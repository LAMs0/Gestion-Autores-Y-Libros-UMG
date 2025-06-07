package com.example;

import com.example.gui.BibliotecaGUI;

import javax.swing.*;

public class MainApp {
    public static void main(String[] args) {
        // Configurar look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            BibliotecaGUI gui = new BibliotecaGUI();
            gui.setVisible(true);

            // Cerrar conexiones al salir
            gui.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                    gui.cerrarConexiones();
                }
            });
        });
    }
}