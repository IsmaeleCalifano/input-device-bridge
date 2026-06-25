package it.unibas.inputdevicebridge.test.modello.interprete;

import it.unibas.inputdevicebridge.Applicazione;
import it.unibas.inputdevicebridge.enums.ETipologiaAzionePersonalizzata;
import it.unibas.inputdevicebridge.enums.ETipologiaEventoPersonalizzato;
import it.unibas.inputdevicebridge.enums.ETipologiaEventoSistema;
import it.unibas.inputdevicebridge.modello.Costanti;
import it.unibas.inputdevicebridge.modello.Punto;
import it.unibas.inputdevicebridge.modello.interprete.AttesaState;
import it.unibas.inputdevicebridge.modello.interprete.CalibrazioneState;
import it.unibas.inputdevicebridge.modello.interprete.EsitoInterpretazione;
import it.unibas.inputdevicebridge.modello.segnale.ISegnale;
import it.unibas.inputdevicebridge.modello.segnale.SegnaleContinuo;
import it.unibas.inputdevicebridge.modello.segnale.SegnaleDiscreto;
import java.util.AbstractMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestCalibrazioneState {

    private CalibrazioneState calibrazioneState;
    private final long timeStampIniziale = 0L;

    @BeforeEach
    public void setUp() {
        this.calibrazioneState = new CalibrazioneState(this.timeStampIniziale);
        Map.Entry<ETipologiaEventoPersonalizzato, ETipologiaAzionePersonalizzata> entryMock
                = new AbstractMap.SimpleEntry<>(ETipologiaEventoPersonalizzato.SEGNALE_BREVE, ETipologiaAzionePersonalizzata.CLICK);
        Applicazione.getInstance().getModello().putBean(Costanti.ENTRY_EVENTO_AZIONE_CALIBRAZIONE, entryMock);
    }

    // ---------- TEST SEGNALE CONTINUO ----------
    
    @Test
    public void testInterpretaSegnaleInizialeContinuo() {
        EsitoInterpretazione esitoInterpretazione = this.interpretaSegnaleInizialeContinuo();
        assertNull(esitoInterpretazione.getStatoSuccessivo());
        assertEquals(ETipologiaEventoSistema.NESSUN_EVENTO, esitoInterpretazione.getTipologiaEvento());
    }

    @Test
    public void testInterpretaSegnaleStabileContinuo() {
        this.interpretaSegnaleInizialeContinuo();
        ISegnale segnaleStabile = new SegnaleContinuo(new Punto(100.5f, 100.5f), this.timeStampIniziale + 50L);
        EsitoInterpretazione esitoInterpretazione = this.calibrazioneState.interpreta(segnaleStabile);
        assertNull(esitoInterpretazione.getStatoSuccessivo());
        assertEquals(ETipologiaEventoSistema.NESSUN_EVENTO, esitoInterpretazione.getTipologiaEvento());
    }

    @Test
    public void testInterpretaSegnaleUscitaAreaContinuo() {
        this.interpretaSegnaleInizialeContinuo();
        ISegnale segnaleUscita = new SegnaleContinuo(new Punto(300.0f, 300.0f), this.timeStampIniziale + Costanti.DURATA_1_SECONDO);
        EsitoInterpretazione esitoInterpretazione = this.calibrazioneState.interpreta(segnaleUscita);
        assertInstanceOf(AttesaState.class, esitoInterpretazione.getStatoSuccessivo());
        assertEquals(ETipologiaEventoPersonalizzato.SEGNALE_BREVE, esitoInterpretazione.getTipologiaEvento());
        long durataSegnaleAcquisito = (long) Applicazione.getInstance().getModello().getBean(Costanti.DURATA_SEGNALE_ACQUISITO);
        assertEquals(Costanti.DURATA_1_SECONDO, durataSegnaleAcquisito);
    }

    @Test
    public void testInterpretaUscitaAreaContinuoSenzaEntry() {
        Applicazione.getInstance().getModello().putBean(Costanti.ENTRY_EVENTO_AZIONE_CALIBRAZIONE, null);
        this.interpretaSegnaleInizialeContinuo();
        ISegnale segnaleUscita = new SegnaleContinuo(new Punto(300.0f, 300.0f), this.timeStampIniziale + Costanti.DURATA_1_SECONDO);
        EsitoInterpretazione esitoInterpretazione = this.calibrazioneState.interpreta(segnaleUscita);
        assertInstanceOf(AttesaState.class, esitoInterpretazione.getStatoSuccessivo());
        assertEquals(ETipologiaEventoSistema.NESSUN_EVENTO, esitoInterpretazione.getTipologiaEvento());
    }
    
    private EsitoInterpretazione interpretaSegnaleInizialeContinuo() {
        ISegnale segnaleIniziale = new SegnaleContinuo(new Punto(100.0f, 100.0f), this.timeStampIniziale);
        return this.calibrazioneState.interpreta(segnaleIniziale);
    }

    // ---------- TEST SEGNALE DISCRETO ----------
    
    @Test
    public void testInterpretaSegnaleAttivoDiscreto() {
        ISegnale segnaleAttivo = new SegnaleDiscreto(Costanti.SOGLIA_SEGNALE_STABILE + 1.0f, this.timeStampIniziale + 500L);
        EsitoInterpretazione esitoInterpretazione = this.calibrazioneState.interpreta(segnaleAttivo);
        assertNull(esitoInterpretazione.getStatoSuccessivo());
        assertEquals(ETipologiaEventoSistema.NESSUN_EVENTO, esitoInterpretazione.getTipologiaEvento());
    }

    @Test
    public void testInterpretaSegnaleRilascioDiscreto() {
        ISegnale segnaleRilascio = new SegnaleDiscreto(Costanti.SOGLIA_SEGNALE_STABILE - 1.0f,  this.timeStampIniziale + Costanti.DURATA_2_SECONDI);
        EsitoInterpretazione esitoInterpretazione = this.calibrazioneState.interpreta(segnaleRilascio);
        assertInstanceOf(AttesaState.class, esitoInterpretazione.getStatoSuccessivo());
        assertEquals(ETipologiaEventoPersonalizzato.SEGNALE_BREVE, esitoInterpretazione.getTipologiaEvento());
        long durataSegnaleAcquisito = (long) Applicazione.getInstance().getModello().getBean(Costanti.DURATA_SEGNALE_ACQUISITO);
        assertEquals(Costanti.DURATA_2_SECONDI, durataSegnaleAcquisito);
    }

    @Test
    public void testInterpretaSegnaleRilascioDiscretoSenzaEntry() {
        Applicazione.getInstance().getModello().putBean(Costanti.ENTRY_EVENTO_AZIONE_CALIBRAZIONE, null);
        ISegnale segnaleRilascio = new SegnaleDiscreto(0.0f, this.timeStampIniziale + 100L);
        EsitoInterpretazione esito = this.calibrazioneState.interpreta(segnaleRilascio);
        assertInstanceOf(AttesaState.class, esito.getStatoSuccessivo());
        assertEquals(ETipologiaEventoSistema.NESSUN_EVENTO, esito.getTipologiaEvento());
    }
    
}
