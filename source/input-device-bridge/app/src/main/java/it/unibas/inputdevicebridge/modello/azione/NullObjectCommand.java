package it.unibas.inputdevicebridge.modello.azione;

import it.unibas.inputdevicebridge.modello.interprete.IInterpreteState;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NullObjectCommand implements IAzioneCommand  {
    

    @Override
    public void esegui() {
        log.debug(">> Nessuna azione eseguita. <<");
    }

    @Override
    public IInterpreteState getStatoSuccessivo() {
        return null;
    }
    
}
