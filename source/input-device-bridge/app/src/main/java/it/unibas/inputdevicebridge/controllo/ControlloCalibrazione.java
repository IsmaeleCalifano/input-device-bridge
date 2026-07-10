package it.unibas.inputdevicebridge.controllo;

import it.unibas.inputdevicebridge.enums.ETipologiaAzionePersonalizzata;
import it.unibas.inputdevicebridge.enums.ETipologiaEventoPersonalizzato;
import it.unibas.inputdevicebridge.modello.CalibratoreSegnale;
import it.unibas.inputdevicebridge.modello.Costanti;
import it.unibas.inputdevicebridge.modello.DeviceBridgeFacade;
import it.unibas.inputdevicebridge.modello.Modello;
import it.unibas.inputdevicebridge.modello.profilo_utente.ProfiloUtente;
import it.unibas.inputdevicebridge.vista.VistaCalibrazione;
import it.unibas.inputdevicebridge.vista.VistaGestioneProfilo;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
public class ControlloCalibrazione {

    private final Modello modello;
    private final DeviceBridgeFacade deviceBridgeFacade;
    private final CalibratoreSegnale calibratoreSegnale;
    private final VistaGestioneProfilo vistaGestioneProfilo;
    private final VistaCalibrazione vistaCalibrazione;
    private final ControlloPrincipale controlloPrincipale;

    private int indiceCorrente = 0;
    private int numeroTotaleStep = 0;
    @Getter
    private List<Map.Entry<ETipologiaEventoPersonalizzato, ETipologiaAzionePersonalizzata>> listaEntryEventiAzioniCalibrazione;

    @Inject
    public ControlloCalibrazione(Modello modello, DeviceBridgeFacade deviceBridgeFacade, CalibratoreSegnale calibratoreSegnale, VistaGestioneProfilo vistaGestioneProfilo, VistaCalibrazione vistaCalibrazione, ControlloPrincipale controlloPrincipale) {
        this.modello = modello;
        this.deviceBridgeFacade = deviceBridgeFacade;
        this.calibratoreSegnale = calibratoreSegnale;
        this.vistaGestioneProfilo = vistaGestioneProfilo;
        this.vistaCalibrazione = vistaCalibrazione;
        this.controlloPrincipale = controlloPrincipale;
    }

    public void inizializza(ProfiloUtente profiloUtente) {
        this.indiceCorrente = 0;
        this.listaEntryEventiAzioniCalibrazione = new ArrayList<>(profiloUtente.getMappaComandiPersonalizzati().entrySet());
        this.numeroTotaleStep = this.listaEntryEventiAzioniCalibrazione.size();
    }

    public void avanzaSchermo(VistaCalibrazione vistaCalibrazione) {
        if (this.indiceCorrente == this.numeroTotaleStep) {
            ProfiloUtente profiloUtenteTemporaneo = (ProfiloUtente) modello.getBean(Costanti.PROFILO_UTENTE_TEMPORANEO);
            profiloUtenteTemporaneo = calibratoreSegnale.calibraProfilo(profiloUtenteTemporaneo);
            if (profiloUtenteTemporaneo == null) {
                vistaCalibrazione.dispose();
                vistaGestioneProfilo.mostraMessaggioErrori("Impossibile calibrare il profilo: numero di azioni calibrate insufficiente");
                return;
            }
            vistaGestioneProfilo.inizializzaCampiProfiloUtente(profiloUtenteTemporaneo);
            List<Long> durateSegnali = new ArrayList<>(profiloUtenteTemporaneo.getMappaDurataSegnale().values());
            StringBuilder messaggio = new StringBuilder("Calibrazione eseguita con successo!");
            if (this.isDurateSegnaliTroppoSimili(durateSegnali)) {
                messaggio.append("\nNOTA: La durata di alcuni segnali è simile.");
            }
            modello.putBean(Costanti.ENTRY_EVENTO_AZIONE_CALIBRAZIONE, null);
            vistaCalibrazione.dispose();
            vistaGestioneProfilo.mostraMessaggio(messaggio.toString());
            return;
        }
        modello.putBean(Costanti.ENTRY_EVENTO_AZIONE_CALIBRAZIONE, this.listaEntryEventiAzioniCalibrazione.get(this.indiceCorrente));
        vistaCalibrazione.aggiornaStepBar(this.indiceCorrente);
        if (this.indiceCorrente > 0) {
            vistaCalibrazione.mostraPannelloSuccessivo();
        }
        this.indiceCorrente++;
        vistaCalibrazione.setVisible(true);
    }

    private boolean isDurateSegnaliTroppoSimili(List<Long> durateSegnali) {
        int count = 0;
        long mezzoSecondo = Costanti.DURATA_1_SECONDO / 2;
        for (int i = 0; i < (durateSegnali.size() - 1); i++) {
            count++;
            log.debug("Contatore: {}", count);
            long intervalloSegnali = durateSegnali.get(i + 1) - durateSegnali.get(i);
            if (intervalloSegnali < mezzoSecondo) {
                return true;
            }
        }
        return false;
    }

    public WindowAdapter getAzioneChiusuraFinestra() {
        return new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent evt) {
                controlloPrincipale.getAzioneFerma().actionPerformed(null);
                modello.putBean(Costanti.PROFILO_UTENTE_TEMPORANEO, null);
                ProfiloUtente profiloUtente = (ProfiloUtente) modello.getBean(Costanti.PROFILO_UTENTE_SELEZIONATO);
                if (profiloUtente != null) {
                    deviceBridgeFacade.applicaProfiloUtente(profiloUtente);
                }
                vistaGestioneProfilo.setVisible(true);
            }

            @Override
            public void windowClosing(WindowEvent evt) {
                controlloPrincipale.getAzioneFerma().actionPerformed(null);
                int scelta = vistaCalibrazione.mostraConfermaUscita();
                if (scelta == 0) {
                    vistaCalibrazione.dispose();
                    return;
                }
                controlloPrincipale.getAzioneAvvia().actionPerformed(null);
            }
        };
    }
}
