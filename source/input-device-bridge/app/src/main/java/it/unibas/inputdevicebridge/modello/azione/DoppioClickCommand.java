package it.unibas.inputdevicebridge.modello.azione;

import it.unibas.inputdevicebridge.modello.interprete.AttesaState;
import it.unibas.inputdevicebridge.modello.interprete.IInterpreteState;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DoppioClickCommand implements IAzioneCommand {

    @Override
    public void esegui() {
        log.info(">> Eseguo azione DOPPIO CLICK. <<");
        for (int i = 0; i < 2; i++) {
            IniettoreAzione.getInstance().premiClickSinistro();
            IniettoreAzione.getInstance().rilasciaClickSinistro();
        }
    }

    @Override
    public IInterpreteState getStatoSuccessivo() {
        return new AttesaState();
    }

}
