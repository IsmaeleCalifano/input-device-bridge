package it.unibas.inputdevicebridge.modello.interprete;

import it.unibas.inputdevicebridge.enums.ETipologiaEventoSistema;
import it.unibas.inputdevicebridge.modello.Costanti;
import it.unibas.inputdevicebridge.modello.segnale.ISegnale;
import java.awt.Toolkit;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class ScrollState implements IInterpreteState {
    
    private final int ALTEZZA_SCHERMO = Toolkit.getDefaultToolkit().getScreenSize().height;
    private final int MARGINE_ALTO = (int) (ALTEZZA_SCHERMO * 0.20);
    private final int MARGINE_BASSO = (int) (ALTEZZA_SCHERMO * 0.80);
    private long timeStampUltimoScroll;
  
    @Override
    public EsitoInterpretazione interpreta(ISegnale segnale) {
        if (segnale.getAttivo() != null) {
            //TODO: Implementa logica per segnale discreto
            log.warn("TODO: Implementa logica per segnale discreto");
            return new EsitoInterpretazione(null, ETipologiaEventoSistema.NESSUN_EVENTO);
        }
        if (segnale.getPunto() != null) {
            if (segnale.getPunto().getY() < MARGINE_ALTO) {
                this.timeStampUltimoScroll = segnale.getTimeStamp();
                return new EsitoInterpretazione(null, ETipologiaEventoSistema.SCROLL_SU);
            } else if (segnale.getPunto().getY() > MARGINE_BASSO) {
                this.timeStampUltimoScroll = segnale.getTimeStamp();
                return new EsitoInterpretazione(null, ETipologiaEventoSistema.SCROLL_GIU);
            }
            long tempoInattivo = segnale.getTimeStamp() - this.timeStampUltimoScroll;
            if (tempoInattivo > Costanti.MAX_TEMPO_INATTIVO) {
                return new EsitoInterpretazione(new AttesaState(), ETipologiaEventoSistema.NESSUN_EVENTO);
            }
        }
        return new EsitoInterpretazione(null, ETipologiaEventoSistema.NESSUN_EVENTO);
    }
    
    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
        
}
