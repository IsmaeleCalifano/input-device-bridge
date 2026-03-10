package it.unibas.inputdevicebridge.modello.interprete;

import it.unibas.inputdevicebridge.enums.ITipologiaEvento;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EsitoInterpretazione {
    
    private final IInterpreteState statoSuccessivo;
    private final ITipologiaEvento tipologiaEvento;
    
}
