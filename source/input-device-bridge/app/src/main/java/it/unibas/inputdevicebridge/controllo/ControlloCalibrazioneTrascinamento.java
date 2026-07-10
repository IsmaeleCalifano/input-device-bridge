package it.unibas.inputdevicebridge.controllo;

import it.unibas.inputdevicebridge.enums.ETipologiaAzionePersonalizzata;
import it.unibas.inputdevicebridge.enums.ETipologiaEventoPersonalizzato;
import it.unibas.inputdevicebridge.modello.CalibratoreSegnale;
import it.unibas.inputdevicebridge.modello.Costanti;
import it.unibas.inputdevicebridge.modello.Modello;
import it.unibas.inputdevicebridge.modello.interprete.IInterpreteObserver;
import it.unibas.inputdevicebridge.modello.interprete.Interprete;
import it.unibas.inputdevicebridge.vista.VistaCalibrazione;
import it.unibas.inputdevicebridge.vista.VistaCalibrazioneTrascinamento;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
public class ControlloCalibrazioneTrascinamento implements IInterpreteObserver {

    private final Modello modello;
    private final CalibratoreSegnale calibratoreSegnale;
    private final VistaCalibrazione vistaCalibrazione;
    private final VistaCalibrazioneTrascinamento vistaCalibrazioneTrascinamento;
    private volatile Long durataSegnaleAcquisito;

    private boolean selecting = false;

    @Getter
    private final MouseAdapter listenerTrascinamento = new ListenerTrascinamento();

    @Inject
    public ControlloCalibrazioneTrascinamento(Interprete interprete, Modello modello, CalibratoreSegnale calibratoreSegnale, VistaCalibrazione vistaCalibrazione, VistaCalibrazioneTrascinamento vistaCalibrazioneTrascinamento) {
        this.modello = modello;
        this.calibratoreSegnale = calibratoreSegnale;
        this.vistaCalibrazione = vistaCalibrazione;
        this.vistaCalibrazioneTrascinamento = vistaCalibrazioneTrascinamento;
        interprete.addObserver(this);
    }

    @Override
    public void onDurataSegnaleAggiornata(Long durataSegnale) {
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onDurataSegnaleStatoTerminato(Long durataSegnale) {
        Map.Entry<ETipologiaEventoPersonalizzato, ETipologiaAzionePersonalizzata> stepCorrente = (Map.Entry<ETipologiaEventoPersonalizzato, ETipologiaAzionePersonalizzata>) modello.getBean(Costanti.ENTRY_EVENTO_AZIONE_CALIBRAZIONE);
        if (stepCorrente == null || stepCorrente.getValue() != ETipologiaAzionePersonalizzata.TRASCINAMENTO) {
            return;
        }
        this.durataSegnaleAcquisito = durataSegnale;
    }

    private class ListenerTrascinamento extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {
            selecting = false;
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (!selecting && vistaCalibrazioneTrascinamento.haTestoSelezionato()) {
                selecting = true;
                Long durataTrascinamento = durataSegnaleAcquisito;
                durataSegnaleAcquisito = null;
                if (durataTrascinamento == null) {
                    return;
                }
                calibratoreSegnale.aggiungiDurataAzione(ETipologiaAzionePersonalizzata.TRASCINAMENTO, durataTrascinamento);
                log.debug("Durata trascinamento: {} s", durataTrascinamento / (float) Costanti.DURATA_1_SECONDO);

                int numeroTrascinamentiEffettuati = calibratoreSegnale.numeroElementiDurateAzione(ETipologiaAzionePersonalizzata.TRASCINAMENTO);
                vistaCalibrazioneTrascinamento.aggiornaLabelTrascinamentiEffettuati(numeroTrascinamentiEffettuati);

                if (numeroTrascinamentiEffettuati == Costanti.NUMERO_TENTATIVI_CALIBRAZIONE) {
                    vistaCalibrazione.avanzaSchermo();
                    vistaCalibrazioneTrascinamento.aggiornaLabelTrascinamentiEffettuati(0);
                }
            }
        }
    }
}
