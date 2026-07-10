package it.unibas.inputdevicebridge.controllo;

import it.unibas.inputdevicebridge.modello.Costanti;
import it.unibas.inputdevicebridge.modello.Modello;
import it.unibas.inputdevicebridge.modello.profilo_utente.ArchivioProfiliUtente;
import it.unibas.inputdevicebridge.modello.profilo_utente.ProfiloUtente;
import it.unibas.inputdevicebridge.vista.Frame;
import it.unibas.inputdevicebridge.vista.VistaAreaTest;
import it.unibas.inputdevicebridge.vista.VistaGestioneProfilo;
import it.unibas.inputdevicebridge.vista.VistaInfo;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;
import lombok.Getter;

@Getter
@ApplicationScoped
public class ControlloMenu {

    private final Modello modello;
    private final Frame frame;
    private final VistaGestioneProfilo vistaGestioneProfilo;
    private final VistaAreaTest vistaAreaTest;
    private final VistaInfo vistaInfo;
    private final ControlloPrincipale controlloPrincipale;

    private final Action azioneEsci = new AzioneEsci();
    private final Action azioneNuovoProfilo = new AzioneNuovoProfilo();
    private final Action azioneGestioneProfilo = new AzioneGestioneProfilo();
    private final Action azioneAreaTest = new AzioneAreaTest();
    private final Action azioneInfo = new AzioneInfo();

    @Inject
    public ControlloMenu(Modello modello, Frame frame, VistaGestioneProfilo vistaGestioneProfilo, VistaAreaTest vistaAreaTest, VistaInfo vistaInfo, ControlloPrincipale controlloPrincipale) {
        this.modello = modello;
        this.frame = frame;
        this.vistaGestioneProfilo = vistaGestioneProfilo;
        this.vistaAreaTest = vistaAreaTest;
        this.vistaInfo = vistaInfo;
        this.controlloPrincipale = controlloPrincipale;
    }

    public void inizializza() {
        this.inizializzaAzioneGestioneProfilo();
    }

    private void inizializzaAzioneGestioneProfilo() {
        ArchivioProfiliUtente archivioProfiliUtente = (ArchivioProfiliUtente) this.modello.getBean(Costanti.ARCHIVIO_PROFILI_UTENTE);
        this.azioneGestioneProfilo.setEnabled(!archivioProfiliUtente.getListaProfiliUtente().isEmpty());
    }

    private class AzioneEsci extends AbstractAction {

        public AzioneEsci() {
            this.putValue(Action.NAME, "Esci");
            this.putValue(Action.SHORT_DESCRIPTION, "Esci dall'applicazione");
            this.putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl W"));
            this.putValue(MNEMONIC_KEY, KeyEvent.VK_E);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            controlloPrincipale.getAzioneFerma().actionPerformed(null);
            Thread threadInputDeviceBridge = controlloPrincipale.getThreadInputDeviceBridge();
            if (threadInputDeviceBridge != null) {
                try {
                    threadInputDeviceBridge.join(500);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
            System.exit(0);
        }
    }

    private class AzioneNuovoProfilo extends AbstractAction {

        public AzioneNuovoProfilo() {
            this.putValue(Action.NAME, "Nuovo profilo");
            this.putValue(Action.SHORT_DESCRIPTION, "Crea un nuovo profilo utente");
            this.putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl N"));
            this.putValue(MNEMONIC_KEY, KeyEvent.VK_N);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            vistaGestioneProfilo.visualizzaNuovo();
        }

    }

    private class AzioneGestioneProfilo extends AbstractAction {

        public AzioneGestioneProfilo() {
            this.putValue(Action.NAME, "Gestione profilo");
            this.putValue(Action.SHORT_DESCRIPTION, "Gestione delle proprietà del profilo utente");
            this.putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl M"));
            this.putValue(MNEMONIC_KEY, KeyEvent.VK_M);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            vistaGestioneProfilo.visualizzaModifica();
        }

    }

    private class AzioneAreaTest extends AbstractAction {

        public AzioneAreaTest() {
            this.putValue(Action.NAME, "Area test");
            this.putValue(Action.SHORT_DESCRIPTION, "Area dove fare test delle azioni");
            this.putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl T"));
            this.putValue(MNEMONIC_KEY, KeyEvent.VK_T);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            ProfiloUtente profiloUtente = (ProfiloUtente) modello.getBean(Costanti.PROFILO_UTENTE_SELEZIONATO);
            if (profiloUtente == null) {
                frame.mostraMessaggioErrori("Seleziona o crea un profilo prima di avviare l'area test!");
                return;
            }
            controlloPrincipale.getAzioneAvvia().actionPerformed(null);
            vistaAreaTest.visualizza();
        }

    }

    private class AzioneInfo extends AbstractAction {

        public AzioneInfo() {
            this.putValue(Action.NAME, "Info");
            this.putValue(Action.SHORT_DESCRIPTION, "Info app");
            this.putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl I"));
            this.putValue(MNEMONIC_KEY, KeyEvent.VK_SLASH);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            vistaInfo.visualizza();
        }

    }

}
