package it.unibas.inputdevicebridge.vista;

import it.unibas.inputdevicebridge.Applicazione;
import it.unibas.inputdevicebridge.enums.ETipologiaAzionePersonalizzata;
import it.unibas.inputdevicebridge.modello.Costanti;

public class VistaCalibrazioneClick extends javax.swing.JPanel implements IVistaCalibrazione {

    public void inizializza() {
        initComponents();
        this.bottoneClick.setAction(Applicazione.getInstance().getControlloCalibrazioneClick().getAzioneCalibraClick());
        this.resetLabelContatore();
    }

    public void aggiornaLabelClickEffettuati(int numeroClickEffettuati) {
        this.labelClickEffettuati.setText("Click effettuati: " + numeroClickEffettuati + "/" + Costanti.NUMERO_TENTATIVI_CALIBRAZIONE);
    }
    
    @Override
    public void resetLabelContatore() {
        int numeroClickEffettuati = Applicazione.getInstance().getCalibratoreSegnale().numeroElementiDurateAzione(ETipologiaAzionePersonalizzata.CLICK);
        this.aggiornaLabelClickEffettuati(numeroClickEffettuati);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.JLabel jLabel2 = new javax.swing.JLabel();
        bottoneClick = new javax.swing.JButton();
        labelClickEffettuati = new javax.swing.JLabel();
        javax.swing.JLabel jLabel1 = new javax.swing.JLabel();

        jLabel2.setText("Premi il pulsante diverse volte");

        bottoneClick.setText("Clicca");

        labelClickEffettuati.setText("Click effettuati: -/-");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("Calibrazione del click");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 136, Short.MAX_VALUE)
                        .addComponent(labelClickEffettuati))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(bottoneClick))
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
                    .addComponent(labelClickEffettuati))
                .addGap(18, 18, 18)
                .addComponent(bottoneClick)
                .addContainerGap(190, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bottoneClick;
    private javax.swing.JLabel labelClickEffettuati;
    // End of variables declaration//GEN-END:variables

}
