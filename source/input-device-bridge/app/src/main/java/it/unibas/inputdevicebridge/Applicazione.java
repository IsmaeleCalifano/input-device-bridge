package it.unibas.inputdevicebridge;

import it.unibas.inputdevicebridge.controllo.ControlloAreaTest;
import it.unibas.inputdevicebridge.controllo.ControlloGestioneProfilo;
import it.unibas.inputdevicebridge.controllo.ControlloMenu;
import it.unibas.inputdevicebridge.controllo.ControlloPrincipale;
import it.unibas.inputdevicebridge.modello.Costanti;
import it.unibas.inputdevicebridge.modello.DeviceBridgeFacade;
import it.unibas.inputdevicebridge.modello.Modello;
import it.unibas.inputdevicebridge.modello.profilo_utente.ArchivioProfiliUtente;
import it.unibas.inputdevicebridge.modello.profilo_utente.ProfiloUtente;
import it.unibas.inputdevicebridge.persistenza.DAOArchivioProfiliUtenteMock;
import it.unibas.inputdevicebridge.persistenza.DAOException;
import it.unibas.inputdevicebridge.vista.Frame;
import it.unibas.inputdevicebridge.vista.VistaPrincipale;
import javax.swing.SwingUtilities;
import lombok.Getter;
import it.unibas.inputdevicebridge.persistenza.IDAOArchivioProfiliUtente;
import it.unibas.inputdevicebridge.vista.VistaAreaTest;
import it.unibas.inputdevicebridge.vista.VistaGestioneProfilo;
import it.unibas.inputdevicebridge.vista.VistaInfo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class Applicazione {
    
    private static final Applicazione singleton = new Applicazione();
    
    public static Applicazione getInstance() {
        return singleton;
    }
    
    private Applicazione() {}
    
    private final Modello modello = new Modello();
    private final DeviceBridgeFacade deviceBridge = new DeviceBridgeFacade();
    private final IDAOArchivioProfiliUtente daoArchivioProfiliUtente = new DAOArchivioProfiliUtenteMock();
    private final Frame frame = new Frame();
    private final VistaPrincipale vistaPrincipale = new VistaPrincipale();
    private final VistaGestioneProfilo vistaGestioneProfilo = new VistaGestioneProfilo(this.frame, true);
    private final VistaAreaTest vistaAreaTest = new VistaAreaTest(this.frame, true);
    private final VistaInfo vistaInfo = new VistaInfo(this.frame, true);
    private final ControlloMenu controlloMenu = new ControlloMenu();
    private final ControlloPrincipale controlloPrincipale = new ControlloPrincipale();
    private final ControlloGestioneProfilo controlloGestioneProfilo = new ControlloGestioneProfilo();
    private final ControlloAreaTest controlloAreaTest = new ControlloAreaTest();
    
    private void inizializza() {
        this.inizializzaArchivio();
        this.controlloMenu.inizializza();
        this.controlloPrincipale.inizializza();
        this.vistaInfo.inizializza();
        this.vistaAreaTest.inizializza();
        this.vistaGestioneProfilo.inizializza();
        this.vistaPrincipale.inizializza();
        this.frame.inizializza();
    }
    
    private void inizializzaArchivio() {
        try {
            ArchivioProfiliUtente archivioProfiliUtente = this.daoArchivioProfiliUtente.caricaArchivioProfiliUtente("");
            this.modello.putBean(Costanti.ARCHIVIO_PROFILI_UTENTE, archivioProfiliUtente);
            ProfiloUtente profiloUtente = archivioProfiliUtente.getProfiloUtentePerIndice(0);
            if (profiloUtente != null) {
                this.modello.putBean(Costanti.PROFILO_UTENTE_SELEZIONATO, profiloUtente);
                this.deviceBridge.applicaProfiloUtente(profiloUtente);
            }
            log.debug("Archivio caricato correttamente con {} profili utente!", archivioProfiliUtente.getListaProfiliUtente().size());
        } catch (DAOException ex) {
            this.modello.putBean(Costanti.ARCHIVIO_PROFILI_UTENTE, new ArchivioProfiliUtente());
            log.debug("Errore durante il caricamento dell'archivio!");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run() {
                Applicazione.getInstance().inizializza();
            }
        });
    }
    
}
