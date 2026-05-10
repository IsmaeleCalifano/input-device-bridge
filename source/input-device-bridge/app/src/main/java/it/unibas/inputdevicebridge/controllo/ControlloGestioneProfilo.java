package it.unibas.inputdevicebridge.controllo;

import it.unibas.inputdevicebridge.Applicazione;
import it.unibas.inputdevicebridge.modello.Costanti;
import it.unibas.inputdevicebridge.modello.profilo_utente.ArchivioProfiliUtente;
import it.unibas.inputdevicebridge.modello.profilo_utente.ProfiloUtente;
import it.unibas.inputdevicebridge.persistenza.DAOException;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class ControlloGestioneProfilo {
    
    private final Action azioneSalvaModificheProfilo = new AzioneSalvaModificheProfilo();
    private final Action azioneSalvaNuovoPrfilo = new AzioneSalvaNuovoPrfilo();
    
    private class AzioneSalvaModificheProfilo extends AbstractAction {

        public AzioneSalvaModificheProfilo() {
            this.putValue(Action.NAME, "Salva modifiche");
            this.putValue(Action.SHORT_DESCRIPTION, "Salva modifiche profilo utente");
            this.putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl S"));
            this.putValue(MNEMONIC_KEY, KeyEvent.VK_S);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            log.info("Azione salva modifiche profilo");
            try {
                String errori = this.convalida(Applicazione.getInstance().getVistaGestioneProfilo().getTextCampoNomeProfilo());
                if (!errori.trim().isEmpty()) {
                    log.debug("ERRORE: {}", errori);
                    Applicazione.getInstance().getFrame().mostraMessaggioErrori(errori);
                    return;
                }
                ArchivioProfiliUtente archivioProfiliUtente = (ArchivioProfiliUtente) Applicazione.getInstance().getModello().getBean(Costanti.ARCHIVIO_PROFILI_UTENTE);
                ProfiloUtente profiloUtente = (ProfiloUtente) Applicazione.getInstance().getModello().getBean(Costanti.PROFILO_UTENTE_SELEZIONATO);
                archivioProfiliUtente.rimuoviProfiloUtente(profiloUtente);
                ProfiloUtente profiloUtenteAggiornato = Applicazione.getInstance().getVistaGestioneProfilo().getProfiloAggiornato();
                archivioProfiliUtente.aggiungiProfiloUtente(profiloUtenteAggiornato);
                Applicazione.getInstance().getModello().putBean(Costanti.PROFILO_UTENTE_SELEZIONATO, profiloUtenteAggiornato);
                Applicazione.getInstance().getDeviceBridge().applicaProfiloUtente(profiloUtenteAggiornato);
                Applicazione.getInstance().getDaoArchivioProfiliUtente().salvaArchivioProfiliUtente(archivioProfiliUtente);
                Applicazione.getInstance().getVistaPrincipale().inizializzaComboProfili();
                Applicazione.getInstance().getVistaGestioneProfilo().dispose();
                Applicazione.getInstance().getFrame().mostraMessaggio("Modifiche profilo salvate correttamente!");
                log.debug("Profilo utente aggiornato correttamente!");
            } catch (DAOException ex) {
                log.debug("ERRORE: Impossibile salvare profilo utente!");
            }
        }
        
        private String convalida(String nomeProfilo) {
            StringBuilder sb = new StringBuilder();
            if (nomeProfilo.trim().isEmpty()) {
                sb.append("Il nome del profilo utente non deve essere vuoto!");
            }
            return sb.toString();
        }

    }
    
    private class AzioneSalvaNuovoPrfilo extends AbstractAction {

        public AzioneSalvaNuovoPrfilo() {
            this.putValue(Action.NAME, "Salva nuovo");
            this.putValue(Action.SHORT_DESCRIPTION, "Salva un nuovo profilo utente");
            this.putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl S"));
            this.putValue(MNEMONIC_KEY, KeyEvent.VK_S);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            log.info("Azione salva nuovo profilo");
            try {
                String errori = this.convalida(Applicazione.getInstance().getVistaGestioneProfilo().getTextCampoNomeProfilo());
                if (!errori.trim().isEmpty()) {
                    log.debug("ERRORE: {}", errori);
                    Applicazione.getInstance().getFrame().mostraMessaggioErrori(errori);
                    return;
                }
                ArchivioProfiliUtente archivioProfiliUtente = (ArchivioProfiliUtente) Applicazione.getInstance().getModello().getBean(Costanti.ARCHIVIO_PROFILI_UTENTE);
                ProfiloUtente profiloUtente = Applicazione.getInstance().getVistaGestioneProfilo().getProfiloAggiornato();
                archivioProfiliUtente.aggiungiProfiloUtente(profiloUtente);
                Applicazione.getInstance().getDaoArchivioProfiliUtente().salvaArchivioProfiliUtente(archivioProfiliUtente);
                Applicazione.getInstance().getModello().putBean(Costanti.PROFILO_UTENTE_SELEZIONATO, profiloUtente);
                Applicazione.getInstance().getDeviceBridge().applicaProfiloUtente(profiloUtente);
                Applicazione.getInstance().getVistaPrincipale().inizializzaComboProfili();
                Applicazione.getInstance().getControlloMenu().getAzioneGestioneProfilo().setEnabled(true);
                Applicazione.getInstance().getControlloPrincipale().getAzioneAggiornaProfiloSelezionato().setEnabled(true);
                Applicazione.getInstance().getVistaGestioneProfilo().dispose();
                Applicazione.getInstance().getFrame().mostraMessaggio("Nuovo profilo salvato correttamente!");
                log.debug("Nuovo profilo utente salvato correttamente!");
            } catch (DAOException ex) {
                log.debug("ERRORE: Impossibile salvare profilo utente!");
            }
        }
        
        private String convalida(String nomeProfilo) {
            StringBuilder sb = new StringBuilder();
            if (nomeProfilo.trim().isEmpty()) {
                sb.append("Il nome del profilo utente non deve essere vuoto!");
            }
            return sb.toString();
        }

    }
    
}
