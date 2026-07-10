package it.unibas.inputdevicebridge.modello.interprete;

import it.unibas.inputdevicebridge.enums.ITipologiaEvento;
import it.unibas.inputdevicebridge.modello.Punto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EsitoInterpretazione {
    
    private final IInterpreteState statoSuccessivo;
    private final ITipologiaEvento tipologiaEvento;
    private Punto puntoAzione;
    private Long durataSegnale;
    
    public EsitoInterpretazione(IInterpreteState statoSuccessivo, ITipologiaEvento tipologiaEvento) {
        this.statoSuccessivo = statoSuccessivo;
        this.tipologiaEvento = tipologiaEvento;
    }

    public EsitoInterpretazione(IInterpreteState statoSuccessivo, ITipologiaEvento tipologiaEvento, Punto puntoAzione) {
        this.statoSuccessivo = statoSuccessivo;
        this.tipologiaEvento = tipologiaEvento;
        this.puntoAzione = puntoAzione;
    }

    public EsitoInterpretazione(IInterpreteState statoSuccessivo, ITipologiaEvento tipologiaEvento, Long durataSegnale) {
        this.statoSuccessivo = statoSuccessivo;
        this.tipologiaEvento = tipologiaEvento;
        this.durataSegnale = durataSegnale;
    }
    
}
