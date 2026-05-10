package it.unibas.inputdevicebridge.modello.interprete;

import it.unibas.inputdevicebridge.enums.ETipologiaEventoSistema;
import it.unibas.inputdevicebridge.modello.Costanti;
import it.unibas.inputdevicebridge.modello.segnale.ISegnale;
import java.awt.Toolkit;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ScrollState implements IInterpreteState {
    
    private final int ALTEZZA_SCHERMO = Toolkit.getDefaultToolkit().getScreenSize().height;
    private final int MARGINE_ALTO = (int) (ALTEZZA_SCHERMO * 0.20);
    private final int MARGINE_BASSO = (int) (ALTEZZA_SCHERMO * 0.80);
    private Long timeStampUltimaAttivita;
    private boolean direzioneScrollGiu;

    public ScrollState() {
        this.direzioneScrollGiu = true;
    }
  
    @Override
    public EsitoInterpretazione interpreta(ISegnale segnale) {
        if (segnale.getIntensita() != null) {
            if (Math.abs(segnale.getIntensita()) > Costanti.SOGLIA_SEGNALE_STABILE) {
                if (this.timeStampUltimaAttivita == null) {
                    this.timeStampUltimaAttivita = segnale.getTimeStamp();
                } else {
                    long durataSegnale = segnale.getTimeStamp() - this.timeStampUltimaAttivita;
                    if (durataSegnale >= Costanti.DURATA_2_SECONDI) {
                        if (this.direzioneScrollGiu) {
                            return new EsitoInterpretazione(null, ETipologiaEventoSistema.SCROLL_GIU);
                        }
                        return new EsitoInterpretazione(null, ETipologiaEventoSistema.SCROLL_SU);
                    }
                }
            } else {
                if (this.timeStampUltimaAttivita != null) {
                    long durataSegnale = segnale.getTimeStamp() - this.timeStampUltimaAttivita;
                    this.timeStampUltimaAttivita = null;
                    if (durataSegnale < Costanti.DURATA_1_SECONDO) {
                        this.direzioneScrollGiu = !this.direzioneScrollGiu;
                        log.info("Direzione scroll invertita!");
                    } else if (durataSegnale >= Costanti.DURATA_1_SECONDO && durataSegnale < Costanti.DURATA_2_SECONDI) {
                        return new EsitoInterpretazione(new AttesaState(), ETipologiaEventoSistema.NESSUN_EVENTO);
                    }
                }
                
            }
        }
        if (segnale.getPunto() != null) {
            if (this.timeStampUltimaAttivita == null) {
                this.timeStampUltimaAttivita = segnale.getTimeStamp();
            }
            if (segnale.getPunto().getY() < MARGINE_ALTO) {
                this.timeStampUltimaAttivita = segnale.getTimeStamp();
                return new EsitoInterpretazione(null, ETipologiaEventoSistema.SCROLL_SU);
            } else if (segnale.getPunto().getY() > MARGINE_BASSO) {
                this.timeStampUltimaAttivita = segnale.getTimeStamp();
                return new EsitoInterpretazione(null, ETipologiaEventoSistema.SCROLL_GIU);
            }
            long tempoInattivo = segnale.getTimeStamp() - this.timeStampUltimaAttivita;
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
