package it.unibas.inputdevicebridge.modello.azione;

import it.unibas.inputdevicebridge.modello.interprete.AttesaState;
import it.unibas.inputdevicebridge.modello.interprete.IInterpreteState;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ClickCommand implements IAzioneCommand {

    @Override
    public void esegui() {
        log.info(">> Eseguo azione CLICK. <<");
    }

    @Override
    public IInterpreteState getStatoSuccessivo() {
        return new AttesaState();
    }
    
}
