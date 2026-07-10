package it.unibas.inputdevicebridge.vista;

import it.unibas.inputdevicebridge.controllo.ControlloCalibrazioneDoppioClick;
import it.unibas.inputdevicebridge.enums.ETipologiaAzionePersonalizzata;
import it.unibas.inputdevicebridge.modello.CalibratoreSegnale;
import it.unibas.inputdevicebridge.modello.Costanti;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class VistaCalibrazioneDoppioClick extends javax.swing.JPanel implements IVistaCalibrazione {
    
    private final CalibratoreSegnale calibratoreSegnale;
    private final ControlloCalibrazioneDoppioClick controlloCalibrazioneDoppioClick;

    @Inject
    public VistaCalibrazioneDoppioClick(CalibratoreSegnale calibratoreSegnale, ControlloCalibrazioneDoppioClick controlloCalibrazioneDoppioClick) {
        this.calibratoreSegnale = calibratoreSegnale;
        this.controlloCalibrazioneDoppioClick = controlloCalibrazioneDoppioClick;
    }
    
    public void inizializza() {
        initComponents();
        this.bottoneDoppioClick.addMouseListener(this.controlloCalibrazioneDoppioClick.getMouseListenerDoppioClick());
        this.resetLabelContatore();
    }

    public void aggiornaLabelClickEffettuati(int numeroClickEffettuati) {
        this.labelDoppiClickEffettuati.setText("Doppi click effettuati: " + numeroClickEffettuati + "/" + Costanti.NUMERO_TENTATIVI_CALIBRAZIONE);
    }
    
    @Override
    public void resetLabelContatore() {
        int numeroClickEffettuati = this.calibratoreSegnale.numeroElementiDurateAzione(ETipologiaAzionePersonalizzata.DOPPIO_CLICK);
        this.aggiornaLabelClickEffettuati(numeroClickEffettuati);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.JLabel jLabel2 = new javax.swing.JLabel();
        bottoneDoppioClick = new javax.swing.JButton();
        labelDoppiClickEffettuati = new javax.swing.JLabel();
        javax.swing.JLabel jLabel1 = new javax.swing.JLabel();

        jLabel2.setText("Premi il pulsante diverse volte");

        bottoneDoppioClick.setText("Doppio click");

        labelDoppiClickEffettuati.setText("Doppi click effettuati: -/-");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("Calibrazione del  doppio click");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 102, Short.MAX_VALUE)
                        .addComponent(labelDoppiClickEffettuati))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(bottoneDoppioClick))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(labelDoppiClickEffettuati))
                .addGap(18, 18, 18)
                .addComponent(bottoneDoppioClick)
                .addContainerGap(190, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bottoneDoppioClick;
    private javax.swing.JLabel labelDoppiClickEffettuati;
    // End of variables declaration//GEN-END:variables

}
