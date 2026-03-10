package it.unibas.inputdevicebridge.modello.interprete;

import it.unibas.inputdevicebridge.enums.ETipologiaEventoSistema;
import it.unibas.inputdevicebridge.modello.Costanti;
import it.unibas.inputdevicebridge.modello.Punto;
import it.unibas.inputdevicebridge.modello.segnale.ISegnale;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TrascinamentoState implements IInterpreteState {
    
    private Punto ultimoPunto;
    private long timeStampSegnaleStabile;

    public TrascinamentoState(long timeStampSegnaleStabile) {
        this.timeStampSegnaleStabile = timeStampSegnaleStabile;
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
                if (distanzaEuclidea <= Costanti.SOGLIA_SEGNALE_STABILE) {
                    long durataSegnale = segnale.getTimeStamp() - this.timeStampSegnaleStabile;
                    if (durataSegnale >= Costanti.DURATA_2_SECONDI) {
                        return new EsitoInterpretazione(new AttesaState(), ETipologiaEventoSistema.RILASCIO);
                    }
                } else {
                    this.timeStampSegnaleStabile = segnale.getTimeStamp();
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
