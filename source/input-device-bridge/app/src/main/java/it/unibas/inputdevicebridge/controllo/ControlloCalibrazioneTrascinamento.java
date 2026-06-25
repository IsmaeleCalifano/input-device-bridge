package it.unibas.inputdevicebridge.controllo;

import it.unibas.inputdevicebridge.Applicazione;
import it.unibas.inputdevicebridge.enums.ETipologiaAzionePersonalizzata;
import it.unibas.inputdevicebridge.modello.Costanti;
import it.unibas.inputdevicebridge.vista.VistaCalibrazioneTrascinamento;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ControlloCalibrazioneTrascinamento {

    private boolean selecting = false;
    
    @Getter
    private MouseAdapter listenerTrascinamento = new ListenerTrascinamento();

    private class ListenerTrascinamento extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {
            selecting = false;
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            VistaCalibrazioneTrascinamento vistaCalibrazioneTrascinamento = Applicazione.getInstance().getVistaCalibrazioneTrascinamento();
            if (!selecting && vistaCalibrazioneTrascinamento.haTestoSelezionato()) {
                selecting = true;
                Long durataTrascinamento = (Long) Applicazione.getInstance().getModello().getBean(Costanti.DURATA_SEGNALE_ACQUISITO);
                Applicazione.getInstance().getModello().putBean(Costanti.DURATA_SEGNALE_ACQUISITO, null);
                if (durataTrascinamento == null) {
                    return;
                }
                Applicazione.getInstance().getCalibratoreSegnale().aggiungiDurataAzione(ETipologiaAzionePersonalizzata.TRASCINAMENTO, durataTrascinamento);
                log.debug("Durata trascinamento: {} s", durataTrascinamento / (float) Costanti.DURATA_1_SECONDO);
                
                int numeroTrascinamentiEffettuati = Applicazione.getInstance().getCalibratoreSegnale().numeroElementiDurateAzione(ETipologiaAzionePersonalizzata.TRASCINAMENTO);
                vistaCalibrazioneTrascinamento.aggiornaLabelTrascinamentiEffettuati(numeroTrascinamentiEffettuati);
                
                if (numeroTrascinamentiEffettuati == Costanti.NUMERO_TENTATIVI_CALIBRAZIONE) {
                    Applicazione.getInstance().getVistaCalibrazione().avanzaSchermo();
                    vistaCalibrazioneTrascinamento.aggiornaLabelTrascinamentiEffettuati(0);
                }
            }
        }
    }
}