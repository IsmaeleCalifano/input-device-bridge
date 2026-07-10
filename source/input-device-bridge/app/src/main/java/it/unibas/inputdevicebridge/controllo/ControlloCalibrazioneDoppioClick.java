package it.unibas.inputdevicebridge.controllo;

import it.unibas.inputdevicebridge.enums.ETipologiaAzionePersonalizzata;
import it.unibas.inputdevicebridge.enums.ETipologiaEventoPersonalizzato;
import it.unibas.inputdevicebridge.modello.CalibratoreSegnale;
import it.unibas.inputdevicebridge.modello.Costanti;
import it.unibas.inputdevicebridge.modello.Modello;
import it.unibas.inputdevicebridge.modello.interprete.IInterpreteObserver;
import it.unibas.inputdevicebridge.modello.interprete.Interprete;
import it.unibas.inputdevicebridge.vista.VistaCalibrazione;
import it.unibas.inputdevicebridge.vista.VistaCalibrazioneClick;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Map;
import javax.swing.AbstractAction;
import javax.swing.Action;
import static javax.swing.Action.ACCELERATOR_KEY;
import static javax.swing.Action.MNEMONIC_KEY;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
@ApplicationScoped
public class ControlloCalibrazioneDoppioClick implements IInterpreteObserver {

    private final Modello modello;
    private final CalibratoreSegnale calibratoreSegnale;
    private final VistaCalibrazione vistaCalibrazione;
    private final VistaCalibrazioneClick vistaCalibrazioneClick;
    private volatile Long durataSegnaleAcquisito;

    private final Action azioneCalibraDoppioClick = new AzioneCalibraDoppioClick();

    @Inject
    public ControlloCalibrazioneDoppioClick(Interprete interprete, Modello modello, CalibratoreSegnale calibratoreSegnale, VistaCalibrazione vistaCalibrazione, VistaCalibrazioneClick vistaCalibrazioneClick) {
        this.modello = modello;
        this.calibratoreSegnale = calibratoreSegnale;
        this.vistaCalibrazione = vistaCalibrazione;
        this.vistaCalibrazioneClick = vistaCalibrazioneClick;
        interprete.addObserver(this);
    }

    @Override
    public void onDurataSegnaleAggiornata(Long durataSegnale) {
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onDurataSegnaleStatoTerminato(Long durataSegnale) {
        Map.Entry<ETipologiaEventoPersonalizzato, ETipologiaAzionePersonalizzata> stepCorrente = (Map.Entry<ETipologiaEventoPersonalizzato, ETipologiaAzionePersonalizzata>) modello.getBean(Costanti.ENTRY_EVENTO_AZIONE_CALIBRAZIONE);
        if (stepCorrente == null || stepCorrente.getValue() != ETipologiaAzionePersonalizzata.DOPPIO_CLICK) {
            return;
        }
        this.durataSegnaleAcquisito = durataSegnale;
    }

    private class AzioneCalibraDoppioClick extends AbstractAction {

        public AzioneCalibraDoppioClick() {
            this.putValue(Action.NAME, "Doppio click");
            this.putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl alt d"));
            this.putValue(MNEMONIC_KEY, KeyEvent.VK_D);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Long durataDoppioClick = durataSegnaleAcquisito;
            durataSegnaleAcquisito = null;
            if (durataDoppioClick == null) {
                return;
            }
            log.debug("Durata doppio click: {} s", durataDoppioClick / (float) Costanti.DURATA_1_SECONDO);
            calibratoreSegnale.aggiungiDurataAzione(ETipologiaAzionePersonalizzata.DOPPIO_CLICK, durataDoppioClick);
            int numeroClickEffettuati = calibratoreSegnale.numeroElementiDurateAzione(ETipologiaAzionePersonalizzata.DOPPIO_CLICK);
            vistaCalibrazioneClick.aggiornaLabelClickEffettuati(numeroClickEffettuati);
            if (numeroClickEffettuati == Costanti.NUMERO_TENTATIVI_CALIBRAZIONE) {
                vistaCalibrazione.avanzaSchermo();
                vistaCalibrazioneClick.aggiornaLabelClickEffettuati(0);
                log.debug("Calibrazione click effettuata correttamente!");
            }
        }
    }

    private final MouseListener mouseListenerDoppioClick = new MouseAdapter() {

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e)) {
                azioneCalibraDoppioClick.actionPerformed(new ActionEvent(e.getSource(), ActionEvent.ACTION_PERFORMED, "doppioClick"));
            }
        }
        
    };

}
