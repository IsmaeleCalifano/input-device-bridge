package it.unibas.inputdevicebridge.controllo;

import it.unibas.inputdevicebridge.Applicazione;
import it.unibas.inputdevicebridge.modello.Costanti;
import it.unibas.inputdevicebridge.modello.input_device.EyeTrackerStrategy;
import it.unibas.inputdevicebridge.modello.input_device.IInputDeviceStrategy;
import it.unibas.inputdevicebridge.modello.input_device.SwitchStrategy;
import it.unibas.inputdevicebridge.modello.profilo_utente.ArchivioProfiliUtente;
import it.unibas.inputdevicebridge.modello.profilo_utente.ProfiloUtente;
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
public class ControlloPrincipale {

    private final Action azioneAggiornaProfiloSelezionato = new AzioneAggiornaProfiloSelezionato();
    private final Action azioneAvvia = new AzioneAvvia();
    private final Action azioneFerma = new AzioneFerma();
    private final ActionListener azioneAggiornaDispositivoSelezionato = new AzioneAggiornaDispositivoSelezionato();

    public void inizializza() {
        this.inizializzaAzioneAggiornaProfiloSelezionato();
    }

    private void inizializzaAzioneAggiornaProfiloSelezionato() {
        ArchivioProfiliUtente archivioProfiliUtente = (ArchivioProfiliUtente) Applicazione.getInstance().getModello().getBean(Costanti.ARCHIVIO_PROFILI_UTENTE);
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
            String nomeProfiloUtenteSelezionato = Applicazione.getInstance().getVistaPrincipale().getTextItemSelezionataComboProfili();
            ArchivioProfiliUtente archivioProfiliUtente = (ArchivioProfiliUtente) Applicazione.getInstance().getModello().getBean(Costanti.ARCHIVIO_PROFILI_UTENTE);
            ProfiloUtente profiloUtenteSelezionato = archivioProfiliUtente.getProfiloUtentePerNome(nomeProfiloUtenteSelezionato);
            if (profiloUtenteSelezionato != null) {
                Applicazione.getInstance().getModello().putBean(Costanti.PROFILO_UTENTE_SELEZIONATO, profiloUtenteSelezionato);
                Applicazione.getInstance().getDeviceBridge().applicaProfiloUtente(profiloUtenteSelezionato);
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
            ProfiloUtente profiloUtente = (ProfiloUtente) Applicazione.getInstance().getModello().getBean(Costanti.PROFILO_UTENTE_SELEZIONATO);
            if (profiloUtente == null) {
                Applicazione.getInstance().getFrame().mostraMessaggioErrori("Seleziona o crea un profilo prima di avviare!");
                return;
            }
            Applicazione.getInstance().getVistaPrincipale().aggiornaStatoEsecuzione(true);
            Applicazione.getInstance().getControlloMenu().getAzioneAreaTest().setEnabled(false);
            log.debug("Applicazione in esecuzione!");
            Thread threadInputDeviceBridge = new Thread(new Runnable() {
                @Override
                public void run() {
                    Applicazione.getInstance().getDeviceBridge().esegui();
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
            Applicazione.getInstance().getVistaPrincipale().aggiornaStatoEsecuzione(false);
            Applicazione.getInstance().getDeviceBridge().fermaEsecuzione();
            Applicazione.getInstance().getControlloMenu().getAzioneAreaTest().setEnabled(true);
            log.debug("Esecuzione applicazione fermata!");
        }

    }

    private class AzioneAggiornaDispositivoSelezionato implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String nomeDispositivoSlezionato = Applicazione.getInstance().getVistaPrincipale().getTextRadioDispositiviSelezionato();
            IInputDeviceStrategy device = null;
            if (nomeDispositivoSlezionato.equals(EyeTrackerStrategy.class.getSimpleName())) {
                device = new EyeTrackerStrategy();
            } else if (nomeDispositivoSlezionato.equals(SwitchStrategy.class.getSimpleName())) {
                device = new SwitchStrategy();
            }
            if (device != null) {
                log.debug("Dispositivo selezionato: {}.", nomeDispositivoSlezionato);
                Applicazione.getInstance().getDeviceBridge().setDevice(device);
            }
        }

    }

}
