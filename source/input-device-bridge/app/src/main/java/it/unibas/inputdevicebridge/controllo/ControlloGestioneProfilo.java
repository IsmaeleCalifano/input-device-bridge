package it.unibas.inputdevicebridge.controllo;

import it.unibas.inputdevicebridge.enums.ETipologiaAzionePersonalizzata;
import it.unibas.inputdevicebridge.modello.Costanti;
import it.unibas.inputdevicebridge.modello.DeviceBridgeFacade;
import it.unibas.inputdevicebridge.modello.Modello;
import it.unibas.inputdevicebridge.modello.profilo_utente.ArchivioProfiliUtente;
import it.unibas.inputdevicebridge.modello.profilo_utente.ProfiloUtente;
import it.unibas.inputdevicebridge.persistenza.DAOException;
import it.unibas.inputdevicebridge.persistenza.IDAOArchivioProfiliUtente;
import it.unibas.inputdevicebridge.vista.Frame;
import it.unibas.inputdevicebridge.vista.VistaCalibrazione;
import it.unibas.inputdevicebridge.vista.VistaCalibrazioneClick;
import it.unibas.inputdevicebridge.vista.VistaCalibrazioneDoppioClick;
import it.unibas.inputdevicebridge.vista.VistaCalibrazioneScroll;
import it.unibas.inputdevicebridge.vista.VistaCalibrazioneTrascinamento;
import it.unibas.inputdevicebridge.vista.VistaGestioneProfilo;
import it.unibas.inputdevicebridge.vista.VistaPrincipale;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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
@ApplicationScoped
public class ControlloGestioneProfilo {

    private final Modello modello;
    private final DeviceBridgeFacade deviceBridgeFacade;
    private final IDAOArchivioProfiliUtente daoArchivioProfiliUtente;
    private final Frame frame;
    private final VistaPrincipale vistaPrincipale;
    private final VistaGestioneProfilo vistaGestioneProfilo;
    private final VistaCalibrazione vistaCalibrazione;
    private final VistaCalibrazioneClick vistaCalibrazioneClick;
    private final VistaCalibrazioneDoppioClick vistaCalibrazioneDoppioClick;
    private final VistaCalibrazioneScroll vistaCalibrazioneScroll;
    private final VistaCalibrazioneTrascinamento vistaCalibrazioneTrascinamento;
    private final ControlloMenu controlloMenu;
    private final ControlloPrincipale controlloPrincipale;

    @Inject
    public ControlloGestioneProfilo(Modello modello, DeviceBridgeFacade deviceBridgeFacade, IDAOArchivioProfiliUtente daoArchivioProfiliUtente, Frame frame, VistaPrincipale vistaPrincipale, VistaGestioneProfilo vistaGestioneProfilo, VistaCalibrazione vistaCalibrazione, VistaCalibrazioneClick vistaCalibrazioneClick, VistaCalibrazioneDoppioClick vistaCalibrazioneDoppioClick, VistaCalibrazioneScroll vistaCalibrazioneScroll, VistaCalibrazioneTrascinamento vistaCalibrazioneTrascinamento, ControlloMenu controlloMenu, ControlloPrincipale controlloPrincipale) {
        this.modello = modello;
        this.deviceBridgeFacade = deviceBridgeFacade;
        this.daoArchivioProfiliUtente = daoArchivioProfiliUtente;
        this.frame = frame;
        this.vistaPrincipale = vistaPrincipale;
        this.vistaGestioneProfilo = vistaGestioneProfilo;
        this.vistaCalibrazione = vistaCalibrazione;
        this.vistaCalibrazioneClick = vistaCalibrazioneClick;
        this.vistaCalibrazioneDoppioClick = vistaCalibrazioneDoppioClick;
        this.vistaCalibrazioneScroll = vistaCalibrazioneScroll;
        this.vistaCalibrazioneTrascinamento = vistaCalibrazioneTrascinamento;
        this.controlloMenu = controlloMenu;
        this.controlloPrincipale = controlloPrincipale;
    }

    private final Action azioneAvviaCalibrazione = new AzioneAvviaCalibrazione();
    private final Action azioneSalvaModificheProfilo = new AzioneSalvaModificheProfilo();
    private final Action azioneSalvaNuovoPrfilo = new AzioneSalvaNuovoPrfilo();
    private final Action azioneEliminaProfiloUtente = new AzioneEliminaProfiloUtente();

    private String convalida(ProfiloUtente profiloUtente) {
        StringBuilder errori = new StringBuilder();
        if (profiloUtente.getNome().trim().isEmpty()) {
            errori.append("Per avviare la calibrazione e' necessario inserire un nome per il profilo utente!");
        }
        if (this.verificaItemSelezionatiDuplicati(profiloUtente)) {
            errori.append("\nLe azioni selezionate devo essere tutte distinte!");
        }
        return errori.toString();
    }

    private boolean verificaProfiloDuplicato(ProfiloUtente profiloUtente) {
        ArchivioProfiliUtente archivioProfiliUtente = (ArchivioProfiliUtente) this.modello.getBean(Costanti.ARCHIVIO_PROFILI_UTENTE);
        return archivioProfiliUtente.getListaProfiliUtente().contains(profiloUtente);
    }

    private boolean verificaItemSelezionatiDuplicati(ProfiloUtente profiloUtente) {
        Set<ETipologiaAzionePersonalizzata> itemSelezionati = new HashSet<>();
        List<ETipologiaAzionePersonalizzata> listaComandiPersonalizzati = new ArrayList<>(profiloUtente.getMappaComandiPersonalizzati().values());
        for (ETipologiaAzionePersonalizzata comandoPersonalizzato : listaComandiPersonalizzati) {
            itemSelezionati.add(comandoPersonalizzato);
        }
        return itemSelezionati.size() < listaComandiPersonalizzati.size();
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
            ProfiloUtente profiloUtenteTemporaneo = vistaGestioneProfilo.getProfiloAggiornato();
            String errori = convalida(profiloUtenteTemporaneo);
            if (!errori.trim().isEmpty()) {
                log.debug("ERRORE: {}", errori);
                frame.mostraMessaggioErrori(errori);
                return;
            }
            this.creaMappaAzioniViste();
            modello.putBean(Costanti.PROFILO_UTENTE_TEMPORANEO, profiloUtenteTemporaneo);
            deviceBridgeFacade.applicaProfiloUtente(profiloUtenteTemporaneo);
            controlloPrincipale.getAzioneAvvia().actionPerformed(null);
            vistaGestioneProfilo.setVisible(false);
            vistaCalibrazione.visualizza();
        }

        private void creaMappaAzioniViste() {
            Map<ETipologiaAzionePersonalizzata, JPanel> mappaAzoniViste = new HashMap<>();
            mappaAzoniViste.put(ETipologiaAzionePersonalizzata.CLICK, vistaCalibrazioneClick);
            mappaAzoniViste.put(ETipologiaAzionePersonalizzata.DOPPIO_CLICK, vistaCalibrazioneDoppioClick);
            mappaAzoniViste.put(ETipologiaAzionePersonalizzata.SCROLL, vistaCalibrazioneScroll);
            mappaAzoniViste.put(ETipologiaAzionePersonalizzata.TRASCINAMENTO, vistaCalibrazioneTrascinamento);
            modello.putBean(Costanti.MAPPA_AZIONI_VISTE, mappaAzoniViste);
        }

    }

    private class AzioneEliminaProfiloUtente extends AbstractAction {

        public AzioneEliminaProfiloUtente() {
            this.putValue(Action.NAME, "Elimina profilo");
            this.putValue(Action.SHORT_DESCRIPTION, "Elimina profilo utente selezionato");
            this.putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl E"));
            this.putValue(MNEMONIC_KEY, KeyEvent.VK_E);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            ArchivioProfiliUtente archivioProfiliUtente = (ArchivioProfiliUtente) modello.getBean(Costanti.ARCHIVIO_PROFILI_UTENTE);
            ProfiloUtente profiloUtente = (ProfiloUtente) modello.getBean(Costanti.PROFILO_UTENTE_SELEZIONATO);
            try {
                archivioProfiliUtente.rimuoviProfiloUtente(profiloUtente);
                daoArchivioProfiliUtente.salvaArchivioProfiliUtente(archivioProfiliUtente);
                modello.putBean(Costanti.PROFILO_UTENTE_SELEZIONATO, archivioProfiliUtente.getProfiloUtentePerIndice(0));
                vistaPrincipale.inizializzaComboProfili();
                if (archivioProfiliUtente.size() == 0) {
                    controlloMenu.getAzioneGestioneProfilo().setEnabled(false);
                     controlloPrincipale.getAzioneAggiornaProfiloSelezionato().setEnabled(false);
                }
                vistaGestioneProfilo.dispose();
            } catch (DAOException ex) {
                archivioProfiliUtente.aggiungiProfiloUtente(profiloUtente);
                log.error("ERRORE: Impossibile eliminare il profilo utente selezionato!");
                frame.mostraMessaggioErrori("Impossibile eliminare il profilo utente selezionato!");
            }
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
                ProfiloUtente profiloUtenteAggiornato = vistaGestioneProfilo.getProfiloAggiornato();
                String errori = convalida(profiloUtenteAggiornato);
                ProfiloUtente profiloUtente = (ProfiloUtente) modello.getBean(Costanti.PROFILO_UTENTE_SELEZIONATO);
                if (!profiloUtente.equals(profiloUtenteAggiornato) && verificaProfiloDuplicato(profiloUtente)) {
                    errori += "\nIl nome scelto è già utilizzato per un altro profilo utente!";
                }
                if (!errori.trim().isEmpty()) {
                    log.debug("ERRORE: {}", errori);
                    frame.mostraMessaggioErrori(errori);
                    return;
                }
                ArchivioProfiliUtente archivioProfiliUtente = (ArchivioProfiliUtente) modello.getBean(Costanti.ARCHIVIO_PROFILI_UTENTE);
                archivioProfiliUtente.rimuoviProfiloUtente(profiloUtente);
                archivioProfiliUtente.aggiungiProfiloUtente(profiloUtenteAggiornato);
                modello.putBean(Costanti.PROFILO_UTENTE_SELEZIONATO, profiloUtenteAggiornato);
                deviceBridgeFacade.applicaProfiloUtente(profiloUtenteAggiornato);
                daoArchivioProfiliUtente.salvaArchivioProfiliUtente(archivioProfiliUtente);
                vistaPrincipale.inizializzaComboProfili();
                vistaGestioneProfilo.dispose();
                frame.mostraMessaggio("Modifiche profilo salvate correttamente!");
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
                ProfiloUtente profiloUtente = vistaGestioneProfilo.getProfiloAggiornato();
                String errori = convalida(profiloUtente);
                if (verificaProfiloDuplicato(profiloUtente)) {
                    errori += "\nIl nome scelto è già utilizzato per un altro profilo utente!";
                }
                if (!errori.trim().isEmpty()) {
                    log.debug("ERRORE: {}", errori);
                    frame.mostraMessaggioErrori(errori);
                    return;
                }
                ArchivioProfiliUtente archivioProfiliUtente = (ArchivioProfiliUtente) modello.getBean(Costanti.ARCHIVIO_PROFILI_UTENTE);
                archivioProfiliUtente.aggiungiProfiloUtente(profiloUtente);
                daoArchivioProfiliUtente.salvaArchivioProfiliUtente(archivioProfiliUtente);
                modello.putBean(Costanti.PROFILO_UTENTE_SELEZIONATO, profiloUtente);
                deviceBridgeFacade.applicaProfiloUtente(profiloUtente);
                vistaPrincipale.inizializzaComboProfili();
                controlloMenu.getAzioneGestioneProfilo().setEnabled(true);
                controlloPrincipale.getAzioneAggiornaProfiloSelezionato().setEnabled(true);
                vistaGestioneProfilo.dispose();
                frame.mostraMessaggio("Nuovo profilo salvato correttamente!");
                log.debug("Nuovo profilo utente salvato correttamente!");
            } catch (DAOException ex) {
                log.debug("ERRORE: Impossibile salvare profilo utente!");
            }
        }

    }

}
