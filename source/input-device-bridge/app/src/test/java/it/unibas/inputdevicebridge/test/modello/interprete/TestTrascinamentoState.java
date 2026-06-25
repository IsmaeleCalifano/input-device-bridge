package it.unibas.inputdevicebridge.test.modello.interprete;

import it.unibas.inputdevicebridge.enums.ETipologiaEventoSistema;
import it.unibas.inputdevicebridge.modello.Costanti;
import it.unibas.inputdevicebridge.modello.Punto;
import it.unibas.inputdevicebridge.modello.interprete.AttesaState;
import it.unibas.inputdevicebridge.modello.interprete.EsitoInterpretazione;
import it.unibas.inputdevicebridge.modello.interprete.TrascinamentoState;
import it.unibas.inputdevicebridge.modello.segnale.ISegnale;
import it.unibas.inputdevicebridge.modello.segnale.SegnaleContinuo;
import it.unibas.inputdevicebridge.modello.segnale.SegnaleDiscreto;
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
    
    // ---------- TEST SEGNALE CONTINUO ----------

    @Test
    public void testInterpretaSegnaleInizialeContinuo() {
        EsitoInterpretazione esitoInterpretazione = this.interpretaSegnaleInizialeContinuo();
        assertNull(esitoInterpretazione.getStatoSuccessivo());
        assertEquals(ETipologiaEventoSistema.NESSUN_EVENTO, esitoInterpretazione.getTipologiaEvento());
    }
    
    @Test
    public void testInterpretaSegnaleStabileBreveContinuo() {
        this.interpretaSegnaleInizialeContinuo();
        ISegnale segnaleStabileBreve = new SegnaleContinuo(new Punto(100.0f, 100.0f), Costanti.DURATA_1_SECONDO);
        EsitoInterpretazione esitoInterpretazione = this.trascinamentoState.interpreta(segnaleStabileBreve);
        assertNull(esitoInterpretazione.getStatoSuccessivo());
        assertEquals(ETipologiaEventoSistema.NESSUN_EVENTO, esitoInterpretazione.getTipologiaEvento());
    }
    
    @Test
    public void testInterpretaSegnaleDinamicoContinuo() {
        this.interpretaSegnaleInizialeContinuo();
        ISegnale segnaleDinamico = new SegnaleContinuo(new Punto(150.0f, 150.0f), this.timeStampIniziale + Costanti.DURATA_1_SECONDO);
        EsitoInterpretazione esitoInterpretazione = this.trascinamentoState.interpreta(segnaleDinamico);
        assertNull(esitoInterpretazione.getStatoSuccessivo());
        assertEquals(ETipologiaEventoSistema.NESSUN_EVENTO, esitoInterpretazione.getTipologiaEvento());
    }
    
    @Test
    public void testInterpretaSegnaleRilascioContinuo() {
        this.interpretaSegnaleInizialeContinuo();
        ISegnale segnaleRilascio = new SegnaleContinuo(new Punto(100.0f, 100.0f), this.timeStampIniziale +  3 * Costanti.DURATA_1_SECONDO);
        EsitoInterpretazione esitoInterpretazione = this.trascinamentoState.interpreta(segnaleRilascio);
        assertInstanceOf(AttesaState.class, esitoInterpretazione.getStatoSuccessivo());
        assertEquals(ETipologiaEventoSistema.RILASCIO, esitoInterpretazione.getTipologiaEvento());
    }
    
    private EsitoInterpretazione interpretaSegnaleInizialeContinuo() {
        ISegnale segnaleIniziale = new SegnaleContinuo(new Punto(100.0f, 100.0f), this.timeStampIniziale);
        return this.trascinamentoState.interpreta(segnaleIniziale);
    }

    // ---------- TEST SEGNALE DISCRETO ----------

    @Test
    public void testInterpretaSegnaleAvviaMovimentoDiscreto() {
        long timeStamp = this.timeStampIniziale + Costanti.VELOCITA_SCANSIONE + 10L;
        ISegnale segnale = new SegnaleDiscreto(Costanti.SOGLIA_SEGNALE_STABILE - 1.0f, timeStamp);
        EsitoInterpretazione esitoInterpretazione = this.trascinamentoState.interpreta(segnale);
        assertNull(esitoInterpretazione.getStatoSuccessivo());
        assertEquals(ETipologiaEventoSistema.AVVIA_MOVIMENTO, esitoInterpretazione.getTipologiaEvento());
    }

    @Test
    public void testInterpretaSegnaleCompletaSelezioneRilascioDiscreto() {
        long timeStamp = this.timeStampIniziale + Costanti.VELOCITA_SCANSIONE + 10L;
        ISegnale segnaleY = new SegnaleDiscreto(Costanti.SOGLIA_SEGNALE_STABILE + 10.0f, timeStamp);
        this.trascinamentoState.interpreta(segnaleY);
        timeStamp = this.timeStampIniziale + Costanti.DURATA_2_SECONDI + 500L; 
        ISegnale segnaleX = new SegnaleDiscreto(Costanti.SOGLIA_SEGNALE_STABILE + 10.0f, timeStamp);
        EsitoInterpretazione esitoInterpretazione = this.trascinamentoState.interpreta(segnaleX);
        assertInstanceOf(AttesaState.class, esitoInterpretazione.getStatoSuccessivo());
        assertEquals(ETipologiaEventoSistema.RILASCIO, esitoInterpretazione.getTipologiaEvento());
    }
    
}
