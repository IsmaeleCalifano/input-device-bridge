package it.unibas.inputdevicebridge.vista;

public class VistaInfo extends javax.swing.JDialog {

    public VistaInfo(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
    }

    public void inizializza() {
        initComponents();
        this.setTextEditorPaneInfo();
    }
    
    public void visualizza() {
        this.setLocationRelativeTo(this.getParent());
        this.setVisible(true);
    }
    
    private void setTextEditorPaneInfo() {
        this.editorPaneInfo.setText("""
                <!DOCTYPE html>
                <html>
                  <head>
                    <style>
                      body {
                        font-family: "Segoe UI", sans-serif;
                        padding: 0 15px;
                      }
                    </style>
                  </head>
                  <body>
                    <h2><b>INPUT DEVICE BRIDGE</b></h2>
                    <p>
                    Questa applicazione è un middleware d'integrazione che funge da ponte invisibile tra dispositivi non standard 
                    e applicazioni. Il software intercetta i segnali dei dispositivi e li traduce in tempo reale in eventi semantici 
                    (Click, Drag, Scroll, KeyPress) comprensibili dal sistema operativo.
                    </p>
                    <p>L'interfaccia permette di configurare il ponte:</p>
                    <ul>
                        <li>Pagina principale: monitoraggio in tempo reale con scelta del dispositivo e del profilo utente;</li>
                        <li>Gestione profilo: pannello di configurazione per associare segnali fisici ad azioni logiche;</li>
                        <li>Area di Test: zona "sandbox" per provare le configurazioni prima dell'uso reale.</li>
                    </ul>
                  </body>
                </html>
        """);
    }

    

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.JScrollPane jScrollPane2 = new javax.swing.JScrollPane();
        editorPaneInfo = new javax.swing.JEditorPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Info");
        setPreferredSize(new java.awt.Dimension(500, 400));
        setResizable(false);

        editorPaneInfo.setEditable(false);
        editorPaneInfo.setContentType("text/html"); // NOI18N
        jScrollPane2.setViewportView(editorPaneInfo);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JEditorPane editorPaneInfo;
    // End of variables declaration//GEN-END:variables
}
