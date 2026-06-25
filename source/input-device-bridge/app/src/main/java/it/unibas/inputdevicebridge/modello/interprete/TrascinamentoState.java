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
    private final MovimentoState movimentoState;

    public TrascinamentoState(long timeStampSegnaleStabile) {
        this.timeStampSegnaleStabile = timeStampSegnaleStabile;
        this.movimentoState = new MovimentoState();
    }

    @Override
    public EsitoInterpretazione interpreta(ISegnale segnale) {
        if (segnale.getIntensita() != null) {
            EsitoInterpretazione esitoInterpretazioneMovimento = movimentoState.interpreta(segnale);
            if (esitoInterpretazioneMovimento.getTipologiaEvento() == ETipologiaEventoSistema.FERMA_MOVIMENTO) {
               return new EsitoInterpretazione(new AttesaState(), ETipologiaEventoSistema.RILASCIO);
            }
            return esitoInterpretazioneMovimento;
        }
        if (segnale.getPunto() != null) {
            if (this.ultimoPunto != null) {
                double distanzaEuclidea = segnale.getPunto().calcolaDistanzaEuclidea(this.ultimoPunto);
                if (distanzaEuclidea <= Costanti.SOGLIA_SEGNALE_STABILE) {
                    EsitoInterpretazione esitoInterpretazione = this.esitoInterpretazioneSegnaleDiscreto(segnale);
                    if (esitoInterpretazione.getTipologiaEvento() == ETipologiaEventoSistema.RILASCIO) {
                        return esitoInterpretazione;
                    }
                } else {
                    this.timeStampSegnaleStabile = segnale.getTimeStamp();
                }

            }
            this.ultimoPunto = segnale.getPunto();
        }
        return new EsitoInterpretazione(null, ETipologiaEventoSistema.NESSUN_EVENTO);
    }
    
    private EsitoInterpretazione esitoInterpretazioneSegnaleDiscreto(ISegnale segnale) {
        long durataSegnale = segnale.getTimeStamp() - this.timeStampSegnaleStabile;
        if (durataSegnale >= Costanti.DURATA_2_SECONDI) {
            return new EsitoInterpretazione(new AttesaState(), ETipologiaEventoSistema.RILASCIO);
        }
        return new EsitoInterpretazione(null, ETipologiaEventoSistema.NESSUN_EVENTO);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
    
}
