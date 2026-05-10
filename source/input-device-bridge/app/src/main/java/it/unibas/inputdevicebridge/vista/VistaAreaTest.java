package it.unibas.inputdevicebridge.vista;

import it.unibas.inputdevicebridge.Applicazione;
import lombok.Getter;
import lombok.Setter;


public class VistaAreaTest extends javax.swing.JDialog {
    
    @Getter
    @Setter
    private int numeroClick;

    public VistaAreaTest(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        this.numeroClick = 0;
    }

    public void inizializza() {
        initComponents();
        this.bottoneClicca.setAction(Applicazione.getInstance().getControlloAreaTest().getAzioneClicca());
    }

    public void visualizza() {
        this.numeroClick = 0;
        this.aggiornaLabelNumeroClick();
        this.setLocationRelativeTo(this.getParent());
        this.setVisible(true);
    }
    
    public void aggiornaLabelNumeroClick() {
        this.labelNumeroClick.setText("Click effettuati: " + this.numeroClick);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.JTabbedPane jTabbedPane1 = new javax.swing.JTabbedPane();
        javax.swing.JPanel jPanel1 = new javax.swing.JPanel();
        javax.swing.JLabel jLabel3 = new javax.swing.JLabel();
        bottoneClicca = new javax.swing.JButton();
        labelNumeroClick = new javax.swing.JLabel();
        javax.swing.JPanel jPanel2 = new javax.swing.JPanel();
        javax.swing.JLabel jLabel2 = new javax.swing.JLabel();
        javax.swing.JScrollPane jScrollPane4 = new javax.swing.JScrollPane();
        javax.swing.JTextPane jTextPane2 = new javax.swing.JTextPane();
        javax.swing.JPanel jPanel3 = new javax.swing.JPanel();
        javax.swing.JLabel jLabel1 = new javax.swing.JLabel();
        javax.swing.JScrollPane jScrollPane1 = new javax.swing.JScrollPane();
        javax.swing.JTextPane jTextPane3 = new javax.swing.JTextPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Area test");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        jTabbedPane1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jTabbedPane1.setPreferredSize(new java.awt.Dimension(400, 325));

        jLabel3.setText("Clicca il bottone per effettuare il test.");

        bottoneClicca.setText("Clicca");

        labelNumeroClick.setText("Click effettuati: 0");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(bottoneClicca)
                    .addComponent(labelNumeroClick))
                .addContainerGap(195, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel3)
                .addGap(18, 18, 18)
                .addComponent(bottoneClicca)
                .addGap(18, 18, 18)
                .addComponent(labelNumeroClick)
                .addContainerGap(181, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Test click", jPanel1);

        jLabel2.setText("Effettua lo scroll nell'area sottostante");

        jTextPane2.setEditable(false);
        jTextPane2.setText("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Praesent at tortor pulvinar, finibus ante ac, viverra diam. Aenean sagittis est at nisl condimentum scelerisque. Duis ultrices egestas nisl efficitur eleifend. Phasellus varius, urna sed semper venenatis, ante libero faucibus mauris, eget aliquam risus nunc id purus. Ut ipsum nunc, ornare consequat tempus non, cursus vitae risus. Etiam a nunc vel nibh gravida auctor. Aliquam erat volutpat. Donec eget ipsum nec nisl egestas faucibus. Mauris in hendrerit lectus.\n\nAenean fringilla aliquet mauris at euismod. Integer varius sollicitudin velit non finibus. Pellentesque sodales sit amet enim sed dignissim. Nam faucibus nisl pretium, ultricies arcu at, suscipit lacus. Donec varius enim et blandit ultrices. In ut euismod erat, vitae mattis nisl. Maecenas sed mi dignissim, mollis ante sed, ultricies dolor. In nec leo convallis dolor sodales consequat.\n\nNulla et ultricies nisi. Nunc non odio vitae erat tempor vestibulum non in eros. Pellentesque est nulla, efficitur nec venenatis id, viverra sed leo. Pellentesque nec rhoncus felis, vitae semper sapien. Donec congue orci sit amet consequat mattis. Sed cursus cursus tellus. Ut at massa placerat, tempor massa vel, eleifend elit. Integer a metus ante. Nulla vehicula at magna eu blandit. Duis nec tincidunt urna. Maecenas non arcu in felis venenatis pulvinar. Aliquam eu tempus augue. Morbi felis lacus, tristique in massa at, pharetra rhoncus erat. Aenean vehicula arcu ac pretium placerat. Donec faucibus erat metus, et pellentesque sem faucibus nec. Aliquam quis enim a sapien pharetra suscipit.\n\nOrci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Suspendisse lacus justo, consectetur non metus consectetur, varius laoreet sapien. Nullam eget orci arcu. Donec tortor lectus, ornare ut condimentum eget, elementum sit amet nisl. Nunc quis enim posuere arcu ullamcorper tempor sit amet sed ligula. Curabitur eleifend eget risus id faucibus. Nam leo dui, cursus id ex et, feugiat aliquam lacus. Ut quis tincidunt dui. Nam sed nisl vel mauris congue placerat. Aenean ut velit suscipit, interdum tortor venenatis, facilisis quam. Donec tortor magna, cursus et est eget, maximus sollicitudin risus.\n\nPhasellus justo justo, feugiat eget tincidunt quis, vestibulum quis purus. Morbi consequat venenatis hendrerit. Proin interdum vitae augue at iaculis. Proin ut ex erat. Donec ut fermentum ipsum, vel mollis elit. Nunc viverra metus vitae interdum ullamcorper. Pellentesque in lectus mollis, venenatis nulla at, tincidunt massa.\n\nVestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae; Ut sodales in quam ac tincidunt. Nunc et euismod sem. Aenean a velit sollicitudin justo commodo viverra sed sed nulla. Curabitur sapien orci, accumsan in odio ut, scelerisque luctus velit. Duis eget porta odio. Cras nisi tortor, elementum sit amet lacus quis, bibendum congue diam. Fusce euismod sed est in dapibus. Vestibulum eu ante nec mauris consectetur lacinia. Aliquam tristique, ipsum at interdum porta, leo est eleifend tortor, sed pulvinar felis lorem at orci. Curabitur vestibulum erat a tellus ultricies, at pharetra neque posuere.\n\nEtiam elit eros, finibus at purus sit amet, dignissim mattis tellus. Maecenas accumsan ligula eget neque ullamcorper lobortis non non nisl. Morbi congue ex nec diam congue, maximus laoreet eros placerat. In ut leo commodo, fermentum purus sit amet, auctor sem. Donec nec sem sed dui viverra venenatis. Nullam enim arcu, fringilla nec urna vel, finibus egestas velit. Donec eros turpis, pellentesque vitae tempus et, egestas sed augue. Aenean mattis nisl ut erat suscipit, eu aliquet nulla molestie. Ut in augue a massa ultricies auctor.\n\nAenean et ultrices sem. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. In hac habitasse platea dictumst. Integer suscipit elit dui, sit amet efficitur odio pretium ut. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Sed facilisis dignissim interdum. Donec vel elementum ligula. Integer eget lacinia purus. Sed feugiat lorem quam, eu tempus est finibus a.\n\nCras ac purus eu augue auctor lacinia. Aenean tincidunt risus lorem, at suscipit ante luctus at. Quisque vestibulum molestie arcu id cursus. Curabitur varius augue orci, non ornare nunc sodales vitae. Donec felis sem, posuere eu rutrum at, finibus id elit. Fusce nec egestas lorem. In maximus convallis augue eu scelerisque. Ut convallis eu massa vitae varius. In eget pretium ligula.\n\nAenean eu tellus lorem. Proin facilisis nibh dictum, tristique arcu et, convallis leo. Vivamus tristique enim a pulvinar convallis. Aliquam erat volutpat. Morbi viverra sapien risus, vel dictum odio auctor eget. Etiam mi eros, bibendum et dictum at, condimentum at odio. Etiam facilisis diam at libero dignissim, sit amet imperdiet tellus ultrices.");
        jScrollPane4.setViewportView(jTextPane2);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 381, Short.MAX_VALUE))
                .addContainerGap(7, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 242, Short.MAX_VALUE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Test scroll", jPanel2);

        jLabel1.setText("Seleziona il testo sottostante.");

        jTextPane3.setEditable(false);
        jTextPane3.setText("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec quis enim ut tellus ullamcorper tristique quis ac nulla. In sodales sit amet lacus at molestie. Mauris vitae turpis est. Nunc placerat, felis vel tincidunt elementum, velit est bibendum massa, consequat congue velit ante vitae dui. Vestibulum fermentum pharetra varius. Mauris blandit placerat finibus. In purus dolor, consequat a molestie sit amet, laoreet nec elit. Fusce sagittis viverra leo, malesuada imperdiet lectus lacinia vitae.");
        jScrollPane1.setViewportView(jTextPane3);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 228, Short.MAX_VALUE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 242, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Test trascinamento", jPanel3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 394, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 319, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        Applicazione.getInstance().getControlloPrincipale().getAzioneFerma().actionPerformed(null);
    }//GEN-LAST:event_formWindowClosed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bottoneClicca;
    private javax.swing.JLabel labelNumeroClick;
    // End of variables declaration//GEN-END:variables
}
