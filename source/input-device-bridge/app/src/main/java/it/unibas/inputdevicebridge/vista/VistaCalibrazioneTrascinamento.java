package it.unibas.inputdevicebridge.vista;

import it.unibas.inputdevicebridge.controllo.ControlloCalibrazioneTrascinamento;
import it.unibas.inputdevicebridge.enums.ETipologiaAzionePersonalizzata;
import it.unibas.inputdevicebridge.modello.CalibratoreSegnale;
import it.unibas.inputdevicebridge.modello.Costanti;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.awt.event.MouseAdapter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Singleton
public class VistaCalibrazioneTrascinamento extends javax.swing.JPanel implements IVistaCalibrazione {

    private final CalibratoreSegnale calibratoreSegnale;
    private final ControlloCalibrazioneTrascinamento controlloCalibrazioneTrascinamento;

    @Inject
    public VistaCalibrazioneTrascinamento(CalibratoreSegnale calibratoreSegnale, ControlloCalibrazioneTrascinamento controlloCalibrazioneTrascinamento) {
        this.calibratoreSegnale = calibratoreSegnale;
        this.controlloCalibrazioneTrascinamento = controlloCalibrazioneTrascinamento;
    }

    public void inizializza() {
        initComponents();
        this.resetLabelContatore();
        MouseAdapter listenerTrascinamento = this.controlloCalibrazioneTrascinamento.getListenerTrascinamento();
        this.textPane.addMouseListener(listenerTrascinamento);
        this.textPane.addMouseMotionListener(listenerTrascinamento);
    }

    public boolean haTestoSelezionato() {
        return this.textPane.getSelectionStart() != this.textPane.getSelectionEnd();
    }

    public void aggiornaLabelTrascinamentiEffettuati(int numeroTrascinamentiEffettuati) {
        this.labelTrascinamentiEffettuati.setText("Trascinamenti effettuati: " + numeroTrascinamentiEffettuati + "/" + Costanti.NUMERO_TENTATIVI_CALIBRAZIONE);
    }

    @Override
    public void resetLabelContatore() {
        int numeroTrascinamentiEffettuati = this.calibratoreSegnale.numeroElementiDurateAzione(ETipologiaAzionePersonalizzata.TRASCINAMENTO);
        this.aggiornaLabelTrascinamentiEffettuati(numeroTrascinamentiEffettuati);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.JLabel jLabel1 = new javax.swing.JLabel();
        javax.swing.JLabel jLabel2 = new javax.swing.JLabel();
        javax.swing.JScrollPane jScrollPane1 = new javax.swing.JScrollPane();
        textPane = new javax.swing.JTextPane();
        labelTrascinamentiEffettuati = new javax.swing.JLabel();

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("Calibrazione trascinamento");

        jLabel2.setText("Trascina il cursore per selezionare una parte del testo");

        textPane.setEditable(false);
        textPane.setText("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi cursus id odio at facilisis. Sed sodales volutpat facilisis. Cras semper libero ultricies augue viverra, quis ultricies est condimentum. Nam at mauris libero. Vivamus facilisis lacus eu ligula vestibulum posuere. Sed laoreet turpis at lacus pretium, vel varius ex tempus. Integer ac ultricies nisi. Sed eleifend diam et mi finibus, quis tincidunt metus volutpat.\n\nMauris lacinia, enim ut sollicitudin ornare, arcu diam interdum lacus, non placerat dolor ligula et quam. Morbi tincidunt interdum pretium. Duis varius nisi erat, vel commodo nibh laoreet sed. Curabitur luctus ultrices tortor quis consectetur. Curabitur leo enim, fermentum nec justo varius, rhoncus posuere urna. Integer fermentum risus nec ante accumsan dapibus. Suspendisse eget feugiat quam. Aliquam vitae lacus ultrices turpis scelerisque facilisis. Sed pulvinar suscipit justo, nec imperdiet turpis tempus eget. Proin blandit ultrices ornare. Proin condimentum commodo velit. Proin in dui neque. Nullam a fermentum ligula, vitae tempus quam. Maecenas condimentum molestie pharetra. Sed massa tortor, tempor a bibendum in, bibendum eget augue.\n\nNullam consequat dui id lorem convallis vehicula. Praesent interdum tincidunt augue ac ultricies. Cras varius velit vitae magna mattis facilisis eget ac est. Ut in faucibus magna. Fusce non quam in sem congue pulvinar at nec orci. Aliquam lobortis urna eu lacus dignissim, vel pharetra lorem molestie. Pellentesque tempus odio vitae metus pellentesque faucibus. Maecenas ultrices est sed sapien cursus, facilisis feugiat sapien dictum. Maecenas leo lorem, ultrices vel nisi quis, mattis luctus ex.\n\nIn accumsan eu ipsum ac tempus. Aliquam consequat ullamcorper cursus. Morbi elementum libero in nisi egestas ornare. Etiam hendrerit nec mi in ultricies. Fusce at efficitur massa. Duis hendrerit, augue eget bibendum rhoncus, risus nibh interdum ex, nec gravida felis massa accumsan elit. Sed tempus dui nec nisl posuere aliquam. Mauris rutrum finibus dolor vel pulvinar. Proin eu imperdiet neque. Donec leo ipsum, hendrerit non placerat sit amet, finibus vitae risus. Ut tempor elementum consectetur. Proin sed tempor ex. In congue ante ut pretium dapibus. Nunc eget erat viverra, faucibus dolor at, mollis urna. Duis sit amet turpis a leo maximus ornare.\n\nNam sagittis tortor a ultricies fringilla. Nulla facilisi. Etiam justo est, venenatis a fringilla at, ornare id augue. Nullam in mollis augue. Morbi laoreet a dolor eget placerat. Etiam porttitor purus in est convallis, at cursus leo tristique. Nulla facilisi. Integer in est eget diam interdum rhoncus et sed sapien. Aenean dolor magna, dapibus a purus sit amet, molestie congue libero. Phasellus maximus lorem at nunc egestas consequat. Praesent mi felis, fermentum vitae sollicitudin eu, semper sit amet massa. Curabitur accumsan congue lectus quis finibus. Fusce viverra ante hendrerit, efficitur tortor et, lacinia nulla.\n\nNam elementum lorem nec justo iaculis rhoncus. Aliquam vel accumsan odio. Vestibulum porttitor tempor diam nec fringilla. Morbi vestibulum est vitae arcu condimentum convallis. Fusce cursus orci et tortor fermentum, ut porttitor velit hendrerit. Sed id elit nisl. Morbi vestibulum, augue ut mattis fermentum, elit est elementum mi, sit amet gravida lacus mauris vitae arcu. Pellentesque sollicitudin diam felis, eget consequat elit molestie non. Vestibulum sed arcu lacinia, dapibus felis lacinia, blandit risus. Sed lacinia eget leo ut mollis. Maecenas id nunc ac augue placerat facilisis. Duis tempor vel tellus eget tincidunt.\n\nFusce at velit sed magna commodo venenatis. Ut aliquam purus purus, ut ultrices nunc convallis eget. Donec quis euismod justo, sagittis suscipit tortor. Mauris et volutpat purus. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce non scelerisque ex. Sed viverra leo nec sem tempus, et accumsan ligula condimentum. Quisque rutrum lobortis ligula, vel ullamcorper sem gravida et. Duis tempor ac arcu eleifend ornare. Vivamus leo magna, elementum nec mi quis, egestas convallis lectus.");
        jScrollPane1.setViewportView(textPane);

        labelTrascinamentiEffettuati.setText("Trascinamenti effettuati: -/-");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 249, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(labelTrascinamentiEffettuati)))
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
                    .addComponent(labelTrascinamentiEffettuati))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 211, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel labelTrascinamentiEffettuati;
    private javax.swing.JTextPane textPane;
    // End of variables declaration//GEN-END:variables

}
