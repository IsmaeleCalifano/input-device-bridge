package it.unibas.inputdevicebridge.test.modello.interprete;

import it.unibas.inputdevicebridge.enums.ETipologiaEventoSistema;
import it.unibas.inputdevicebridge.modello.Punto;
import it.unibas.inputdevicebridge.modello.interprete.AcquisizioneState;
import it.unibas.inputdevicebridge.modello.interprete.AttesaState;
import it.unibas.inputdevicebridge.modello.interprete.EsitoInterpretazione;
import it.unibas.inputdevicebridge.modello.segnale.ISegnale;
import it.unibas.inputdevicebridge.modello.segnale.SegnaleContinuo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestAttesaState {

    private AttesaState attesaState;
    private final long timeStampIniziale = 0L;

    @BeforeEach
    public void setUp() {
        this.attesaState = new AttesaState();
    }

    @Test
    public void testInterpretaSegnaleIniziale() {
        EsitoInterpretazione esitoInterpretazione = this.interpretaSegnaleIniziale();
        assertNull(esitoInterpretazione.getStatoSuccessivo());
        assertEquals(ETipologiaEventoSistema.NESSUN_EVENTO, esitoInterpretazione.getTipologiaEvento());
    }

    @Test
    public void testInterpretaSegnaleStabile() {
        this.interpretaSegnaleIniziale();
        ISegnale segnaleStabile = new SegnaleContinuo(new Punto(100.2f, 100.2f), this.timeStampIniziale + 50L);
        EsitoInterpretazione esitoInterpretazione = this.attesaState.interpreta(segnaleStabile);
        assertInstanceOf(AcquisizioneState.class, esitoInterpretazione.getStatoSuccessivo());
        assertEquals(ETipologiaEventoSistema.NESSUN_EVENTO, esitoInterpretazione.getTipologiaEvento());
    }
    
    @Test
    public void testInterpretaSegnaleDinamico() {
        this.interpretaSegnaleIniziale();
        ISegnale segnaleDinamico = new SegnaleContinuo(new Punto(102.0f, 102.0f), this.timeStampIniziale + 50L);
        EsitoInterpretazione esitoInterpretazione = this.attesaState.interpreta(segnaleDinamico);
        assertNull(esitoInterpretazione.getStatoSuccessivo());
        assertEquals(ETipologiaEventoSistema.NESSUN_EVENTO, esitoInterpretazione.getTipologiaEvento());
    }
    
    private EsitoInterpretazione interpretaSegnaleIniziale() {
        ISegnale segnaleIniziale = new SegnaleContinuo(new Punto(100.0f, 100.0f), this.timeStampIniziale);
        return this.attesaState.interpreta(segnaleIniziale);
    }
    
}
