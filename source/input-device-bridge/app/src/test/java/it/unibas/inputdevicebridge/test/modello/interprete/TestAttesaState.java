package it.unibas.inputdevicebridge.test.modello.interprete;

import io.quarkus.test.junit.QuarkusTest;
import it.unibas.inputdevicebridge.enums.ETipologiaEventoSistema;
import it.unibas.inputdevicebridge.modello.Costanti;
import it.unibas.inputdevicebridge.modello.Modello;
import it.unibas.inputdevicebridge.modello.Punto;
import it.unibas.inputdevicebridge.modello.interprete.AcquisizioneState;
import it.unibas.inputdevicebridge.modello.interprete.AttesaState;
import it.unibas.inputdevicebridge.modello.interprete.CalibrazioneState;
import it.unibas.inputdevicebridge.modello.interprete.EsitoInterpretazione;
import it.unibas.inputdevicebridge.modello.segnale.ISegnale;
import it.unibas.inputdevicebridge.modello.segnale.SegnaleContinuo;
import it.unibas.inputdevicebridge.modello.segnale.SegnaleDiscreto;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class TestAttesaState {

    @Inject
    private Modello modello;
    private AttesaState attesaState;
    private final long timeStampIniziale = 0L;

    @BeforeEach
    public void setUp() {
        this.attesaState = new AttesaState();
        this.modello.putBean(Costanti.PROFILO_UTENTE_TEMPORANEO, null);
    }

    // ---------- TEST SEGNALE CONTINUO ----------
    @Test
    public void testInterpretaSegnaleInizialeContinuo() {
        EsitoInterpretazione esitoInterpretazione = this.interpretaSegnaleInizialeContinuo();
        assertNull(esitoInterpretazione.getStatoSuccessivo());
        assertEquals(ETipologiaEventoSistema.NESSUN_EVENTO, esitoInterpretazione.getTipologiaEvento());
    }

    @Test
    public void testInterpretaSegnaleStabileContinuoVersoAquisizione() {
        this.interpretaSegnaleInizialeContinuo();
        ISegnale segnaleStabile = new SegnaleContinuo(new Punto(100.2f, 100.2f), this.timeStampIniziale + 50L);
        EsitoInterpretazione esitoInterpretazione = this.attesaState.interpreta(segnaleStabile);
        assertInstanceOf(AcquisizioneState.class, esitoInterpretazione.getStatoSuccessivo());
        assertEquals(ETipologiaEventoSistema.NESSUN_EVENTO, esitoInterpretazione.getTipologiaEvento());
    }

    @Test
    public void testInterpretaSegnaleStabileContinuoVersoCalibrazione() {
        this.modello.putBean(Costanti.PROFILO_UTENTE_TEMPORANEO, new Object());
        this.interpretaSegnaleInizialeContinuo();
        ISegnale segnaleStabile = new SegnaleContinuo(new Punto(100.2f, 100.2f), this.timeStampIniziale + 50L);
        EsitoInterpretazione esitoInterpretazione = this.attesaState.interpreta(segnaleStabile);
        assertInstanceOf(CalibrazioneState.class, esitoInterpretazione.getStatoSuccessivo());
        assertEquals(ETipologiaEventoSistema.NESSUN_EVENTO, esitoInterpretazione.getTipologiaEvento());
    }

    @Test
    public void testInterpretaSegnaleDinamicoContinuo() {
        this.interpretaSegnaleInizialeContinuo();
        ISegnale segnaleDinamico = new SegnaleContinuo(new Punto(102.0f, 102.0f), this.timeStampIniziale + 50L);
        EsitoInterpretazione esitoInterpretazione = this.attesaState.interpreta(segnaleDinamico);
        assertNull(esitoInterpretazione.getStatoSuccessivo());
        assertEquals(ETipologiaEventoSistema.NESSUN_EVENTO, esitoInterpretazione.getTipologiaEvento());
    }

    private EsitoInterpretazione interpretaSegnaleInizialeContinuo() {
        ISegnale segnaleIniziale = new SegnaleContinuo(new Punto(100.0f, 100.0f), this.timeStampIniziale);
        return this.attesaState.interpreta(segnaleIniziale);
    }

    // ---------- TEST SEGNALE DISCRETO ----------
    @Test
    public void testInterpretaSegnaleDiscretoVersoAquisizione() {
        EsitoInterpretazione esitoInterpretazione = this.interpretaSegnaleInizialeDiscreto();
        assertInstanceOf(AcquisizioneState.class, esitoInterpretazione.getStatoSuccessivo());
        assertEquals(ETipologiaEventoSistema.NESSUN_EVENTO, esitoInterpretazione.getTipologiaEvento());
    }

    @Test
    public void testInterpretaSegnaleCompletaSelezioneDiscretoVersoCalibrazione() {
        this.modello.putBean(Costanti.PROFILO_UTENTE_TEMPORANEO, new Object());
        EsitoInterpretazione esitoInterpretazione = this.interpretaSegnaleInizialeDiscreto();
        assertInstanceOf(CalibrazioneState.class, esitoInterpretazione.getStatoSuccessivo());
        assertEquals(ETipologiaEventoSistema.NESSUN_EVENTO, esitoInterpretazione.getTipologiaEvento());
    }

    private EsitoInterpretazione interpretaSegnaleInizialeDiscreto() {
        long timeStamp = this.timeStampIniziale + 10L; 
        ISegnale segnaleY = new SegnaleDiscreto(Costanti.SOGLIA_SEGNALE_STABILE + 1.0f, timeStamp);
        this.attesaState.interpreta(segnaleY);
        ISegnale segnaleTransizione = new SegnaleDiscreto(0.0f, timeStamp + 10L);
        this.attesaState.interpreta(segnaleTransizione);
        ISegnale segnaleX = new SegnaleDiscreto(Costanti.SOGLIA_SEGNALE_STABILE + 1.0f, timeStamp + 20L);
        return this.attesaState.interpreta(segnaleX);
    }
    
}
