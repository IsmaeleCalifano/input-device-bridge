package it.unibas.inputdevicebridge.modello.interprete;

import it.unibas.inputdevicebridge.Applicazione;
import it.unibas.inputdevicebridge.enums.ETipologiaEventoSistema;
import it.unibas.inputdevicebridge.modello.Costanti;
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
    private float y = 0.0f;
    private float x = 0.0f;
    private boolean faseScansioneY = true;
    private long timeStampUltimoSpostamento = 0;

    @Override
    public EsitoInterpretazione interpreta(ISegnale segnale) {
        long timeStampAttuale = segnale.getTimeStamp();
        if ((timeStampAttuale - this.timeStampUltimoSpostamento) > Costanti.VELOCITA_SCANSIONE) {
            if (segnale.getIntensita() != null && abs(segnale.getIntensita()) > Costanti.SOGLIA_SEGNALE_STABILE) {
                if (this.faseScansioneY) {
                    log.info("Coordinata Y bloccata a: {}", this.y);
                    this.faseScansioneY = false;
                    return new EsitoInterpretazione(null, ETipologiaEventoSistema.NESSUN_EVENTO);
                } else {
                    Punto puntoSelezionato = new Punto(this.x, this.y);
                    log.info("Punto selezionato: ({})", puntoSelezionato);
                    return new EsitoInterpretazione(null, ETipologiaEventoSistema.FERMA_MOVIMENTO);
                }
            }
            this.timeStampUltimoSpostamento = timeStampAttuale;
            if (this.faseScansioneY) {
                this.y += Costanti.PASSO_GRIGLIA;
                if (this.y > MAX_Y) {
                    this.y = 0.0f;
                }
            } else {
                this.x += Costanti.PASSO_GRIGLIA;
                if (this.x > MAX_X) {
                    this.x = 0.0f;
                }
            }
            Applicazione.getInstance().getModello().putBean(Costanti.PUNTO_MOVIMENTO, new Punto(this.x, this.y));
            return new EsitoInterpretazione(null, ETipologiaEventoSistema.AVVIA_MOVIMENTO);
        }
        return new EsitoInterpretazione(null, ETipologiaEventoSistema.NESSUN_EVENTO);
    }
}
