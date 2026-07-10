package it.unibas.inputdevicebridge;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import it.unibas.inputdevicebridge.controllo.ControlloMenu;
import it.unibas.inputdevicebridge.controllo.ControlloPrincipale;
import it.unibas.inputdevicebridge.modello.Costanti;
import it.unibas.inputdevicebridge.modello.DeviceBridgeFacade;
import it.unibas.inputdevicebridge.modello.Modello;
import it.unibas.inputdevicebridge.modello.profilo_utente.ArchivioProfiliUtente;
import it.unibas.inputdevicebridge.modello.profilo_utente.ProfiloUtente;
import it.unibas.inputdevicebridge.persistenza.DAOException;
import it.unibas.inputdevicebridge.vista.Frame;
import javax.swing.SwingUtilities;
import lombok.Getter;
import it.unibas.inputdevicebridge.persistenza.IDAOArchivioProfiliUtente;
import it.unibas.inputdevicebridge.vista.WindowDurataSegnale;
import it.unibas.inputdevicebridge.vista.VistaAreaTest;
import it.unibas.inputdevicebridge.vista.VistaCalibrazione;
import it.unibas.inputdevicebridge.vista.VistaCalibrazioneClick;
import it.unibas.inputdevicebridge.vista.VistaCalibrazioneDoppioClick;
import it.unibas.inputdevicebridge.vista.VistaCalibrazioneScroll;
import it.unibas.inputdevicebridge.vista.VistaCalibrazioneTrascinamento;
import it.unibas.inputdevicebridge.vista.VistaDurataSegnale;
import it.unibas.inputdevicebridge.vista.VistaGestioneProfilo;
import it.unibas.inputdevicebridge.vista.VistaInfo;
import it.unibas.inputdevicebridge.vista.VistaPrincipale;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@QuarkusMain
public class Applicazione implements QuarkusApplication {
 
    @Inject private Modello modello;
    @Inject private DeviceBridgeFacade deviceBridgeFacade;
    @Inject private IDAOArchivioProfiliUtente daoArchivioProfiliUtente;
    @Inject private ControlloMenu controlloMenu;
    @Inject private ControlloPrincipale controlloPrincipale;
    @Inject private Frame frame;
    @Inject private WindowDurataSegnale windowDurataSegnale;
    @Inject private VistaDurataSegnale VistaDurataSegnale;
    @Inject private VistaPrincipale vistaPrincipale;
    @Inject private VistaGestioneProfilo vistaGestioneProfilo;
    @Inject private VistaAreaTest vistaAreaTest;
    @Inject private VistaInfo vistaInfo;
    @Inject private VistaCalibrazione vistaCalibrazione;
    @Inject private VistaCalibrazioneClick vistaCalibrazioneClick;
    @Inject private VistaCalibrazioneDoppioClick vistaCalibrazioneDoppioClick;
    @Inject private VistaCalibrazioneScroll vistaCalibrazioneScroll;
    @Inject private VistaCalibrazioneTrascinamento vistaCalibrazioneTrascinamento;

    private void inizializza() {
        this.inizializzaArchivio();
        this.controlloMenu.inizializza();
        this.controlloPrincipale.inizializza();
        this.vistaCalibrazioneTrascinamento.inizializza();
        this.vistaCalibrazioneScroll.inizializza();
        this.vistaCalibrazioneClick.inizializza();
        this.vistaCalibrazioneDoppioClick.inizializza();
        this.vistaCalibrazione.inizializza();
        this.vistaInfo.inizializza();
        this.vistaAreaTest.inizializza();
        this.vistaGestioneProfilo.inizializza();
        this.vistaPrincipale.inizializza();
        this.VistaDurataSegnale.inizializza();
        this.windowDurataSegnale.inizializza();
        this.frame.inizializza();
    }

    private void inizializzaArchivio() {
        try {
            ArchivioProfiliUtente archivioProfiliUtente = this.daoArchivioProfiliUtente.caricaArchivioProfiliUtente();
            this.modello.putBean(Costanti.ARCHIVIO_PROFILI_UTENTE, archivioProfiliUtente);
            ProfiloUtente profiloUtente = archivioProfiliUtente.getProfiloUtentePerIndice(0);
            if (profiloUtente != null) {
                this.modello.putBean(Costanti.PROFILO_UTENTE_SELEZIONATO, profiloUtente);
                this.deviceBridgeFacade.applicaProfiloUtente(profiloUtente);
            }
            log.debug("Archivio caricato correttamente con {} profili utente!", archivioProfiliUtente.size());
        } catch (DAOException ex) {
            this.modello.putBean(Costanti.ARCHIVIO_PROFILI_UTENTE, new ArchivioProfiliUtente());
            log.debug("Errore durante il caricamento dell'archivio!");
        }
    }

    public static void main(String[] args) {
        Quarkus.run(Applicazione.class, args);
    }

    @Override
    public int run(String... args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Applicazione.this.inizializza();
            }
        });
        Quarkus.waitForExit();
        return 0;
    }

}
