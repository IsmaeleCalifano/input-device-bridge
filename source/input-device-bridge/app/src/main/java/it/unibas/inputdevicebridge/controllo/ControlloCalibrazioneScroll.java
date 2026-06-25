package it.unibas.inputdevicebridge.controllo;

import it.unibas.inputdevicebridge.Applicazione;
import it.unibas.inputdevicebridge.enums.ETipologiaAzionePersonalizzata;
import it.unibas.inputdevicebridge.modello.Costanti;
import it.unibas.inputdevicebridge.vista.VistaCalibrazioneScroll;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import javax.swing.Timer;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ControlloCalibrazioneScroll {

    private boolean scrolling = false;
    private long ultimoEventoRotellina;

    @Getter
    private MouseWheelListener listenerScroll = new ListenerScroll();

    public void inizializzaTracking() {
        Timer timer = new Timer(50, e -> {
            if (this.scrolling && ((System.nanoTime() - this.ultimoEventoRotellina) > Costanti.MAX_TEMPO_INATTIVO)) {
                this.scrolling = false;
                int numeroScrollEffettuati = Applicazione.getInstance().getCalibratoreSegnale().numeroElementiDurateAzione(ETipologiaAzionePersonalizzata.SCROLL);
                Applicazione.getInstance().getVistaCalibrazioneScroll().aggiornaLabelScrollEffettuati(numeroScrollEffettuati);
            }
        });
        timer.start();
    }

    private class ListenerScroll implements MouseWheelListener {

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            VistaCalibrazioneScroll vistaCalibrazioneScroll = Applicazione.getInstance().getVistaCalibrazioneScroll();
            if (!scrolling) {
                scrolling = true;
                Long durataScroll = (Long) Applicazione.getInstance().getModello().getBean(Costanti.DURATA_SEGNALE_ACQUISITO);
                Applicazione.getInstance().getModello().putBean(Costanti.DURATA_SEGNALE_ACQUISITO, null);
                if (durataScroll == null) {
                    return;
                }
                Applicazione.getInstance().getCalibratoreSegnale().aggiungiDurataAzione(ETipologiaAzionePersonalizzata.SCROLL, durataScroll);
                log.debug("Durata = {} s", durataScroll / (float) Costanti.DURATA_1_SECONDO);
                int numeroScrollEffettuati = Applicazione.getInstance().getCalibratoreSegnale().numeroElementiDurateAzione(ETipologiaAzionePersonalizzata.SCROLL);
                if (numeroScrollEffettuati == Costanti.NUMERO_TENTATIVI_CALIBRAZIONE) {
                    Applicazione.getInstance().getVistaCalibrazione().avanzaSchermo();
                    vistaCalibrazioneScroll.aggiornaLabelScrollEffettuati(0);
                    log.debug("Calibrazione scroll effettuata correttamente!");
                }
            }
            ultimoEventoRotellina = System.nanoTime();
        }
    }
}
