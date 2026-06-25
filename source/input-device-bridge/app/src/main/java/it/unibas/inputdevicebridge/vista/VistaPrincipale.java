package it.unibas.inputdevicebridge.vista;

import it.unibas.inputdevicebridge.Applicazione;
import it.unibas.inputdevicebridge.modello.Costanti;
import it.unibas.inputdevicebridge.modello.IDeviceObserver;
import it.unibas.inputdevicebridge.modello.Punto;
import it.unibas.inputdevicebridge.modello.input_device.EyeTrackerStrategy;
import it.unibas.inputdevicebridge.modello.input_device.SwitchStrategy;
import it.unibas.inputdevicebridge.modello.profilo_utente.ArchivioProfiliUtente;
import it.unibas.inputdevicebridge.modello.profilo_utente.ProfiloUtente;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class VistaPrincipale extends javax.swing.JPanel implements IDeviceObserver {

    private Timer timerLampeggio;
    private Color coloreStatoCorrente;

    public void inizializza() {
        initComponents();
        Applicazione.getInstance().getDeviceBridge().addObserver(this);
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

    @Override
    public void onLatenzaAggiornata(float latenzaMs) {
        SwingUtilities.invokeLater(() -> {
            this.labelLatenza.setText(String.format("%.1f ms", latenzaMs));
        });
    }

    @Override
    public void onEsecuzioneDispositivoAggiornata(boolean inEsecuzione) {
        SwingUtilities.invokeLater(() -> {
            if (inEsecuzione) {
                if (this.timerLampeggio == null) {
                    this.timerLampeggio = new Timer(500, e -> {
                        Color corrente = this.labelStatoDispositivo.getForeground();

                        if (Color.LIGHT_GRAY.equals(corrente)) {
                            this.labelStatoDispositivo.setForeground(this.coloreStatoCorrente);
                        } else {
                            this.labelStatoDispositivo.setForeground(Color.LIGHT_GRAY);
                        }
                    });
                }
                if (!this.timerLampeggio.isRunning()) {
                    this.timerLampeggio.start();
                }
            } else {
                if (this.timerLampeggio != null) {
                    this.timerLampeggio.stop();
                }
                this.labelStatoDispositivo.setForeground(coloreStatoCorrente);
                this.resetLabel();
            }
        });
    }

    @Override
    public void onStatoDispositivoAggiornato(boolean connesso) {
        SwingUtilities.invokeLater(() -> {
            this.coloreStatoCorrente = connesso ? new Color(76, 175, 80) : new Color(255, 82, 82);
            this.labelStatoDispositivo.setForeground(coloreStatoCorrente);
            if (!connesso) {
                this.resetLabel();
            }
        });
    }

    private void resetLabel() {
        this.labelLatenza.setText("- ms");
        this.labelStatoApplicativo.setText("-");
        this.labelUltimaAzioneEseguita.setText("-");
        this.labelDescrizioneSegnale.setText("Segnale:");
        this.labelValoreSegnale.setVerticalAlignment(SwingConstants.BOTTOM);
        this.labelValoreSegnale.setFont(new Font("Segoe UI Black", Font.BOLD, 36));
        this.labelValoreSegnale.setText("•");
        this.labelValoreSegnale.setForeground(Color.LIGHT_GRAY);
    }

    @Override
    public void onStatoApplicativoAggiornato(String nomeStato) {
        SwingUtilities.invokeLater(() -> {
            this.labelStatoApplicativo.setText(nomeStato);
            this.labelStatoApplicativo.setForeground(new Color(0, 66, 226));
            Timer t = new Timer(300, e -> this.labelStatoApplicativo.setForeground(Color.BLACK));
            t.setRepeats(false);
            t.start();
        });
    }

    @Override
    public void onUltimaAzioneAggiornata(String nomeAzione) {
        SwingUtilities.invokeLater(() -> {
            this.labelUltimaAzioneEseguita.setText(nomeAzione);
            this.labelUltimaAzioneEseguita.setForeground(new Color(0, 66, 226));
            Timer t = new Timer(300, e -> this.labelUltimaAzioneEseguita.setForeground(Color.BLACK));
            t.setRepeats(false);
            t.start();
        });
    }

    @Override
    public void onDatiSegnaleAggiornati(Punto coordinate, Float intensita) {
        SwingUtilities.invokeLater(() -> {
            if (coordinate != null) {
                this.labelDescrizioneSegnale.setText("Coordinate:");
                this.labelValoreSegnale.setForeground(Color.BLACK);
                this.labelValoreSegnale.setVerticalAlignment(SwingConstants.TOP);
                this.labelValoreSegnale.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                this.labelValoreSegnale.setText(String.format("(x = %.2f , y = %.2f)", coordinate.getX(), coordinate.getY()));
            }
            if (intensita != null) {
                this.labelDescrizioneSegnale.setText("Intensità:");
                this.labelValoreSegnale.setVerticalAlignment(SwingConstants.BOTTOM);
                this.labelValoreSegnale.setFont(new Font("Segoe UI Black", Font.BOLD, 36));
                this.labelValoreSegnale.setText("•");
                this.labelValoreSegnale.setForeground(this.calcolaColoreIntensita(intensita));
            }
        });
    }

    private Color calcolaColoreIntensita(float intensita) {
        float t = Math.max(-1.0f, Math.min(1.0f, intensita));
        float hue;
        if (t >= 0) {
            hue = 0.611f; // blu
        } else {
            hue = 0.833f; // magenta
        }
        float saturation = Math.abs(t) * 0.6f;
        float brightness = 0.8f;
        return Color.getHSBColor(hue, saturation, brightness);
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
        javax.swing.JPanel jPanel3 = new javax.swing.JPanel();
        javax.swing.JLabel jLabel2 = new javax.swing.JLabel();
        labelLatenza = new javax.swing.JLabel();
        javax.swing.JSeparator jSeparator1 = new javax.swing.JSeparator();
        javax.swing.JLabel jLabel4 = new javax.swing.JLabel();
        labelStatoDispositivo = new javax.swing.JLabel();
        javax.swing.JSeparator jSeparator2 = new javax.swing.JSeparator();
        javax.swing.JLabel jLabel6 = new javax.swing.JLabel();
        javax.swing.JLabel jLabel7 = new javax.swing.JLabel();
        labelDescrizioneSegnale = new javax.swing.JLabel();
        labelValoreSegnale = new javax.swing.JLabel();
        labelStatoApplicativo = new javax.swing.JLabel();
        labelUltimaAzioneEseguita = new javax.swing.JLabel();

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

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Monitor input"));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Latenza");

        labelLatenza.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        labelLatenza.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelLatenza.setText("- ms");

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Stato dispositivo");

        labelStatoDispositivo.setFont(new java.awt.Font("Segoe UI Black", 1, 48)); // NOI18N
        labelStatoDispositivo.setForeground(new java.awt.Color(255, 82, 82));
        labelStatoDispositivo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelStatoDispositivo.setText("•");

        jLabel6.setText("Stato applicativo: ");

        jLabel7.setText("Ultima azione eseguita: ");

        labelDescrizioneSegnale.setText("Segnale:");

        labelValoreSegnale.setFont(new java.awt.Font("Segoe UI Black", 1, 36)); // NOI18N
        labelValoreSegnale.setForeground(new java.awt.Color(204, 204, 204));
        labelValoreSegnale.setText("•");
        labelValoreSegnale.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        labelStatoApplicativo.setText("-");

        labelUltimaAzioneEseguita.setText("-");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator2)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(labelLatenza, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 219, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 3, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
                            .addComponent(labelStatoDispositivo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(labelDescrizioneSegnale)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(labelValoreSegnale))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(labelStatoApplicativo))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(labelUltimaAzioneEseguita)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(18, 18, 18)
                                .addComponent(labelLatenza))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(labelStatoDispositivo, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(labelStatoApplicativo))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(labelUltimaAzioneEseguita))
                .addGap(18, 18, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(labelValoreSegnale, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(labelDescrizioneSegnale, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(6, 6, 6)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(labelProfiloUtente)
                                .addGap(18, 18, 18)
                                .addComponent(comboProfili, 0, 278, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addComponent(bottoneGesioneProfilo)))
                        .addGap(2, 2, 2))
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(bottoneFerma)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bottoneAvvia)))
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
                .addGap(18, 18, 18)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
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
    private javax.swing.JLabel labelDescrizioneSegnale;
    private javax.swing.JLabel labelLatenza;
    private javax.swing.JLabel labelProfiloUtente;
    private javax.swing.JLabel labelStatoApplicativo;
    private javax.swing.JLabel labelStatoDispositivo;
    private javax.swing.JLabel labelUltimaAzioneEseguita;
    private javax.swing.JLabel labelValoreSegnale;
    private javax.swing.JRadioButton radioEyeTracker;
    private javax.swing.JRadioButton radioHeadPointer;
    private javax.swing.JRadioButton radioSipAndPuff;
    private javax.swing.JRadioButton radioSwitch;
    // End of variables declaration//GEN-END:variables

}
