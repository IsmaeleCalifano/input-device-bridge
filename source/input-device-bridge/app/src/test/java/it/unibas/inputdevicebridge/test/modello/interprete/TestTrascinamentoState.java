package it.unibas.inputdevicebridge.test.modello.interprete;

import it.unibas.inputdevicebridge.enums.ETipologiaEventoSistema;
import it.unibas.inputdevicebridge.modello.Costanti;
import it.unibas.inputdevicebridge.modello.Punto;
import it.unibas.inputdevicebridge.modello.interprete.AttesaState;
import it.unibas.inputdevicebridge.modello.interprete.EsitoInterpretazione;
import it.unibas.inputdevicebridge.modello.interprete.TrascinamentoState;
import it.unibas.inputdevicebridge.modello.segnale.ISegnale;
import it.unibas.inputdevicebridge.modello.segnale.SegnaleContinuo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestTrascinamentoState {

    private TrascinamentoState trascinamentoState;
    private final long timeStampIniziale = 0L;

    @BeforeEach
    public void setUp() {
        this.trascinamentoState = new TrascinamentoState(this.timeStampIniziale);
    }
    
    @Test
    public void testInterpretaSegnaleIniziale() {
        EsitoInterpretazione esitoInterpretazione = this.interpretaSegnaleIniziale();
        assertNull(esitoInterpretazione.getStatoSuccessivo());
        assertEquals(ETipologiaEventoSistema.NESSUN_EVENTO, esitoInterpretazione.getTipologiaEvento());
    }
    
    @Test
    public void testInterpretaSegnaleStabileBreve() {
        this.interpretaSegnaleIniziale();
        ISegnale segnaleStabileBreve = new SegnaleContinuo(new Punto(100.0f, 100.0f), Costanti.DURATA_1_SECONDO);
        EsitoInterpretazione esitoInterpretazione = this.trascinamentoState.interpreta(segnaleStabileBreve);
        assertNull(esitoInterpretazione.getStatoSuccessivo());
        assertEquals(ETipologiaEventoSistema.NESSUN_EVENTO, esitoInterpretazione.getTipologiaEvento());
    }
    
    @Test
    public void testInterpretaSegnaleDinamico() {
        this.interpretaSegnaleIniziale();
        ISegnale segnaleDinamico = new SegnaleContinuo(new Punto(150.0f, 150.0f), this.timeStampIniziale + Costanti.DURATA_1_SECONDO);
        EsitoInterpretazione esitoInterpretazione = this.trascinamentoState.interpreta(segnaleDinamico);
        assertNull(esitoInterpretazione.getStatoSuccessivo());
        assertEquals(ETipologiaEventoSistema.NESSUN_EVENTO, esitoInterpretazione.getTipologiaEvento());
    }
    
    @Test
    public void testInterpretaSegnaleRilascio() {
        this.interpretaSegnaleIniziale();
        ISegnale segnaleRilascio = new SegnaleContinuo(new Punto(100.0f, 100.0f), this.timeStampIniziale +  3 * Costanti.DURATA_1_SECONDO);
        EsitoInterpretazione esitoInterpretazione = this.trascinamentoState.interpreta(segnaleRilascio);
        assertInstanceOf(AttesaState.class, esitoInterpretazione.getStatoSuccessivo());
        assertEquals(ETipologiaEventoSistema.RILASCIO, esitoInterpretazione.getTipologiaEvento());
    }
    
    private EsitoInterpretazione interpretaSegnaleIniziale() {
        ISegnale segnaleIniziale = new SegnaleContinuo(new Punto(100.0f, 100.0f), this.timeStampIniziale);
        return this.trascinamentoState.interpreta(segnaleIniziale);
    }
    
}
