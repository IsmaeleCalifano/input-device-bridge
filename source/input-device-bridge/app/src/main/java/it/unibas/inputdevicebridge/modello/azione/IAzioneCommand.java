package it.unibas.inputdevicebridge.modello.azione;

import it.unibas.inputdevicebridge.modello.interprete.IInterpreteState;

public interface IAzioneCommand {

    public void esegui();
    public IInterpreteState getStatoSuccessivo();
    
}
