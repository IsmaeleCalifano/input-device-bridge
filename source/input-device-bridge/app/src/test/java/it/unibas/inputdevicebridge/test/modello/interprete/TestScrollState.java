package it.unibas.inputdevicebridge.test.modello.interprete;

import it.unibas.inputdevicebridge.enums.ETipologiaEventoSistema;
import it.unibas.inputdevicebridge.modello.Costanti;
import it.unibas.inputdevicebridge.modello.Punto;
import it.unibas.inputdevicebridge.modello.interprete.AttesaState;
import it.unibas.inputdevicebridge.modello.interprete.EsitoInterpretazione;
import it.unibas.inputdevicebridge.modello.interprete.ScrollState;
import it.unibas.inputdevicebridge.modello.segnale.ISegnale;
import it.unibas.inputdevicebridge.modello.segnale.SegnaleContinuo;
import it.unibas.inputdevicebridge.modello.segnale.SegnaleDiscreto;
import java.awt.Toolkit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestScrollState {

    private ScrollState scrollState;
    private float altezzaSchermo;
    private float centroSchermo;
    private final long timeStampIniziale = 0L;

    @BeforeEach
    public void setUp() {
        this.altezzaSchermo = (float) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        this.centroSchermo = altezzaSchermo / 2.0f;
        this.scrollState = new ScrollState();
    }

    // ---------- TEST SEGNALE CONTINUO ----------
    
    @Test
    public void testInterpretaSegnaleScrollSuContinuo() {
        ISegnale segnaleScrollSu = new SegnaleContinuo(new Punto(100.0f, 0.0f), this.timeStampIniziale);
        EsitoInterpretazione esitoInterpretazione = this.scrollState.interpreta(segnaleScrollSu);
        assertNull(esitoInterpretazione.getStatoSuccessivo());
        assertEquals(ETipologiaEventoSistema.SCROLL_SU, esitoInterpretazione.getTipologiaEvento());
    }

    @Test
    public void testInterpretaSegnaleScrollGiuContinuo() {
        ISegnale segnaleScrollGiu = new SegnaleContinuo(new Punto(100.0f, (float) this.altezzaSchermo), this.timeStampIniziale);
        EsitoInterpretazione esitoInterpretazione = this.scrollState.interpreta(segnaleScrollGiu);
        assertNull(esitoInterpretazione.getStatoSuccessivo());
        assertEquals(ETipologiaEventoSistema.SCROLL_GIU, esitoInterpretazione.getTipologiaEvento());
    }

    @Test
    public void testInterpretaSegnaleAlCentroTroppoBreveContinuo() {
        ISegnale segnaleAlCentroTroppoBreve = new SegnaleContinuo(new Punto(100.0f, this.centroSchermo), this.timeStampIniziale);
        EsitoInterpretazione esitoInterpretazione = this.scrollState.interpreta(segnaleAlCentroTroppoBreve);
        assertNull(esitoInterpretazione.getStatoSuccessivo());
        assertEquals(ETipologiaEventoSistema.NESSUN_EVENTO, esitoInterpretazione.getTipologiaEvento());
    }

    @Test
    public void testInterpretaSegnaleAlCentroContinuo() {
        ISegnale segnaleAlCentro = new SegnaleContinuo(new Punto(100.0f, this.centroSchermo), this.timeStampIniziale);
        this.scrollState.interpreta(segnaleAlCentro);
        segnaleAlCentro = new SegnaleContinuo(new Punto(100.0f, this.centroSchermo), this.timeStampIniziale + Costanti.DURATA_1_SECONDO + Costanti.MAX_TEMPO_INATTIVO);
        EsitoInterpretazione esitoInterpretazione = this.scrollState.interpreta(segnaleAlCentro);
        assertInstanceOf(AttesaState.class, esitoInterpretazione.getStatoSuccessivo());
        assertEquals(ETipologiaEventoSistema.NESSUN_EVENTO, esitoInterpretazione.getTipologiaEvento());
    }

    // ---------- TEST SEGNALE DISCRETO ----------
    
    @Test
    public void testInterpretaSegnaleSopraSogliaInizialeDiscreto() {
        ISegnale segnale = new SegnaleDiscreto(Costanti.SOGLIA_SEGNALE_STABILE + 1.0f, this.timeStampIniziale);
        EsitoInterpretazione esitoInterpretazione = this.scrollState.interpreta(segnale);
        assertNull(esitoInterpretazione.getStatoSuccessivo());
        assertEquals(ETipologiaEventoSistema.NESSUN_EVENTO, esitoInterpretazione.getTipologiaEvento());
    }

    @Test
    public void testInterpretaSegnaleScrollGiuDiscreto() {
        this.scrollState.interpreta(new SegnaleDiscreto(Costanti.SOGLIA_SEGNALE_STABILE + 1.0f, this.timeStampIniziale));
        ISegnale segnaleLungo = new SegnaleDiscreto(Costanti.SOGLIA_SEGNALE_STABILE + 1.0f, this.timeStampIniziale + Costanti.DURATA_2_SECONDI + 100L);
        EsitoInterpretazione esito = this.scrollState.interpreta(segnaleLungo);

        assertNull(esito.getStatoSuccessivo());
        assertEquals(ETipologiaEventoSistema.SCROLL_GIU, esito.getTipologiaEvento());
    }

    @Test
    public void testInterpretaSegnaleScrollSuDiscreto() {
        this.scrollState.interpreta(new SegnaleDiscreto(Costanti.SOGLIA_SEGNALE_STABILE + 1.0f, this.timeStampIniziale));
        ISegnale segnaleRilascio = new SegnaleDiscreto(Costanti.SOGLIA_SEGNALE_STABILE - 1.0f, this.timeStampIniziale + Costanti.DURATA_1_SECONDO - 100L);
        EsitoInterpretazione esitoInversione = this.scrollState.interpreta(segnaleRilascio);
        assertNull(esitoInversione.getStatoSuccessivo());
        assertEquals(ETipologiaEventoSistema.NESSUN_EVENTO, esitoInversione.getTipologiaEvento());
        long timeStampNuovaAzione = this.timeStampIniziale + Costanti.DURATA_1_SECONDO;
        this.scrollState.interpreta(new SegnaleDiscreto(Costanti.SOGLIA_SEGNALE_STABILE + 1.0f, timeStampNuovaAzione));
        ISegnale segnaleLungo = new SegnaleDiscreto(Costanti.SOGLIA_SEGNALE_STABILE + 1.0f, timeStampNuovaAzione + Costanti.DURATA_2_SECONDI + 100L);
        EsitoInterpretazione esitoScroll = this.scrollState.interpreta(segnaleLungo);
        assertNull(esitoScroll.getStatoSuccessivo());
        assertEquals(ETipologiaEventoSistema.SCROLL_SU, esitoScroll.getTipologiaEvento());
    }

    @Test
    public void testInterpretaSegnaleEsciDaScrollStateDiscreto() {
        this.scrollState.interpreta(new SegnaleDiscreto(Costanti.SOGLIA_SEGNALE_STABILE + 1.0f, this.timeStampIniziale));
        long timeStampMedio = this.timeStampIniziale + Costanti.DURATA_1_SECONDO + 500L;
        ISegnale segnaleRilascio = new SegnaleDiscreto(Costanti.SOGLIA_SEGNALE_STABILE - 1.0f, timeStampMedio);
        EsitoInterpretazione esitoInterpretazione = this.scrollState.interpreta(segnaleRilascio);
        assertInstanceOf(AttesaState.class, esitoInterpretazione.getStatoSuccessivo());
        assertEquals(ETipologiaEventoSistema.NESSUN_EVENTO, esitoInterpretazione.getTipologiaEvento());
    }
    
}
