package it.unibas.inputdevicebridge.test.modello.interprete;

import it.unibas.inputdevicebridge.enums.ETipologiaEventoSistema;
import it.unibas.inputdevicebridge.modello.Costanti;
import it.unibas.inputdevicebridge.modello.Punto;
import it.unibas.inputdevicebridge.modello.interprete.AttesaState;
import it.unibas.inputdevicebridge.modello.interprete.EsitoInterpretazione;
import it.unibas.inputdevicebridge.modello.interprete.ScrollState;
import it.unibas.inputdevicebridge.modello.segnale.ISegnale;
import it.unibas.inputdevicebridge.modello.segnale.SegnaleContinuo;
import java.awt.Toolkit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestScrollState {

    private ScrollState scrollState;
    private int altezzaSchermo;
    private float centroSchermo;
    private final long timeStampIniziale = 0L;

    @BeforeEach
    public void setUp() {
        this.altezzaSchermo = Toolkit.getDefaultToolkit().getScreenSize().height;
        this.centroSchermo = altezzaSchermo / 2.0f;
        this.scrollState = new ScrollState();
    }

    @Test
    public void testInterpretaSegnaleScrollSu() {
        ISegnale segnaleScrollSu = new SegnaleContinuo(new Punto(100.0f, 0.0f), this.timeStampIniziale);
        EsitoInterpretazione esitoInterpretazione = this.scrollState.interpreta(segnaleScrollSu);
        assertNull(esitoInterpretazione.getStatoSuccessivo());
        assertEquals(ETipologiaEventoSistema.SCROLL_SU, esitoInterpretazione.getTipologiaEvento());
    }

    @Test
    public void testInterpretaSegnaleScrollGiu() {
        ISegnale segnaleScrollGiu = new SegnaleContinuo(new Punto(100.0f, (float) this.altezzaSchermo), this.timeStampIniziale);
        EsitoInterpretazione esitoInterpretazione = this.scrollState.interpreta(segnaleScrollGiu);
        assertNull(esitoInterpretazione.getStatoSuccessivo());
        assertEquals(ETipologiaEventoSistema.SCROLL_GIU, esitoInterpretazione.getTipologiaEvento());
    }
    
    @Test
    public void testInterpretaSegnaleAlCentroTroppoBreve() {
        ISegnale segnaleAlCentroTroppoBreve = new SegnaleContinuo(new Punto(100.0f, this.centroSchermo), this.timeStampIniziale);
        EsitoInterpretazione esitoInterpretazione = this.scrollState.interpreta(segnaleAlCentroTroppoBreve);
        assertNull(esitoInterpretazione.getStatoSuccessivo());
        assertEquals(ETipologiaEventoSistema.NESSUN_EVENTO, esitoInterpretazione.getTipologiaEvento());
    }
   
    @Test
    public void testInterpretaSegnaleAlCentro() {
        ISegnale segnaleAlCentro = new SegnaleContinuo(new Punto(100.0f, this.centroSchermo), this.timeStampIniziale);
        this.scrollState.interpreta(segnaleAlCentro);
        segnaleAlCentro = new SegnaleContinuo(new Punto(100.0f, this.centroSchermo), this.timeStampIniziale + Costanti.DURATA_1_SECONDO + Costanti.MAX_TEMPO_INATTIVO);
        EsitoInterpretazione esitoInterpretazione = this.scrollState.interpreta(segnaleAlCentro);
        assertInstanceOf(AttesaState.class, esitoInterpretazione.getStatoSuccessivo());
        assertEquals(ETipologiaEventoSistema.NESSUN_EVENTO, esitoInterpretazione.getTipologiaEvento());
    }
    
}
