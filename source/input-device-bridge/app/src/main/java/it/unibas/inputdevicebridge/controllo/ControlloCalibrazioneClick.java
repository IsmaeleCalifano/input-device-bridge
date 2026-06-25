package it.unibas.inputdevicebridge.controllo;

import it.unibas.inputdevicebridge.Applicazione;
import it.unibas.inputdevicebridge.enums.ETipologiaAzionePersonalizzata;
import it.unibas.inputdevicebridge.modello.Costanti;
import it.unibas.inputdevicebridge.vista.VistaCalibrazioneClick;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import static javax.swing.Action.ACCELERATOR_KEY;
import static javax.swing.Action.MNEMONIC_KEY;
import javax.swing.KeyStroke;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class ControlloCalibrazioneClick {

    private Action azioneCalibraClick = new AzioneCalibraClick();

    private class AzioneCalibraClick extends AbstractAction {

        public AzioneCalibraClick() {
            this.putValue(Action.NAME, "Clicca");
            this.putValue(Action.SHORT_DESCRIPTION, "Clicca per calibrare");
            this.putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl alt c"));
            this.putValue(MNEMONIC_KEY, KeyEvent.VK_C);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            VistaCalibrazioneClick vistaCalibrazioneClick = Applicazione.getInstance().getVistaCalibrazioneClick();
            Long durataClick = (Long) Applicazione.getInstance().getModello().getBean(Costanti.DURATA_SEGNALE_ACQUISITO);
            Applicazione.getInstance().getModello().putBean(Costanti.DURATA_SEGNALE_ACQUISITO, null);
            if (durataClick == null) {
                return;
            }
            log.debug("Durata click: {} s", durataClick / (float) Costanti.DURATA_1_SECONDO);
            Applicazione.getInstance().getCalibratoreSegnale().aggiungiDurataAzione(ETipologiaAzionePersonalizzata.CLICK, durataClick);
            int numeroClickEffettuati = Applicazione.getInstance().getCalibratoreSegnale().numeroElementiDurateAzione(ETipologiaAzionePersonalizzata.CLICK);
            vistaCalibrazioneClick.aggiornaLabelClickEffettuati(numeroClickEffettuati);
            if (numeroClickEffettuati == Costanti.NUMERO_TENTATIVI_CALIBRAZIONE) {
                Applicazione.getInstance().getVistaCalibrazione().avanzaSchermo();
                vistaCalibrazioneClick.aggiornaLabelClickEffettuati(0);
                log.debug("Calibrazione click effettuata correttamente!");
            }
        }
    }

}
