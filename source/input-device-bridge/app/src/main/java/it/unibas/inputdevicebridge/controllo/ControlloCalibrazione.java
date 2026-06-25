package it.unibas.inputdevicebridge.controllo;

import it.unibas.inputdevicebridge.Applicazione;
import it.unibas.inputdevicebridge.enums.ETipologiaAzionePersonalizzata;
import it.unibas.inputdevicebridge.enums.ETipologiaEventoPersonalizzato;
import it.unibas.inputdevicebridge.modello.Costanti;
import it.unibas.inputdevicebridge.modello.profilo_utente.ProfiloUtente;
import it.unibas.inputdevicebridge.vista.VistaCalibrazione;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ControlloCalibrazione {

    private int indiceCorrente = 0;
    private int numeroTotaleStep = 0;
    @Getter
    private List<Map.Entry<ETipologiaEventoPersonalizzato, ETipologiaAzionePersonalizzata>> listaEntryEventiAzioniCalibrazione;

    public void inizializza(ProfiloUtente profiloUtente) {
        this.indiceCorrente = 0;
        this.listaEntryEventiAzioniCalibrazione = new ArrayList<>(profiloUtente.getMappaComandiPersonalizzati().entrySet());
        this.numeroTotaleStep = this.listaEntryEventiAzioniCalibrazione.size();
    }

    public void avanzaSchermo(VistaCalibrazione vistaCalibrazione) {
        if (this.indiceCorrente == this.numeroTotaleStep) {
            ProfiloUtente profiloUtente = (ProfiloUtente) Applicazione.getInstance().getModello().getBean(Costanti.PROFILO_UTENTE_TEMPORANEO);
            profiloUtente = Applicazione.getInstance().getCalibratoreSegnale().calibraProfilo(profiloUtente);
            if (profiloUtente == null) {
                vistaCalibrazione.dispose();
                Applicazione.getInstance().getFrame().mostraMessaggioErrori("Impossibile calibrare il profilo: numero di azioni calibrate insufficiente");
                return;
            }
            Applicazione.getInstance().getVistaGestioneProfilo().inizializzaCampiProfiloUtente(profiloUtente);
            List<Long> durateSegnali = new ArrayList<>(profiloUtente.getMappaDurataSegnale().values());
            StringBuilder messaggio = new StringBuilder("Calibrazione eseguita con successo!");
            if (this.isDurateSegnaliTroppoSimili(durateSegnali)) {
                messaggio.append("\nNOTA: La durata di alcuni segnali è simile.");
            }
            Applicazione.getInstance().getModello().putBean(Costanti.ENTRY_EVENTO_AZIONE_CALIBRAZIONE, null);
            vistaCalibrazione.dispose();
            Applicazione.getInstance().getFrame().mostraMessaggio(messaggio.toString());
            return;
        }
        Applicazione.getInstance().getModello().putBean(Costanti.ENTRY_EVENTO_AZIONE_CALIBRAZIONE, this.listaEntryEventiAzioniCalibrazione.get(this.indiceCorrente));
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
                Applicazione.getInstance().getModello().putBean(Costanti.PROFILO_UTENTE_TEMPORANEO, null);
            }

            @Override
            public void windowClosing(WindowEvent evt) {
                Applicazione.getInstance().getControlloPrincipale().getAzioneFerma().actionPerformed(null);
                VistaCalibrazione vistaCalibrazione = Applicazione.getInstance().getVistaCalibrazione();
                int scelta = vistaCalibrazione.mostraConfermaUscita();
                if (scelta == 0) {
                    vistaCalibrazione.dispose();
                    return;
                }
                Applicazione.getInstance().getControlloPrincipale().getAzioneAvvia().actionPerformed(null);
            }
        };
    }
}
