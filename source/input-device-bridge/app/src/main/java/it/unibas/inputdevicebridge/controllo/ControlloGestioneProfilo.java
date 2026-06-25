package it.unibas.inputdevicebridge.controllo;

import it.unibas.inputdevicebridge.Applicazione;
import it.unibas.inputdevicebridge.enums.ETipologiaAzionePersonalizzata;
import it.unibas.inputdevicebridge.modello.Costanti;
import it.unibas.inputdevicebridge.modello.profilo_utente.ArchivioProfiliUtente;
import it.unibas.inputdevicebridge.modello.profilo_utente.ProfiloUtente;
import it.unibas.inputdevicebridge.persistenza.DAOException;
import it.unibas.inputdevicebridge.vista.VistaGestioneProfilo;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.swing.AbstractAction;
import javax.swing.Action;
import static javax.swing.Action.ACCELERATOR_KEY;
import static javax.swing.Action.MNEMONIC_KEY;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class ControlloGestioneProfilo {

    private final Action azioneAvviaCalibrazione = new AzioneAvviaCalibrazione();
    private final Action azioneSalvaModificheProfilo = new AzioneSalvaModificheProfilo();
    private final Action azioneSalvaNuovoPrfilo = new AzioneSalvaNuovoPrfilo();

    private String convalida(String nomeProfiloUtente, ETipologiaAzionePersonalizzata itemComboSegnaleBreve, ETipologiaAzionePersonalizzata itemComboSegnaleMedio, ETipologiaAzionePersonalizzata itemComboSegnaleLungo) {
        StringBuilder errori = new StringBuilder();
        if (nomeProfiloUtente.trim().isEmpty()) {
            errori.append("Per avviare la calibrazione e' necessario inserire un nome per il profilo utente!");
        }
        Set<ETipologiaAzionePersonalizzata> itemSelezionati = new HashSet<>();
        itemSelezionati.add(itemComboSegnaleBreve);
        itemSelezionati.add(itemComboSegnaleMedio);
        itemSelezionati.add(itemComboSegnaleLungo);
        if (itemSelezionati.size() < 3) {
            errori.append("\nLe azioni selezionate devo essere tutte distinte!");
        }
        return errori.toString();
    }

    private class AzioneAvviaCalibrazione extends AbstractAction {

        public AzioneAvviaCalibrazione() {
            this.putValue(Action.NAME, "Avvia calibrazione");
            this.putValue(Action.SHORT_DESCRIPTION, "Apre una wizard deicada alla clibrazione");
            this.putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl A"));
            this.putValue(MNEMONIC_KEY, KeyEvent.VK_A);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            VistaGestioneProfilo vistaGestioneProfilo = Applicazione.getInstance().getVistaGestioneProfilo();
            String nomeProfiloUtente = vistaGestioneProfilo.getTextCampoNomeProfilo();
            ETipologiaAzionePersonalizzata itemComboSegnaleBreve = vistaGestioneProfilo.getSelectedItemComboSegnaleBreve();
            ETipologiaAzionePersonalizzata itemComboSegnaleMedio = vistaGestioneProfilo.getSelectedItemComboSegnaleMedio();
            ETipologiaAzionePersonalizzata itemComboSegnaleLungo = vistaGestioneProfilo.getSelectedItemComboSegnaleLungo();
            String errori = convalida(nomeProfiloUtente, itemComboSegnaleBreve, itemComboSegnaleMedio, itemComboSegnaleLungo);
            if (!errori.trim().isEmpty()) {
                log.debug("ERRORE: {}", errori);
                Applicazione.getInstance().getFrame().mostraMessaggioErrori(errori);
                return;
            }
            this.creaMappaAzioniViste();
            ProfiloUtente profiloUtenteTemporaneo = vistaGestioneProfilo.getProfiloAggiornato();
            Applicazione.getInstance().getModello().putBean(Costanti.PROFILO_UTENTE_TEMPORANEO, profiloUtenteTemporaneo);
            Applicazione.getInstance().getControlloPrincipale().getAzioneAvvia().actionPerformed(null);
            Applicazione.getInstance().getVistaCalibrazione().visualizza();
        }

        private void creaMappaAzioniViste() {
            Map<ETipologiaAzionePersonalizzata, JPanel> mappaAzoniViste = new HashMap<>();
            mappaAzoniViste.put(ETipologiaAzionePersonalizzata.CLICK, Applicazione.getInstance().getVistaCalibrazioneClick());
            mappaAzoniViste.put(ETipologiaAzionePersonalizzata.SCROLL, Applicazione.getInstance().getVistaCalibrazioneScroll());
            mappaAzoniViste.put(ETipologiaAzionePersonalizzata.TRASCINAMENTO, Applicazione.getInstance().getVistaCalibrazioneTrascinamento());
            Applicazione.getInstance().getModello().putBean(Costanti.MAPPA_AZIONI_VISTE, mappaAzoniViste);
        }

    }

    private class AzioneSalvaModificheProfilo extends AbstractAction {

        public AzioneSalvaModificheProfilo() {
            this.putValue(Action.NAME, "Salva modifiche");
            this.putValue(Action.SHORT_DESCRIPTION, "Salva modifiche profilo utente");
            this.putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl S"));
            this.putValue(MNEMONIC_KEY, KeyEvent.VK_S);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                VistaGestioneProfilo vistaGestioneProfilo = Applicazione.getInstance().getVistaGestioneProfilo();
                String nomeProfiloUtente = vistaGestioneProfilo.getTextCampoNomeProfilo();
                ETipologiaAzionePersonalizzata itemComboSegnaleBreve = vistaGestioneProfilo.getSelectedItemComboSegnaleBreve();
                ETipologiaAzionePersonalizzata itemComboSegnaleMedio = vistaGestioneProfilo.getSelectedItemComboSegnaleMedio();
                ETipologiaAzionePersonalizzata itemComboSegnaleLungo = vistaGestioneProfilo.getSelectedItemComboSegnaleLungo();
                String errori = convalida(nomeProfiloUtente, itemComboSegnaleBreve, itemComboSegnaleMedio, itemComboSegnaleLungo);
                if (!errori.trim().isEmpty()) {
                    log.debug("ERRORE: {}", errori);
                    Applicazione.getInstance().getFrame().mostraMessaggioErrori(errori);
                    return;
                }
                ArchivioProfiliUtente archivioProfiliUtente = (ArchivioProfiliUtente) Applicazione.getInstance().getModello().getBean(Costanti.ARCHIVIO_PROFILI_UTENTE);
                ProfiloUtente profiloUtente = (ProfiloUtente) Applicazione.getInstance().getModello().getBean(Costanti.PROFILO_UTENTE_SELEZIONATO);
                archivioProfiliUtente.rimuoviProfiloUtente(profiloUtente);
                ProfiloUtente profiloUtenteAggiornato = vistaGestioneProfilo.getProfiloAggiornato();
                archivioProfiliUtente.aggiungiProfiloUtente(profiloUtenteAggiornato);
                Applicazione.getInstance().getModello().putBean(Costanti.PROFILO_UTENTE_SELEZIONATO, profiloUtenteAggiornato);
                Applicazione.getInstance().getDeviceBridge().applicaProfiloUtente(profiloUtenteAggiornato);
                Applicazione.getInstance().getDaoArchivioProfiliUtente().salvaArchivioProfiliUtente(archivioProfiliUtente);
                Applicazione.getInstance().getVistaPrincipale().inizializzaComboProfili();
                vistaGestioneProfilo.dispose();
                Applicazione.getInstance().getFrame().mostraMessaggio("Modifiche profilo salvate correttamente!");
                log.debug("Profilo utente aggiornato correttamente!");
            } catch (DAOException ex) {
                log.debug("ERRORE: Impossibile salvare profilo utente!");
            }
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
            try {
                VistaGestioneProfilo vistaGestioneProfilo = Applicazione.getInstance().getVistaGestioneProfilo();
                String nomeProfiloUtente = vistaGestioneProfilo.getTextCampoNomeProfilo();
                ETipologiaAzionePersonalizzata itemComboSegnaleBreve = vistaGestioneProfilo.getSelectedItemComboSegnaleBreve();
                ETipologiaAzionePersonalizzata itemComboSegnaleMedio = vistaGestioneProfilo.getSelectedItemComboSegnaleMedio();
                ETipologiaAzionePersonalizzata itemComboSegnaleLungo = vistaGestioneProfilo.getSelectedItemComboSegnaleLungo();
                String errori = convalida(nomeProfiloUtente, itemComboSegnaleBreve, itemComboSegnaleMedio, itemComboSegnaleLungo);
                if (!errori.trim().isEmpty()) {
                    log.debug("ERRORE: {}", errori);
                    Applicazione.getInstance().getFrame().mostraMessaggioErrori(errori);
                    return;
                }
                ArchivioProfiliUtente archivioProfiliUtente = (ArchivioProfiliUtente) Applicazione.getInstance().getModello().getBean(Costanti.ARCHIVIO_PROFILI_UTENTE);
                ProfiloUtente profiloUtente = vistaGestioneProfilo.getProfiloAggiornato();
                archivioProfiliUtente.aggiungiProfiloUtente(profiloUtente);
                Applicazione.getInstance().getDaoArchivioProfiliUtente().salvaArchivioProfiliUtente(archivioProfiliUtente);
                Applicazione.getInstance().getModello().putBean(Costanti.PROFILO_UTENTE_SELEZIONATO, profiloUtente);
                Applicazione.getInstance().getDeviceBridge().applicaProfiloUtente(profiloUtente);
                Applicazione.getInstance().getVistaPrincipale().inizializzaComboProfili();
                Applicazione.getInstance().getControlloMenu().getAzioneGestioneProfilo().setEnabled(true);
                Applicazione.getInstance().getControlloPrincipale().getAzioneAggiornaProfiloSelezionato().setEnabled(true);
                vistaGestioneProfilo.dispose();
                Applicazione.getInstance().getFrame().mostraMessaggio("Nuovo profilo salvato correttamente!");
                log.debug("Nuovo profilo utente salvato correttamente!");
            } catch (DAOException ex) {
                log.debug("ERRORE: Impossibile salvare profilo utente!");
            }
        }

    }

}
