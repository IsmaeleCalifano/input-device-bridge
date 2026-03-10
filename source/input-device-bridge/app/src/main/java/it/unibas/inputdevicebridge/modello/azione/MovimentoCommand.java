package it.unibas.inputdevicebridge.modello.azione;

import it.unibas.inputdevicebridge.modello.Punto;
import it.unibas.inputdevicebridge.modello.interprete.IInterpreteState;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class MovimentoCommand implements IAzioneCommand {
    
    private final Punto punto;

    @Override
    public void esegui() {
        log.debug("Sto muovendo il cursore nelle cordinate ({})", this.punto);
    }

    @Override
    public IInterpreteState getStatoSuccessivo() {
        return null;
    }
    
}
