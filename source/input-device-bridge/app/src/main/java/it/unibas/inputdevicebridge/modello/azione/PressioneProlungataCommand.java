package it.unibas.inputdevicebridge.modello.azione;

import it.unibas.inputdevicebridge.modello.interprete.TrascinamentoState;
import it.unibas.inputdevicebridge.modello.interprete.IInterpreteState;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PressioneProlungataCommand implements IAzioneCommand {

    @Override
    public void esegui() {
        log.info(">> Eseguo azione INIZIO TRASCINAMENTO (Pressione prolungata). <<");
    }

    @Override
    public IInterpreteState getStatoSuccessivo() {
        return new TrascinamentoState(System.nanoTime());
    }
    
}
