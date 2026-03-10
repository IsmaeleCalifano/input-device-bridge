package it.unibas.inputdevicebridge.modello.interprete;

import it.unibas.inputdevicebridge.modello.segnale.ISegnale;
import it.unibas.inputdevicebridge.enums.ETipologiaEventoPersonalizzato;
import it.unibas.inputdevicebridge.enums.ETipologiaEventoSistema;
import it.unibas.inputdevicebridge.modello.Costanti;
import it.unibas.inputdevicebridge.modello.Punto;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AcquisizioneState implements IInterpreteState {
    
    private Punto ultimoPunto;
    private final long timeStampInizioAcquisizione;

    public AcquisizioneState(long timeStampInizioAcquisizione) {
        this.timeStampInizioAcquisizione = timeStampInizioAcquisizione;
    }
    
    @Override
    public EsitoInterpretazione interpreta(ISegnale segnale) {
        if (segnale.getAttivo() != null) {
            //TODO: Implementa logica per segnale discreto
            log.warn("TODO: Implementa logica per segnale discreto");
            return new EsitoInterpretazione(null, ETipologiaEventoSistema.NESSUN_EVENTO);
        }
        if (segnale.getPunto() != null) {
            if (this.ultimoPunto != null) {
                double distanzaEuclidea = segnale.getPunto().calcolaDistanzaEuclidea(this.ultimoPunto);
                if (distanzaEuclidea > Costanti.SOGLIA_AREA_MAX) {
                    long durataSegnale = segnale.getTimeStamp() - this.timeStampInizioAcquisizione;
                    if (durataSegnale >= Costanti.DURATA_1_SECONDO && durataSegnale < Costanti.DURATA_2_SECONDI) {
                        return new EsitoInterpretazione(null, ETipologiaEventoPersonalizzato.SEGNALE_BREVE);
                    } else if (durataSegnale >= Costanti.DURATA_2_SECONDI && durataSegnale < Costanti.DURATA_10_SECONDI) {
                        return new EsitoInterpretazione(null, ETipologiaEventoPersonalizzato.SEGNALE_MEDIO);
                    } else if (durataSegnale >= Costanti.DURATA_10_SECONDI && durataSegnale < Costanti.DURATA_20_SECONDI) {
                        return new EsitoInterpretazione(null, ETipologiaEventoPersonalizzato.SEGNALE_LUNGO);
                    } else {
                        return new EsitoInterpretazione(new AttesaState(), ETipologiaEventoSistema.NESSUN_EVENTO);
                    }
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
