package it.unibas.inputdevicebridge.controllo;

import it.unibas.inputdevicebridge.vista.VistaAreaTest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import static javax.swing.Action.ACCELERATOR_KEY;
import static javax.swing.Action.MNEMONIC_KEY;
import javax.swing.KeyStroke;
import lombok.Getter;

@Getter
@ApplicationScoped
public class ControlloAreaTest {
    
    private final VistaAreaTest vistaAreaTest;

    private final Action azioneClicca = new AzioneClicca();

    @Inject
    public ControlloAreaTest(VistaAreaTest vistaAreaTest) {
        this.vistaAreaTest = vistaAreaTest;
    }

    private class AzioneClicca extends AbstractAction {

        public AzioneClicca() {
            this.putValue(Action.NAME, "Clicca");
            this.putValue(Action.SHORT_DESCRIPTION, "Cliccando incrementa il numero di click");
            this.putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl alt c"));
            this.putValue(MNEMONIC_KEY, KeyEvent.VK_C);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            int numeroClick = vistaAreaTest.getNumeroClick();
            vistaAreaTest.setNumeroClick(++numeroClick);
            vistaAreaTest.aggiornaLabelNumeroClick();
        }

    }

}
