package it.unibas.inputdevicebridge.vista;

import it.unibas.inputdevicebridge.Applicazione;
import it.unibas.inputdevicebridge.modello.Costanti;
import it.unibas.inputdevicebridge.modello.input_device.EyeTrackerStrategy;
import it.unibas.inputdevicebridge.modello.input_device.SwitchStrategy;
import it.unibas.inputdevicebridge.modello.profilo_utente.ArchivioProfiliUtente;
import it.unibas.inputdevicebridge.modello.profilo_utente.ProfiloUtente;
import java.awt.event.ActionListener;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class VistaPrincipale extends javax.swing.JPanel {

    public void inizializza() {
        initComponents();
        this.inizializzaComboProfili();
        this.inizializzaActionCommandRadio();
        this.bottoneGesioneProfilo.setAction(Applicazione.getInstance().getControlloMenu().getAzioneGestioneProfilo());
        this.bottoneAvvia.setAction(Applicazione.getInstance().getControlloPrincipale().getAzioneAvvia());
        this.bottoneFerma.setAction(Applicazione.getInstance().getControlloPrincipale().getAzioneFerma());
        ActionListener azioneAggiornaDispositivoSelezionato = Applicazione.getInstance().getControlloPrincipale().getAzioneAggiornaDispositivoSelezionato();
        azioneAggiornaDispositivoSelezionato.actionPerformed(null);
        this.radioEyeTracker.addActionListener(azioneAggiornaDispositivoSelezionato);
        this.radioHeadPointer.addActionListener(azioneAggiornaDispositivoSelezionato);
        this.radioSwitch.addActionListener(azioneAggiornaDispositivoSelezionato);
        this.radioSipAndPuff.addActionListener(azioneAggiornaDispositivoSelezionato);
    }

    public void inizializzaComboProfili() {
        this.comboProfili.setAction(null);
        this.comboProfili.removeAllItems();
        ProfiloUtente profiloUtente = (ProfiloUtente) Applicazione.getInstance().getModello().getBean(Costanti.PROFILO_UTENTE_SELEZIONATO);
        if (profiloUtente == null) {
            this.comboProfili.setEnabled(false);
        } else {
            this.comboProfili.setEnabled(true);
            ArchivioProfiliUtente archivioProfiliUtente = (ArchivioProfiliUtente) Applicazione.getInstance().getModello().getBean(Costanti.ARCHIVIO_PROFILI_UTENTE);
            for (ProfiloUtente profiloUtenteCorrente : archivioProfiliUtente.getListaProfiliUtente()) {
                this.comboProfili.addItem(profiloUtenteCorrente.getNome());
            }
            this.comboProfili.setSelectedItem(profiloUtente.getNome());
        }
        this.comboProfili.setAction(Applicazione.getInstance().getControlloPrincipale().getAzioneAggiornaProfiloSelezionato());
    }

    private void inizializzaActionCommandRadio() {
        this.radioEyeTracker.setActionCommand(EyeTrackerStrategy.class.getSimpleName());
        //this.radioHeadPointer.setActionCommand(HeadPointerStrategy.class.getSimpleName());
        this.radioSwitch.setActionCommand(SwitchStrategy.class.getSimpleName());
        //this.radioSipAndPuff.setActionCommand(SipAndPuffStrategy.class.getSimpleName());
    }

    public String getTextItemSelezionataComboProfili() {
        return this.comboProfili.getSelectedItem().toString();
    }

    public String getTextRadioDispositiviSelezionato() {
        return this.buttonGroupDispositivo.getSelection().getActionCommand();
    }

    public void aggiornaStatoEsecuzione(boolean inEsecuzione) {
        this.bottoneAvvia.setEnabled(!inEsecuzione);
        this.bottoneFerma.setEnabled(inEsecuzione);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroupDispositivo = new javax.swing.ButtonGroup();
        javax.swing.JPanel jPanel1 = new javax.swing.JPanel();
        javax.swing.JLabel jLabel1 = new javax.swing.JLabel();
        javax.swing.JPanel jPanel2 = new javax.swing.JPanel();
        radioEyeTracker = new javax.swing.JRadioButton();
        radioHeadPointer = new javax.swing.JRadioButton();
        radioSwitch = new javax.swing.JRadioButton();
        radioSipAndPuff = new javax.swing.JRadioButton();
        bottoneAvvia = new javax.swing.JButton();
        bottoneFerma = new javax.swing.JButton();
        labelProfiloUtente = new javax.swing.JLabel();
        comboProfili = new javax.swing.JComboBox<>();
        bottoneGesioneProfilo = new javax.swing.JButton();

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 26)); // NOI18N
        jLabel1.setText("Input Device Bridge");

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Seleziona un dispositivo"));

        buttonGroupDispositivo.add(radioEyeTracker);
        radioEyeTracker.setSelected(true);
        radioEyeTracker.setText("Eye-tracker");

        buttonGroupDispositivo.add(radioHeadPointer);
        radioHeadPointer.setText("Head-pointer");
        radioHeadPointer.setEnabled(false);

        buttonGroupDispositivo.add(radioSwitch);
        radioSwitch.setText("Switch");

        buttonGroupDispositivo.add(radioSipAndPuff);
        radioSipAndPuff.setText("Sip and puff");
        radioSipAndPuff.setEnabled(false);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(radioEyeTracker)
                    .addComponent(radioHeadPointer)
                    .addComponent(radioSwitch)
                    .addComponent(radioSipAndPuff))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(radioEyeTracker)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(radioHeadPointer)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(radioSwitch)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(radioSipAndPuff)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        bottoneAvvia.setText("Avvia");

        bottoneFerma.setText("Stop");

        labelProfiloUtente.setText("Profilo utente");

        comboProfili.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        bottoneGesioneProfilo.setText("Gestione profilo");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(bottoneFerma)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bottoneAvvia))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(labelProfiloUtente)
                                .addGap(18, 18, 18)
                                .addComponent(comboProfili, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(bottoneGesioneProfilo)))
                        .addGap(0, 2, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelProfiloUtente)
                    .addComponent(comboProfili, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bottoneGesioneProfilo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 71, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bottoneAvvia)
                    .addComponent(bottoneFerma))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bottoneAvvia;
    private javax.swing.JButton bottoneFerma;
    private javax.swing.JButton bottoneGesioneProfilo;
    private javax.swing.ButtonGroup buttonGroupDispositivo;
    private javax.swing.JComboBox<String> comboProfili;
    private javax.swing.JLabel labelProfiloUtente;
    private javax.swing.JRadioButton radioEyeTracker;
    private javax.swing.JRadioButton radioHeadPointer;
    private javax.swing.JRadioButton radioSipAndPuff;
    private javax.swing.JRadioButton radioSwitch;
    // End of variables declaration//GEN-END:variables

}
