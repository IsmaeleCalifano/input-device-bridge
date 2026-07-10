package it.unibas.inputdevicebridge.controllo;

import it.unibas.inputdevicebridge.enums.ETipologiaAzionePersonalizzata;
import it.unibas.inputdevicebridge.enums.ETipologiaEventoPersonalizzato;
import it.unibas.inputdevicebridge.modello.CalibratoreSegnale;
import it.unibas.inputdevicebridge.modello.Costanti;
import it.unibas.inputdevicebridge.modello.Modello;
import it.unibas.inputdevicebridge.modello.interprete.IInterpreteObserver;
import it.unibas.inputdevicebridge.modello.interprete.Interprete;
import it.unibas.inputdevicebridge.vista.VistaCalibrazione;
import it.unibas.inputdevicebridge.vista.VistaCalibrazioneScroll;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.Map;
import javax.swing.Timer;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
public class ControlloCalibrazioneScroll implements IInterpreteObserver {

    private final Modello modello;
    private final CalibratoreSegnale calibratoreSegnale;
    private final VistaCalibrazione vistaCalibrazione;
    private final VistaCalibrazioneScroll vistaCalibrazioneScroll;

    private boolean scrolling = false;
    private long ultimoEventoRotellina;

    private volatile Long durataSegnaleAcquisito;

    @Getter
    private final MouseWheelListener listenerScroll = new ListenerScroll();

    @Inject
    public ControlloCalibrazioneScroll(Interprete interprete, Modello modello, CalibratoreSegnale calibratoreSegnale, VistaCalibrazione vistaCalibrazione, VistaCalibrazioneScroll vistaCalibrazioneScroll) {
        this.modello = modello;
        this.calibratoreSegnale = calibratoreSegnale;
        this.vistaCalibrazione = vistaCalibrazione;
        this.vistaCalibrazioneScroll = vistaCalibrazioneScroll;
        interprete.addObserver(this);
    }

    @Override
    public void onDurataSegnaleAggiornata(Long durataSegnale) {
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onDurataSegnaleStatoTerminato(Long durataSegnale) {
        Map.Entry<ETipologiaEventoPersonalizzato, ETipologiaAzionePersonalizzata> stepCorrente = (Map.Entry<ETipologiaEventoPersonalizzato, ETipologiaAzionePersonalizzata>) modello.getBean(Costanti.ENTRY_EVENTO_AZIONE_CALIBRAZIONE);
        if (stepCorrente == null || stepCorrente.getValue() != ETipologiaAzionePersonalizzata.SCROLL) {
            return;
        }
        this.durataSegnaleAcquisito = durataSegnale;
    }
    
    public void inizializzaTracking() {
        Timer timer = new Timer(50, e -> {
            if (this.scrolling && ((System.nanoTime() - this.ultimoEventoRotellina) > Costanti.MAX_TEMPO_INATTIVO)) {
                this.scrolling = false;
                int numeroScrollEffettuati = calibratoreSegnale.numeroElementiDurateAzione(ETipologiaAzionePersonalizzata.SCROLL);
                vistaCalibrazioneScroll.aggiornaLabelScrollEffettuati(numeroScrollEffettuati);
            }
        });
        timer.start();
    }    

    private class ListenerScroll implements MouseWheelListener {

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            if (!scrolling) {
                scrolling = true;
                Long durataScroll = durataSegnaleAcquisito;
                durataSegnaleAcquisito = null;
                if (durataScroll == null) {
                    return;
                }
                calibratoreSegnale.aggiungiDurataAzione(ETipologiaAzionePersonalizzata.SCROLL, durataScroll);
                log.debug("Durata = {} s", durataScroll / (float) Costanti.DURATA_1_SECONDO);
                int numeroScrollEffettuati = calibratoreSegnale.numeroElementiDurateAzione(ETipologiaAzionePersonalizzata.SCROLL);
                if (numeroScrollEffettuati == Costanti.NUMERO_TENTATIVI_CALIBRAZIONE) {
                    vistaCalibrazione.avanzaSchermo();
                    vistaCalibrazioneScroll.aggiornaLabelScrollEffettuati(0);
                    log.debug("Calibrazione scroll effettuata correttamente!");
                }
            }
            ultimoEventoRotellina = System.nanoTime();
        }
    }
}
