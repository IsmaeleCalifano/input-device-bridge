package it.unibas.inputdevicebridge.vista;

import it.unibas.inputdevicebridge.Applicazione;
import it.unibas.inputdevicebridge.enums.ETipologiaAzionePersonalizzata;
import it.unibas.inputdevicebridge.enums.ETipologiaEventoPersonalizzato;
import it.unibas.inputdevicebridge.modello.Costanti;
import it.unibas.inputdevicebridge.modello.profilo_utente.ProfiloUtente;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class VistaGestioneProfilo extends javax.swing.JDialog {

    public VistaGestioneProfilo(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
    }
    
    public void inizializza() {
        initComponents();
        this.formattaSliderDecimali();
        this.inizializzaComboAzioneSegnaleBreve();
        this.inizializzaComboAzioneSegnaleMedio();
        this.inizializzaComboAzioneSegnaleLungo();
    }
    
    private void inizializzaComboAzioneSegnaleBreve() {
        this.comboAzioneSegnaleBreve.removeAllItems();
        for (ETipologiaAzionePersonalizzata tipologiaAzionePersonalizzata : ETipologiaAzionePersonalizzata.values()) {
            this.comboAzioneSegnaleBreve.addItem(tipologiaAzionePersonalizzata);
        }
    }
    
    private void inizializzaComboAzioneSegnaleMedio() {
        this.comboAzioneSegnaleMedio.removeAllItems();
        for (ETipologiaAzionePersonalizzata tipologiaAzionePersonalizzata : ETipologiaAzionePersonalizzata.values()) {
            this.comboAzioneSegnaleMedio.addItem(tipologiaAzionePersonalizzata);
        }
    }
    
    private void inizializzaComboAzioneSegnaleLungo() {
        this.comboAzioneSegnaleLungo.removeAllItems();
        for (ETipologiaAzionePersonalizzata tipologiaAzionePersonalizzata : ETipologiaAzionePersonalizzata.values()) {
            this.comboAzioneSegnaleLungo.addItem(tipologiaAzionePersonalizzata);
        }
    }
    
    public void visualizzaModifica() {
        this.inizializzaCampiProfiloSelezionato();
        this.bottoneSalva.setAction(Applicazione.getInstance().getControlloGestioneProfilo().getAzioneSalvaModificheProfilo());
        this.setLocationRelativeTo(this.getParent());
        this.setVisible(true);
    }
    
    public void visualizzaNuovo() {
        this.inizializzaCampiNuovoProfilo();
        this.bottoneSalva.setAction(Applicazione.getInstance().getControlloGestioneProfilo().getAzioneSalvaNuovoPrfilo());
        this.setLocationRelativeTo(this.getParent());
        this.setVisible(true);
    }
    
    private void inizializzaCampiProfiloSelezionato() {
        super.setTitle("Gestione profilo utente");
        ProfiloUtente profiloUtente = (ProfiloUtente) Applicazione.getInstance().getModello().getBean(Costanti.PROFILO_UTENTE_SELEZIONATO);
        this.textFiledNomeProfilo.setText(profiloUtente.getNome());
        this.sliderSogliaSensibilita.setValue((int) (profiloUtente.getSogliaSensibilita() * 10));
        this.sliderSogliaZoneMorte.setValue((int) (profiloUtente.getSogliaZonaMorta() * 10));
        Map<ETipologiaEventoPersonalizzato, Long> mappaDurataSegnale =  profiloUtente.getMappaDurataSegnale();
        this.spinnerSegnaleBreve.setValue((float) mappaDurataSegnale.get(ETipologiaEventoPersonalizzato.SEGNALE_BREVE) / Costanti.DURATA_1_SECONDO);
        this.spinnerSegnaleMedio.setValue((float) mappaDurataSegnale.get(ETipologiaEventoPersonalizzato.SEGNALE_MEDIO) / Costanti.DURATA_1_SECONDO);
        this.spinnerSegnaleLungo.setValue((float) mappaDurataSegnale.get(ETipologiaEventoPersonalizzato.SEGNALE_LUNGO) / Costanti.DURATA_1_SECONDO);
        Map<ETipologiaEventoPersonalizzato, ETipologiaAzionePersonalizzata> mappaComandiPersonalizzati =  profiloUtente.getMappaComandiPersonalizzati();
        this.comboAzioneSegnaleBreve.setSelectedItem(mappaComandiPersonalizzati.get(ETipologiaEventoPersonalizzato.SEGNALE_BREVE));
        this.comboAzioneSegnaleMedio.setSelectedItem(mappaComandiPersonalizzati.get(ETipologiaEventoPersonalizzato.SEGNALE_MEDIO));
        this.comboAzioneSegnaleLungo.setSelectedItem(mappaComandiPersonalizzati.get(ETipologiaEventoPersonalizzato.SEGNALE_LUNGO));
    }
    
     private void inizializzaCampiNuovoProfilo() {
        super.setTitle("Crea nuovo profilo utente");
        this.textFiledNomeProfilo.setText("");
        this.sliderSogliaSensibilita.setValue(this.sliderSogliaSensibilita.getMinimum());
        this.sliderSogliaZoneMorte.setValue(this.sliderSogliaZoneMorte.getMinimum());
        this.spinnerSegnaleBreve.setValue(2.0f);
        this.spinnerSegnaleMedio.setValue(6.0f);
        this.spinnerSegnaleLungo.setValue(10.0f);
        this.comboAzioneSegnaleBreve.setSelectedItem(ETipologiaAzionePersonalizzata.CLICK);
        this.comboAzioneSegnaleMedio.setSelectedItem(ETipologiaAzionePersonalizzata.SCROLL);
        this.comboAzioneSegnaleLungo.setSelectedItem(ETipologiaAzionePersonalizzata.TRASCINAMENTO);
    }
    
    public String getTextCampoNomeProfilo() {
        return this.textFiledNomeProfilo.getText();
    }
    
    public ProfiloUtente getProfiloAggiornato() {
        String nome = this.textFiledNomeProfilo.getText();
        float sogliaSensibilita = this.sliderSogliaSensibilita.getValue() / 10.0f;
        float sogliaZoneMorte = this.sliderSogliaZoneMorte.getValue() / 10.0f;
        ProfiloUtente profiloUtente = new ProfiloUtente(nome, sogliaZoneMorte, sogliaSensibilita);
        Map<ETipologiaEventoPersonalizzato, Long> mappaDurataSegnale =  profiloUtente.getMappaDurataSegnale();
        mappaDurataSegnale.put(ETipologiaEventoPersonalizzato.SEGNALE_BREVE, (long)(((float) this.spinnerSegnaleBreve.getValue()) * Costanti.DURATA_1_SECONDO));
        mappaDurataSegnale.put(ETipologiaEventoPersonalizzato.SEGNALE_MEDIO, (long)(((float) this.spinnerSegnaleMedio.getValue()) * Costanti.DURATA_1_SECONDO));
        mappaDurataSegnale.put(ETipologiaEventoPersonalizzato.SEGNALE_LUNGO, (long)(((float) this.spinnerSegnaleLungo.getValue()) * Costanti.DURATA_1_SECONDO));
        profiloUtente.setMappaDurataSegnale(mappaDurataSegnale);
        Map<ETipologiaEventoPersonalizzato, ETipologiaAzionePersonalizzata> mappaComandiPersonalizzati = new HashMap<>();
        mappaComandiPersonalizzati.put(ETipologiaEventoPersonalizzato.SEGNALE_BREVE, (ETipologiaAzionePersonalizzata) this.comboAzioneSegnaleBreve.getSelectedItem());
        mappaComandiPersonalizzati.put(ETipologiaEventoPersonalizzato.SEGNALE_MEDIO, (ETipologiaAzionePersonalizzata) this.comboAzioneSegnaleMedio.getSelectedItem());
        mappaComandiPersonalizzati.put(ETipologiaEventoPersonalizzato.SEGNALE_LUNGO, (ETipologiaAzionePersonalizzata) this.comboAzioneSegnaleLungo.getSelectedItem());
        profiloUtente.setMappaComandiPersonalizzati(mappaComandiPersonalizzati);
        return profiloUtente;
    }
    
    private void formattaSliderDecimali() {      
        Hashtable<Integer, javax.swing.JLabel> labelSensibilita = new Hashtable<>();
        int minSens = sliderSogliaSensibilita.getMinimum();
        int maxSens = sliderSogliaSensibilita.getMaximum();
        int stepSens = sliderSogliaSensibilita.getMajorTickSpacing(); 
        for (int i = minSens; i <= maxSens; i += stepSens) {
            String testo = String.valueOf(i / 10.0f);
            labelSensibilita.put(i, new javax.swing.JLabel(testo));
        }
        sliderSogliaSensibilita.setLabelTable(labelSensibilita);
        sliderSogliaSensibilita.setPaintLabels(true);
        Hashtable<Integer, javax.swing.JLabel> labelZoneMorte = new Hashtable<>();
        int minZone = sliderSogliaZoneMorte.getMinimum();
        int maxZone = sliderSogliaZoneMorte.getMaximum();
        int stepZone = sliderSogliaZoneMorte.getMajorTickSpacing(); 
        for (int i = minZone; i <= maxZone; i += stepZone) {
            String testo = String.valueOf(i / 10.0f);
            labelZoneMorte.put(i, new javax.swing.JLabel(testo));
        }
        sliderSogliaZoneMorte.setLabelTable(labelZoneMorte);
        sliderSogliaZoneMorte.setPaintLabels(true);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.JPanel jPanel2 = new javax.swing.JPanel();
        javax.swing.JLabel jLabel2 = new javax.swing.JLabel();
        sliderSogliaSensibilita = new javax.swing.JSlider();
        javax.swing.JLabel jLabel3 = new javax.swing.JLabel();
        sliderSogliaZoneMorte = new javax.swing.JSlider();
        javax.swing.JSeparator jSeparator1 = new javax.swing.JSeparator();
        javax.swing.JPanel jPanel3 = new javax.swing.JPanel();
        javax.swing.JLabel jLabel4 = new javax.swing.JLabel();
        comboAzioneSegnaleBreve = new javax.swing.JComboBox<>();
        javax.swing.JLabel jLabel5 = new javax.swing.JLabel();
        comboAzioneSegnaleMedio = new javax.swing.JComboBox<>();
        javax.swing.JLabel jLabel6 = new javax.swing.JLabel();
        comboAzioneSegnaleLungo = new javax.swing.JComboBox<>();
        javax.swing.JLabel jLabel10 = new javax.swing.JLabel();
        spinnerSegnaleBreve = new javax.swing.JSpinner();
        spinnerSegnaleMedio = new javax.swing.JSpinner();
        javax.swing.JLabel jLabel11 = new javax.swing.JLabel();
        spinnerSegnaleLungo = new javax.swing.JSpinner();
        javax.swing.JLabel jLabel12 = new javax.swing.JLabel();
        bottoneSalva = new javax.swing.JButton();
        javax.swing.JLabel jLabel7 = new javax.swing.JLabel();
        textFiledNomeProfilo = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Gestione profilo utente");
        setResizable(false);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Soglie"));

        jLabel2.setText("Sensibilità");

        sliderSogliaSensibilita.setMajorTickSpacing(2);
        sliderSogliaSensibilita.setMaximum(20);
        sliderSogliaSensibilita.setMinimum(10);
        sliderSogliaSensibilita.setMinorTickSpacing(1);
        sliderSogliaSensibilita.setPaintTicks(true);
        sliderSogliaSensibilita.setToolTipText("");
        sliderSogliaSensibilita.setValue(10);

        jLabel3.setText("Zone morte");

        sliderSogliaZoneMorte.setMajorTickSpacing(5);
        sliderSogliaZoneMorte.setMaximum(30);
        sliderSogliaZoneMorte.setMinorTickSpacing(1);
        sliderSogliaZoneMorte.setPaintTicks(true);
        sliderSogliaZoneMorte.setValue(10);

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(sliderSogliaSensibilita, javax.swing.GroupLayout.DEFAULT_SIZE, 199, Short.MAX_VALUE)
                    .addComponent(jLabel2))
                .addGap(18, 18, 18)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(sliderSogliaZoneMorte, javax.swing.GroupLayout.DEFAULT_SIZE, 201, Short.MAX_VALUE)))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(sliderSogliaSensibilita, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sliderSogliaZoneMorte, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(36, 36, 36))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jSeparator1)
                .addContainerGap())
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Mappa azioni"));

        jLabel4.setText("Segnale breve");

        jLabel5.setText("Segnale medio");

        jLabel6.setText("Segnale lungo");

        jLabel10.setText("[s]");

        spinnerSegnaleBreve.setModel(new javax.swing.SpinnerNumberModel(Float.valueOf(0.1f), Float.valueOf(0.1f), Float.valueOf(2.9f), Float.valueOf(0.1f)));

        spinnerSegnaleMedio.setModel(new javax.swing.SpinnerNumberModel(Float.valueOf(3.0f), Float.valueOf(3.0f), Float.valueOf(6.9f), Float.valueOf(0.1f)));

        jLabel11.setText("[s]");

        spinnerSegnaleLungo.setModel(new javax.swing.SpinnerNumberModel(Float.valueOf(7.0f), Float.valueOf(7.0f), Float.valueOf(10.0f), Float.valueOf(0.1f)));

        jLabel12.setText("[s]");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(spinnerSegnaleBreve, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel10))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(spinnerSegnaleLungo, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel12)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(comboAzioneSegnaleBreve, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(comboAzioneSegnaleLungo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(spinnerSegnaleMedio, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel11)
                        .addGap(18, 18, 18)
                        .addComponent(comboAzioneSegnaleMedio, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(comboAzioneSegnaleBreve, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addComponent(spinnerSegnaleBreve, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(comboAzioneSegnaleMedio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spinnerSegnaleMedio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(comboAzioneSegnaleLungo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12)
                    .addComponent(spinnerSegnaleLungo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        bottoneSalva.setText("Salva");

        jLabel7.setText("Nome profilo utente");

        textFiledNomeProfilo.setName(""); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(bottoneSalva))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addGap(18, 18, 18)
                        .addComponent(textFiledNomeProfilo, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(textFiledNomeProfilo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, Short.MAX_VALUE)
                .addComponent(bottoneSalva)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bottoneSalva;
    private javax.swing.JComboBox<ETipologiaAzionePersonalizzata> comboAzioneSegnaleBreve;
    private javax.swing.JComboBox<ETipologiaAzionePersonalizzata> comboAzioneSegnaleLungo;
    private javax.swing.JComboBox<ETipologiaAzionePersonalizzata> comboAzioneSegnaleMedio;
    private javax.swing.JSlider sliderSogliaSensibilita;
    private javax.swing.JSlider sliderSogliaZoneMorte;
    private javax.swing.JSpinner spinnerSegnaleBreve;
    private javax.swing.JSpinner spinnerSegnaleLungo;
    private javax.swing.JSpinner spinnerSegnaleMedio;
    private javax.swing.JTextField textFiledNomeProfilo;
    // End of variables declaration//GEN-END:variables
}
