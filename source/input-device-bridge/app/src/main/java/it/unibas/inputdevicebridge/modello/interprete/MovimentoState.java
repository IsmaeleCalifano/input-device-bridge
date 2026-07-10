package it.unibas.inputdevicebridge.modello.interprete;

import io.quarkus.arc.Arc;
import it.unibas.inputdevicebridge.enums.ETipologiaEventoSistema;
import it.unibas.inputdevicebridge.modello.Costanti;
import it.unibas.inputdevicebridge.modello.Modello;
import it.unibas.inputdevicebridge.modello.Punto;
import it.unibas.inputdevicebridge.modello.segnale.ISegnale;
import java.awt.Dimension;
import java.awt.Toolkit;
import static java.lang.Math.abs;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MovimentoState implements IInterpreteState {
    
    Dimension dimensioniSchermo = Toolkit.getDefaultToolkit().getScreenSize(); 
    private final float MAX_X = (float) dimensioniSchermo.getWidth();
    private final float MAX_Y = (float) dimensioniSchermo.getHeight();
    private float yNormalizzato = 0.0f;
    private float xNormalizzato = 0.0f;
    private boolean faseScansioneY = true;
    private long timeStampUltimoSpostamento = 0;
    private boolean segnaleAttivoPrecedente = false;
    
    @Override
    public EsitoInterpretazione interpreta(ISegnale segnale) {
        long timeStampAttuale = segnale.getTimeStamp();
        boolean segnaleStabile = (segnale.getIntensita() != null) && (abs(segnale.getIntensita()) > Costanti.SOGLIA_SEGNALE_STABILE);
        float coordXReale = this.xNormalizzato * MAX_X;
        float coordYReale = this.yNormalizzato * MAX_Y;
        if (segnaleStabile && !this.segnaleAttivoPrecedente) {
            this.segnaleAttivoPrecedente = true;
            this.timeStampUltimoSpostamento = timeStampAttuale; 
            if (this.faseScansioneY) {
                log.info("Coordinata Y bloccata a: {}", coordYReale);
                this.faseScansioneY = false;
                return new EsitoInterpretazione(null, ETipologiaEventoSistema.NESSUN_EVENTO);
            } else {
                Punto puntoSelezionato = new Punto(coordXReale, coordYReale);
                log.info("Punto selezionato: ({})", puntoSelezionato);
                return new EsitoInterpretazione(null, ETipologiaEventoSistema.FERMA_MOVIMENTO);
            }
        }
        if (!segnaleStabile) {
            this.segnaleAttivoPrecedente = false;
        }
        if (this.segnaleAttivoPrecedente) {
            this.timeStampUltimoSpostamento = timeStampAttuale;
            return new EsitoInterpretazione(null, ETipologiaEventoSistema.NESSUN_EVENTO);
        }
        if ((timeStampAttuale - this.timeStampUltimoSpostamento) > Costanti.VELOCITA_SCANSIONE) {
            this.timeStampUltimoSpostamento = timeStampAttuale;
            if (this.faseScansioneY) {
                this.yNormalizzato += Costanti.PASSO_GRIGLIA;
                if (this.yNormalizzato > 1.0f) {
                    this.yNormalizzato = 0.0f;
                }
            } else {
                this.xNormalizzato += Costanti.PASSO_GRIGLIA;
                if (this.xNormalizzato > 1.0f) {
                    this.xNormalizzato = 0.0f;
                }
            }
            coordXReale = this.xNormalizzato * MAX_X;
            coordYReale = this.yNormalizzato * MAX_Y;
            Modello modello = Arc.container().select(Modello.class).get();
            modello.putBean(Costanti.PUNTO_MOVIMENTO, new Punto(coordXReale, coordYReale));
            return new EsitoInterpretazione(null, ETipologiaEventoSistema.AVVIA_MOVIMENTO);
        }
        return new EsitoInterpretazione(null, ETipologiaEventoSistema.NESSUN_EVENTO);
    }
}