package it.unibas.inputdevicebridge.controllo;

import it.unibas.inputdevicebridge.Applicazione;
import it.unibas.inputdevicebridge.modello.Costanti;
import it.unibas.inputdevicebridge.modello.profilo_utente.ArchivioProfiliUtente;
import it.unibas.inputdevicebridge.modello.profilo_utente.ProfiloUtente;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;
import lombok.Getter;

@Getter
public class ControlloMenu {

    private final Action azioneEsci = new AzioneEsci();
    private final Action azioneNuovoProfilo = new AzioneNuovoProfilo();
    private final Action azioneGestioneProfilo = new AzioneGestioneProfilo();
    private final Action azioneAreaTest = new AzioneAreaTest();
    private final Action azioneInfo = new AzioneInfo();

    public void inizializza() {
        this.inizializzaAzioneGestioneProfilo();
    }

    private void inizializzaAzioneGestioneProfilo() {
        ArchivioProfiliUtente archivioProfiliUtente = (ArchivioProfiliUtente) Applicazione.getInstance().getModello().getBean(Costanti.ARCHIVIO_PROFILI_UTENTE);
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
            Applicazione.getInstance().getVistaGestioneProfilo().visualizzaNuovo();
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
            Applicazione.getInstance().getVistaGestioneProfilo().visualizzaModifica();
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
            ProfiloUtente profiloUtente = (ProfiloUtente) Applicazione.getInstance().getModello().getBean(Costanti.PROFILO_UTENTE_SELEZIONATO);
            if (profiloUtente == null) {
                Applicazione.getInstance().getFrame().mostraMessaggioErrori("Seleziona o crea un profilo prima di avviare l'area test!");
                return;
            }
            Applicazione.getInstance().getControlloPrincipale().getAzioneAvvia().actionPerformed(null);
            Applicazione.getInstance().getVistaAreaTest().visualizza();
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
            Applicazione.getInstance().getVistaInfo().visualizza();
        }

    }

}
