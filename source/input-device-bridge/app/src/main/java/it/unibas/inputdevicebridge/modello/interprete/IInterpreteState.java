package it.unibas.inputdevicebridge.modello.interprete;

import it.unibas.inputdevicebridge.modello.segnale.ISegnale;

public interface IInterpreteState {

    public EsitoInterpretazione interpreta(ISegnale segnale);
    
}
