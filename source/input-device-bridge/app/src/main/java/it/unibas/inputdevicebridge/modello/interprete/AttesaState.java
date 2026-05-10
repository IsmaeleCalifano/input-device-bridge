package it.unibas.inputdevicebridge.modello.interprete;

import it.unibas.inputdevicebridge.enums.ETipologiaEventoSistema;
import it.unibas.inputdevicebridge.modello.Costanti;
import it.unibas.inputdevicebridge.modello.Punto;
import it.unibas.inputdevicebridge.modello.segnale.ISegnale;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AttesaState implements IInterpreteState {
    
    private Punto ultimoPunto;
    private MovimentoState movimentoState;

    public AttesaState() {
        this.movimentoState = new MovimentoState();
    }

    @Override
    public EsitoInterpretazione interpreta(ISegnale segnale) {
        if (segnale.getIntensita()!= null) {
            EsitoInterpretazione esitoInterpretazioneMovimento = movimentoState.interpreta(segnale);
            if (esitoInterpretazioneMovimento.getTipologiaEvento() == ETipologiaEventoSistema.FERMA_MOVIMENTO) {
                return new EsitoInterpretazione(new AcquisizioneState(segnale.getTimeStamp()), ETipologiaEventoSistema.NESSUN_EVENTO);
            }
            return esitoInterpretazioneMovimento;
        }
        if (segnale.getPunto() != null) {
            if (this.ultimoPunto != null) {
                double distanzaEuclidea = segnale.getPunto().calcolaDistanzaEuclidea(this.ultimoPunto);
                if (distanzaEuclidea <= Costanti.SOGLIA_SEGNALE_STABILE) {
                    return new EsitoInterpretazione(new AcquisizioneState(segnale.getTimeStamp()), ETipologiaEventoSistema.NESSUN_EVENTO);
                }
            }
            this.ultimoPunto = segnale.getPunto();
        }
        return new EsitoInterpretazione(null, ETipologiaEventoSistema.NESSUN_EVENTO);
    }
    
    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
    
}
