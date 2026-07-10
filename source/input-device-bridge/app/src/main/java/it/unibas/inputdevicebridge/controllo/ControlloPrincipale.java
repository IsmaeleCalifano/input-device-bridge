package it.unibas.inputdevicebridge.controllo;

import it.unibas.inputdevicebridge.modello.Costanti;
import it.unibas.inputdevicebridge.modello.DeviceBridgeFacade;
import it.unibas.inputdevicebridge.modello.Modello;
import it.unibas.inputdevicebridge.modello.input_device.EyeTrackerStrategy;
import it.unibas.inputdevicebridge.modello.input_device.HeadPointerStrategy;
import it.unibas.inputdevicebridge.modello.input_device.IInputDeviceStrategy;
import it.unibas.inputdevicebridge.modello.input_device.SipAndPuffStrategy;
import it.unibas.inputdevicebridge.modello.input_device.SwitchStrategy;
import it.unibas.inputdevicebridge.modello.interprete.AttesaState;
import it.unibas.inputdevicebridge.modello.profilo_utente.ArchivioProfiliUtente;
import it.unibas.inputdevicebridge.modello.profilo_utente.ProfiloUtente;
import it.unibas.inputdevicebridge.vista.Frame;
import it.unibas.inputdevicebridge.vista.VistaPrincipale;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import static javax.swing.Action.ACCELERATOR_KEY;
import static javax.swing.Action.MNEMONIC_KEY;
import javax.swing.KeyStroke;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@ApplicationScoped
public class ControlloPrincipale {

    private final Modello modello;
    private final DeviceBridgeFacade deviceBridgeFacade;
    private final Frame frame;
    private final VistaPrincipale vistaPrincipale;
    private final ControlloMenu controlloMenu;

    private Thread threadInputDeviceBridge;

    private final Action azioneAggiornaProfiloSelezionato = new AzioneAggiornaProfiloSelezionato();
    private final Action azioneAvvia = new AzioneAvvia();
    private final Action azioneFerma = new AzioneFerma();
    private final ActionListener azioneAggiornaDispositivoSelezionato = new AzioneAggiornaDispositivoSelezionato();

    @Inject
    public ControlloPrincipale(Modello modello, DeviceBridgeFacade deviceBridgeFacade, Frame frame, VistaPrincipale vistaPrincipale, ControlloMenu controlloMenu) {
        this.modello = modello;
        this.deviceBridgeFacade = deviceBridgeFacade;
        this.frame = frame;
        this.vistaPrincipale = vistaPrincipale;
        this.controlloMenu = controlloMenu;
    }

    public void inizializza() {
        this.inizializzaAzioneAggiornaProfiloSelezionato();
    }

    private void inizializzaAzioneAggiornaProfiloSelezionato() {
        ArchivioProfiliUtente archivioProfiliUtente = (ArchivioProfiliUtente) this.modello.getBean(Costanti.ARCHIVIO_PROFILI_UTENTE);
        this.azioneAggiornaProfiloSelezionato.setEnabled(!archivioProfiliUtente.getListaProfiliUtente().isEmpty());
    }

    private class AzioneAggiornaProfiloSelezionato extends AbstractAction {

        public AzioneAggiornaProfiloSelezionato() {
            this.putValue(Action.NAME, "Aggiorna profilo selezionato");
            this.putValue(Action.SHORT_DESCRIPTION, "Aggiorna i dati visualizzati del profilo utente selezionato");
            this.putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl R"));
            this.putValue(MNEMONIC_KEY, KeyEvent.VK_R);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String nomeProfiloUtenteSelezionato = vistaPrincipale.getTextItemSelezionataComboProfili();
            ArchivioProfiliUtente archivioProfiliUtente = (ArchivioProfiliUtente) modello.getBean(Costanti.ARCHIVIO_PROFILI_UTENTE);
            ProfiloUtente profiloUtenteSelezionato = archivioProfiliUtente.getProfiloUtentePerNome(nomeProfiloUtenteSelezionato);
            log.debug("Nome profiloutente selezionato: {}", profiloUtenteSelezionato.getNome());
            if (profiloUtenteSelezionato != null) {
                modello.putBean(Costanti.PROFILO_UTENTE_SELEZIONATO, profiloUtenteSelezionato);
                deviceBridgeFacade.applicaProfiloUtente(profiloUtenteSelezionato);
            }
        }

    }

    private class AzioneAvvia extends AbstractAction {

        public AzioneAvvia() {
            this.putValue(Action.NAME, "Avvia");
            this.putValue(Action.SHORT_DESCRIPTION, "Avvia l'applicazione");
            this.putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl alt A"));
            this.putValue(MNEMONIC_KEY, KeyEvent.VK_A);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (threadInputDeviceBridge != null && threadInputDeviceBridge.isAlive()) {
                return;
            }
            ProfiloUtente profiloUtente = (ProfiloUtente) modello.getBean(Costanti.PROFILO_UTENTE_TEMPORANEO);
            if (profiloUtente == null) {
                profiloUtente = (ProfiloUtente) modello.getBean(Costanti.PROFILO_UTENTE_SELEZIONATO);
                if (profiloUtente == null) {
                    frame.mostraMessaggioErrori("Seleziona o crea un profilo prima di avviare!");
                    return;
                }
            }
            vistaPrincipale.aggiornaStatoEsecuzione(true);
            controlloMenu.getAzioneAreaTest().setEnabled(false);
            log.debug("Applicazione in esecuzione!");
            threadInputDeviceBridge = new Thread(new Runnable() {
                @Override
                public void run() {
                    deviceBridgeFacade.esegui();
                }
            });
            threadInputDeviceBridge.start();
        }

    }

    private class AzioneFerma extends AbstractAction {

        public AzioneFerma() {
            this.putValue(Action.NAME, "Stop");
            this.putValue(Action.SHORT_DESCRIPTION, "Ferma l'esecuzione dell'aplicazione");
            this.putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl F"));
            this.putValue(MNEMONIC_KEY, KeyEvent.VK_F);
            setEnabled(false);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            vistaPrincipale.aggiornaStatoEsecuzione(false);
            deviceBridgeFacade.fermaEsecuzione();
            controlloMenu.getAzioneAreaTest().setEnabled(true);
            deviceBridgeFacade.getInterprete().setStatoCorrente(new AttesaState());
            log.debug("Esecuzione applicazione fermata!");
        }

    }

    private class AzioneAggiornaDispositivoSelezionato implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String nomeDispositivoSlezionato = vistaPrincipale.getTextRadioDispositiviSelezionato();
            IInputDeviceStrategy device = null;
            if (nomeDispositivoSlezionato.equals(EyeTrackerStrategy.class.getSimpleName())) {
                device = new EyeTrackerStrategy();
            } else if (nomeDispositivoSlezionato.equals(HeadPointerStrategy.class.getSimpleName())) {
                device = new HeadPointerStrategy();
            } else if (nomeDispositivoSlezionato.equals(SwitchStrategy.class.getSimpleName())) {
                device = new SwitchStrategy();
            } else if (nomeDispositivoSlezionato.equals(SipAndPuffStrategy.class.getSimpleName())) {
                device = new SipAndPuffStrategy();
            }
            if (device != null) {
                log.debug("Dispositivo selezionato: {}.", nomeDispositivoSlezionato);
                deviceBridgeFacade.setDevice(device);
            }
        }

    }

}
