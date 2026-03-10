package it.unibas.inputdevicebridge.modello.interprete;

import it.unibas.inputdevicebridge.modello.segnale.ISegnale;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Interprete {
    
    private IInterpreteState statoCorrente;

    public Interprete() {
        this.statoCorrente = new AttesaState();
    }
    
    public EsitoInterpretazione interpreta(ISegnale segnale) {
        return this.statoCorrente.interpreta(segnale);
    }
    
}
