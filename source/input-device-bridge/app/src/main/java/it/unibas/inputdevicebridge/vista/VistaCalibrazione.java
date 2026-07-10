package it.unibas.inputdevicebridge.vista;

import it.unibas.inputdevicebridge.controllo.ControlloCalibrazione;
import it.unibas.inputdevicebridge.enums.ETipologiaAzionePersonalizzata;
import it.unibas.inputdevicebridge.enums.ETipologiaEventoPersonalizzato;
import it.unibas.inputdevicebridge.modello.CalibratoreSegnale;
import it.unibas.inputdevicebridge.modello.Costanti;
import it.unibas.inputdevicebridge.modello.Modello;
import it.unibas.inputdevicebridge.modello.profilo_utente.ProfiloUtente;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.GraphicsEnvironment;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Singleton
public class VistaCalibrazione extends javax.swing.JDialog {

    private javax.swing.JLabel[] labelStep;
    private javax.swing.JSeparator[] separatoriStep;
    private CardLayout layoutPannelloVista;
    
    private final Modello modello;
    private final CalibratoreSegnale calibratoreSegnale;
    private final ControlloCalibrazione controlloCalibrazione;

    @Inject
    public VistaCalibrazione(Frame frame, Modello modello, CalibratoreSegnale calibratoreSegnale, ControlloCalibrazione controlloCalibrazione) {
        super(frame, true);
        this.modello = modello;
        this.calibratoreSegnale = calibratoreSegnale;
        this.controlloCalibrazione = controlloCalibrazione;
    }

    public void inizializza() {
        initComponents();
        this.labelStep = new javax.swing.JLabel[]{labelStep1, labelStep2, labelStep3};
        this.separatoriStep = new javax.swing.JSeparator[]{separatoreStep1, separatoreStep2};
        this.layoutPannelloVista = (CardLayout) this.pannelloVista.getLayout();
        this.addWindowListener(this.controlloCalibrazione.getAzioneChiusuraFinestra());
    }

    public void visualizza() {
        this.calibratoreSegnale.resetCalibratore();
        ProfiloUtente profiloUtente = (ProfiloUtente) this.modello.getBean(Costanti.PROFILO_UTENTE_TEMPORANEO);
        this.controlloCalibrazione.inizializza(profiloUtente);
        List<Map.Entry<ETipologiaEventoPersonalizzato, ETipologiaAzionePersonalizzata>> azioniPersonalizzate = this.controlloCalibrazione.getListaEntryEventiAzioniCalibrazione();
        Map<ETipologiaAzionePersonalizzata, JPanel> mappaAzioniViste = (Map<ETipologiaAzionePersonalizzata, JPanel>) this.modello.getBean(Costanti.MAPPA_AZIONI_VISTE);
        this.pannelloVista.removeAll();
        for (Map.Entry<ETipologiaEventoPersonalizzato, ETipologiaAzionePersonalizzata> entry : azioniPersonalizzate) {
            ETipologiaAzionePersonalizzata tipologiaAzionePersonalizzata = entry.getValue();
            JPanel schermoCorrente = mappaAzioniViste.get(tipologiaAzionePersonalizzata);
            this.pannelloVista.add(schermoCorrente);
            if (schermoCorrente instanceof IVistaCalibrazione) {
                ((IVistaCalibrazione) schermoCorrente).resetLabelContatore();
            }
        }
        this.setBounds(GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds());
        this.setLocationRelativeTo(this.getParent());
        this.controlloCalibrazione.avanzaSchermo(this);
    }

    public void avanzaSchermo() {
        this.controlloCalibrazione.avanzaSchermo(this);
    }

    public void mostraPannelloSuccessivo() {
        this.layoutPannelloVista.next(this.pannelloVista);
    }

    public void aggiornaStepBar(int indiceCorrente) {
        Color coloreCompletato = new Color(46, 139, 87);
        Color coloreCorrente = new Color(0, 102, 204);
        Color coloreMancante = Color.LIGHT_GRAY;
        this.aggiornaStepLabel(coloreCompletato, coloreCorrente, coloreMancante, indiceCorrente);
        this.aggiornaStepSeparatore(coloreCompletato, coloreMancante, indiceCorrente);
    }

    private void aggiornaStepLabel(Color coloreCompletato, Color coloreCorrente, Color coloreMancante, int indiceCorrente) {
        for (int i = 0; i < this.labelStep.length; i++) {
            if (i < indiceCorrente) {
                this.labelStep[i].setForeground(coloreCompletato);
            } else if (i == indiceCorrente) {
                this.labelStep[i].setForeground(coloreCorrente);
            } else {
                this.labelStep[i].setForeground(coloreMancante);
            }
        }
    }

    private void aggiornaStepSeparatore(Color coloreCompletato, Color coloreMancante, int indiceCorrente) {
        for (int i = 0; i < this.separatoriStep.length; i++) {
            if (i < indiceCorrente) {
                this.separatoriStep[i].setForeground(coloreCompletato);
                this.separatoriStep[i].setBackground(coloreCompletato);
            } else {
                this.separatoriStep[i].setForeground(coloreMancante);
                this.separatoriStep[i].setBackground(coloreMancante);
            }
        }
    }

    public int mostraConfermaUscita() {
        Object[] opzioni = {"Sì", "No"};
        return JOptionPane.showOptionDialog(
                this,
                "Sei sicuro di voler uscire ed interrompere la calibrazione?",
                "Conferma uscita",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opzioni,
                opzioni[1]);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.JPanel jPanel2 = new javax.swing.JPanel();
        javax.swing.JPanel jPanel1 = new javax.swing.JPanel();
        labelStep1 = new javax.swing.JLabel();
        labelStep2 = new javax.swing.JLabel();
        labelStep3 = new javax.swing.JLabel();
        separatoreStep2 = new javax.swing.JSeparator();
        separatoreStep1 = new javax.swing.JSeparator();
        pannelloVista = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Calibrazione");
        setResizable(false);

        labelStep1.setFont(new java.awt.Font("Segoe UI Black", 1, 36)); // NOI18N
        labelStep1.setText("•");

        labelStep2.setFont(new java.awt.Font("Segoe UI Black", 1, 36)); // NOI18N
        labelStep2.setText("•");

        labelStep3.setFont(new java.awt.Font("Segoe UI Black", 1, 36)); // NOI18N
        labelStep3.setText("•");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(labelStep1)
                .addGap(18, 18, 18)
                .addComponent(separatoreStep1, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(labelStep2)
                .addGap(18, 18, 18)
                .addComponent(separatoreStep2, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(labelStep3))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labelStep2)
                            .addComponent(labelStep1)
                            .addComponent(labelStep3))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(separatoreStep2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(separatoreStep1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(16, 16, 16))))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(90, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap(91, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pannelloVista.setLayout(new java.awt.CardLayout());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pannelloVista, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pannelloVista, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel labelStep1;
    private javax.swing.JLabel labelStep2;
    private javax.swing.JLabel labelStep3;
    private javax.swing.JPanel pannelloVista;
    private javax.swing.JSeparator separatoreStep1;
    private javax.swing.JSeparator separatoreStep2;
    // End of variables declaration//GEN-END:variables
}
