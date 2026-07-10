package it.unibas.inputdevicebridge.vista;

import it.unibas.inputdevicebridge.controllo.ControlloMenu;
import it.unibas.inputdevicebridge.modello.Costanti;
import it.unibas.inputdevicebridge.modello.Modello;
import it.unibas.inputdevicebridge.modello.profilo_utente.ArchivioProfiliUtente;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

@Singleton
public class Frame extends javax.swing.JFrame {
    
    static{
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
        }
    }
    
    @Inject
    private VistaPrincipale vistaPrincipale;
    @Inject
    private ControlloMenu controlloMenu;
    @Inject
    private Modello modello;

    public void inizializza() {
        initComponents();
        this.setContentPane(new JScrollPane(this.vistaPrincipale));
        this.menuNuovoProfilo.setAction(this.controlloMenu.getAzioneNuovoProfilo());
        Action azioneGestioneProfilo = this.controlloMenu.getAzioneGestioneProfilo();
        ArchivioProfiliUtente archivioProfiliUtente = (ArchivioProfiliUtente) this.modello.getBean(Costanti.ARCHIVIO_PROFILI_UTENTE);
        azioneGestioneProfilo.setEnabled(!archivioProfiliUtente.getListaProfiliUtente().isEmpty());
        this.menuGestioneProfilo.setAction(azioneGestioneProfilo);
        this.menuEsci.setAction(this.controlloMenu.getAzioneEsci());
        this.menuAreaTest.setAction(this.controlloMenu.getAzioneAreaTest());
        this.menuInfo.setAction(this.controlloMenu.getAzioneInfo());
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
    
    public void mostraMessaggio(String messaggio){
        JOptionPane.showMessageDialog(this, messaggio, "Input Device Bridge", JOptionPane.INFORMATION_MESSAGE);
   }
    
   public void mostraMessaggioErrori(String messaggio){
       JOptionPane.showMessageDialog(this, messaggio, "Errore", JOptionPane.ERROR_MESSAGE);
   }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.JMenuBar jMenuBar1 = new javax.swing.JMenuBar();
        javax.swing.JMenu jMenu1 = new javax.swing.JMenu();
        menuNuovoProfilo = new javax.swing.JMenuItem();
        menuGestioneProfilo = new javax.swing.JMenuItem();
        javax.swing.JPopupMenu.Separator jSeparator1 = new javax.swing.JPopupMenu.Separator();
        menuEsci = new javax.swing.JMenuItem();
        javax.swing.JMenu jMenu2 = new javax.swing.JMenu();
        menuAreaTest = new javax.swing.JMenuItem();
        javax.swing.JMenu JMenu3 = new javax.swing.JMenu();
        menuInfo = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Input Device Bridge");
        setResizable(false);

        jMenu1.setText("File");

        menuNuovoProfilo.setText("Nuovo profilo");
        jMenu1.add(menuNuovoProfilo);

        menuGestioneProfilo.setText("Gestione profilo");
        jMenu1.add(menuGestioneProfilo);
        jMenu1.add(jSeparator1);

        menuEsci.setText("Esci");
        jMenu1.add(menuEsci);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Test");

        menuAreaTest.setText("Area test");
        jMenu2.add(menuAreaTest);

        jMenuBar1.add(jMenu2);

        JMenu3.setText("?");

        menuInfo.setText("Info");
        JMenu3.add(menuInfo);

        jMenuBar1.add(JMenu3);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 277, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem menuAreaTest;
    private javax.swing.JMenuItem menuEsci;
    private javax.swing.JMenuItem menuGestioneProfilo;
    private javax.swing.JMenuItem menuInfo;
    private javax.swing.JMenuItem menuNuovoProfilo;
    // End of variables declaration//GEN-END:variables
}
