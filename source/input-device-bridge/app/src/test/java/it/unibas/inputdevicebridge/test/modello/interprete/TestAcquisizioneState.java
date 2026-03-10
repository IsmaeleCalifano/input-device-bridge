package it.unibas.inputdevicebridge.test.modello.interprete;

import it.unibas.inputdevicebridge.enums.ETipologiaEventoPersonalizzato;
import it.unibas.inputdevicebridge.enums.ETipologiaEventoSistema;
import it.unibas.inputdevicebridge.modello.Costanti;
import it.unibas.inputdevicebridge.modello.Punto;
import it.unibas.inputdevicebridge.modello.interprete.AcquisizioneState;
import it.unibas.inputdevicebridge.modello.interprete.AttesaState;
import it.unibas.inputdevicebridge.modello.interprete.EsitoInterpretazione;
import it.unibas.inputdevicebridge.modello.segnale.ISegnale;
import it.unibas.inputdevicebridge.modello.segnale.SegnaleContinuo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestAcquisizioneState {

    private AcquisizioneState acquisizioneState;
    private final long timeStampIniziale = 0L;

    @BeforeEach
    public void setUp() {
        this.acquisizioneState = new AcquisizioneState(this.timeStampIniziale);
    }
    
    @Test
    public void testInterpretaSegnaleIniziale() {
        EsitoInterpretazione esitoInterpretazione = this.interpretaSegnaleIniziale();
        assertNull(esitoInterpretazione.getStatoSuccessivo());
        assertEquals(ETipologiaEventoSistema.NESSUN_EVENTO, esitoInterpretazione.getTipologiaEvento());
    }

    @Test
    public void testInterpretaSegnaleTroppoBreve() {
        this.interpretaSegnaleIniziale();
        ISegnale segnaleTroppoBreve = new SegnaleContinuo(new Punto(600.0f, 600.0f), this.timeStampIniziale + Costanti.DURATA_1_SECONDO / 2);
        EsitoInterpretazione esitoInterpretazione = this.acquisizioneState.interpreta(segnaleTroppoBreve);
        assertInstanceOf(AttesaState.class, esitoInterpretazione.getStatoSuccessivo());
        assertEquals(ETipologiaEventoSistema.NESSUN_EVENTO, esitoInterpretazione.getTipologiaEvento());
    }

    @Test
    public void testInterpretaSegnaleBreve() {
        this.interpretaSegnaleIniziale();
        long timeStamp = this.timeStampIniziale + Costanti.DURATA_1_SECONDO + Costanti.DURATA_1_SECONDO / 2;
        ISegnale segnaleBreve = new SegnaleContinuo(new Punto(600.0f, 600.0f), timeStamp);
        EsitoInterpretazione esitoInterpretazione = this.acquisizioneState.interpreta(segnaleBreve);
        assertNull(esitoInterpretazione.getStatoSuccessivo());
        assertEquals(ETipologiaEventoPersonalizzato.SEGNALE_BREVE, esitoInterpretazione.getTipologiaEvento());
    }

    @Test
    public void testInterpretaSegnaleMedio() {
        this.interpretaSegnaleIniziale();
        ISegnale segnaleMedio = new SegnaleContinuo(new Punto(600.0f, 600.0f), this.timeStampIniziale + 3 * Costanti.DURATA_1_SECONDO);
        EsitoInterpretazione esitoInterpretazione = this.acquisizioneState.interpreta(segnaleMedio);
        assertNull(esitoInterpretazione.getStatoSuccessivo());
        assertEquals(ETipologiaEventoPersonalizzato.SEGNALE_MEDIO, esitoInterpretazione.getTipologiaEvento());
    }
    
    @Test
    public void testInterpretaSegnaleLungo() {
        this.interpretaSegnaleIniziale();
        ISegnale segnaleLungo = new SegnaleContinuo(new Punto(600.0f, 600.0f), this.timeStampIniziale + Costanti.DURATA_10_SECONDI);
        EsitoInterpretazione esitoInterpretazione = this.acquisizioneState.interpreta(segnaleLungo);
        assertNull(esitoInterpretazione.getStatoSuccessivo());
        assertEquals(ETipologiaEventoPersonalizzato.SEGNALE_LUNGO, esitoInterpretazione.getTipologiaEvento());
    }
    
    @Test
    public void testInterpretaSegnaleTroppoLungo() {
        this.interpretaSegnaleIniziale();
        ISegnale segnaleTroppoLungo = new SegnaleContinuo(new Punto(600.0f, 600.0f), this.timeStampIniziale + Costanti.DURATA_20_SECONDI);
        EsitoInterpretazione esitoInterpretazione = this.acquisizioneState.interpreta(segnaleTroppoLungo);
        assertInstanceOf(AttesaState.class, esitoInterpretazione.getStatoSuccessivo());
        assertEquals(ETipologiaEventoSistema.NESSUN_EVENTO, esitoInterpretazione.getTipologiaEvento());
    }
    
    private EsitoInterpretazione interpretaSegnaleIniziale() {
        ISegnale segnaleIniziale = new SegnaleContinuo(new Punto(100.0f, 100.0f), this.timeStampIniziale);
        return this.acquisizioneState.interpreta(segnaleIniziale);
    }
    
}

