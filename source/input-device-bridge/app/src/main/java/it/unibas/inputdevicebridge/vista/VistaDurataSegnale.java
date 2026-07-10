package it.unibas.inputdevicebridge.vista;

import jakarta.inject.Singleton;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.BorderFactory;

@Singleton
public class VistaDurataSegnale extends javax.swing.JPanel {

    public void inizializza() {
        initComponents();
        this.resetTextLabelSecondi();
        this.setOpaque(false);
        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createEmptyBorder(7, 10, 7, 10));
        this.labelSecondi.setForeground(Color.WHITE);
        this.labelSecondi.setFont(new Font("Segoe UI", Font.BOLD, 15));
        this.add(this.labelSecondi, BorderLayout.CENTER);
    }

    public void setTextLabelSecondi(float secondi) {
        this.labelSecondi.setText(String.format(java.util.Locale.US, "%.1f s", secondi));
    }

    public void resetTextLabelSecondi() {
        this.labelSecondi.setText("0.0 s");
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D) graphics.create();
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        // Ombra
        graphics2D.setColor(new Color(0, 0, 0, 60));
        graphics2D.fillRoundRect(3, 3, this.getWidth() - 3, this.getHeight() - 3, 19, 19);
        // Sfondo
        graphics2D.setColor(new Color(35, 35, 35, 220));
        graphics2D.fillRoundRect(0, 0, this.getWidth() - 4, this.getHeight() - 4, 19, 19);
        graphics2D.dispose();
        super.paintComponent(graphics);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        labelSecondi = new javax.swing.JLabel();

        labelSecondi.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelSecondi.setText("0.0 s");
        labelSecondi.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        labelSecondi.setPreferredSize(new java.awt.Dimension(50, 25));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelSecondi, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                .addGap(369, 369, 369))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelSecondi, javax.swing.GroupLayout.DEFAULT_SIZE, 16, Short.MAX_VALUE)
                .addGap(278, 278, 278))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel labelSecondi;
    // End of variables declaration//GEN-END:variables
}
