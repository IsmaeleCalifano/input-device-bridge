package it.unibas.inputdevicebridge.test.modello.interprete;

import io.quarkus.test.junit.QuarkusTest;
import it.unibas.inputdevicebridge.Applicazione;
import it.unibas.inputdevicebridge.enums.ETipologiaEventoPersonalizzato;
import it.unibas.inputdevicebridge.enums.ETipologiaEventoSistema;
import it.unibas.inputdevicebridge.modello.Costanti;
import it.unibas.inputdevicebridge.modello.Modello;
import it.unibas.inputdevicebridge.modello.Punto;
import it.unibas.inputdevicebridge.modello.interprete.AcquisizioneState;
import it.unibas.inputdevicebridge.modello.interprete.AttesaState;
import it.unibas.inputdevicebridge.modello.interprete.EsitoInterpretazione;
import it.unibas.inputdevicebridge.modello.profilo_utente.ProfiloUtente;
import it.unibas.inputdevicebridge.modello.segnale.ISegnale;
import it.unibas.inputdevicebridge.modello.segnale.SegnaleContinuo;
import it.unibas.inputdevicebridge.modello.segnale.SegnaleDiscreto;
import jakarta.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class TestAcquisizioneState {

    @Inject
    private Modello modello;
    private AcquisizioneState acquisizioneState;
    private ProfiloUtente profiloUtente;
    private final long timeStampIniziale = 0L;

    @BeforeEach
    public void setUp() {
        Map<ETipologiaEventoPersonalizzato, Long> mappaDurataSegnale = new HashMap<>();
        mappaDurataSegnale.put(ETipologiaEventoPersonalizzato.SEGNALE_BREVE, Costanti.DURATA_2_SECONDI);
        mappaDurataSegnale.put(ETipologiaEventoPersonalizzato.SEGNALE_MEDIO, 6 * Costanti.DURATA_1_SECONDO);
        mappaDurataSegnale.put(ETipologiaEventoPersonalizzato.SEGNALE_LUNGO, Costanti.DURATA_10_SECONDI);
        this.profiloUtente = new ProfiloUtente("Utente 01", 3.0f, 1.2f);
        this.profiloUtente.setMappaDurataSegnale(mappaDurataSegnale);
        this.modello.putBean(Costanti.PROFILO_UTENTE_SELEZIONATO, this.profiloUtente);
        this.acquisizioneState = new AcquisizioneState(this.timeStampIniziale);
    }

    // ---------- TEST SEGNALE CONTINUO ----------

    @Test
    public void testInterpretaSegnaleInizialeContinuo() {
        EsitoInterpretazione esitoInterpretazione = this.interpretaSegnaleInizialeContinuo();
        assertNull(esitoInterpretazione.getStatoSuccessivo());
        assertEquals(ETipologiaEventoSistema.NESSUN_EVENTO, esitoInterpretazione.getTipologiaEvento());
    }

    @Test
    public void testInterpretaSegnaleTroppoBreveContinuo() {
        this.interpretaSegnaleInizialeContinuo();
        ISegnale segnaleTroppoBreve = new SegnaleContinuo(new Punto(600.0f, 600.0f), this.timeStampIniziale + Costanti.DURATA_1_SECONDO / 2);
        EsitoInterpretazione esitoInterpretazione = this.acquisizioneState.interpreta(segnaleTroppoBreve);
        assertInstanceOf(AttesaState.class, esitoInterpretazione.getStatoSuccessivo());
        assertEquals(ETipologiaEventoSistema.NESSUN_EVENTO, esitoInterpretazione.getTipologiaEvento());
    }

    @Test
    public void testInterpretaSegnaleBreveContinuo() {
        this.interpretaSegnaleInizialeContinuo();
        long durataSegnaleBreve = this.profiloUtente.getMappaDurataSegnale().get(ETipologiaEventoPersonalizzato.SEGNALE_BREVE);
        ISegnale segnaleBreve = new SegnaleContinuo(new Punto(600.0f, 600.0f), this.timeStampIniziale + durataSegnaleBreve);
        EsitoInterpretazione esitoInterpretazione = this.acquisizioneState.interpreta(segnaleBreve);
        assertNull(esitoInterpretazione.getStatoSuccessivo());
        assertEquals(ETipologiaEventoPersonalizzato.SEGNALE_BREVE, esitoInterpretazione.getTipologiaEvento());
    }

    @Test
    public void testInterpretaSegnaleMedioContinuo() {
        this.interpretaSegnaleInizialeContinuo();
        long durataSegnaleMedio = this.profiloUtente.getMappaDurataSegnale().get(ETipologiaEventoPersonalizzato.SEGNALE_MEDIO);
        ISegnale segnaleMedio = new SegnaleContinuo(new Punto(600.0f, 600.0f), this.timeStampIniziale + durataSegnaleMedio);
        EsitoInterpretazione esitoInterpretazione = this.acquisizioneState.interpreta(segnaleMedio);
        assertNull(esitoInterpretazione.getStatoSuccessivo());
        assertEquals(ETipologiaEventoPersonalizzato.SEGNALE_MEDIO, esitoInterpretazione.getTipologiaEvento());
    }

    @Test
    public void testInterpretaSegnaleLungoContinuo() {
        this.interpretaSegnaleInizialeContinuo();
        long durataSegnaleLungo = this.profiloUtente.getMappaDurataSegnale().get(ETipologiaEventoPersonalizzato.SEGNALE_LUNGO);
        ISegnale segnaleLungo = new SegnaleContinuo(new Punto(600.0f, 600.0f), this.timeStampIniziale + durataSegnaleLungo);
        EsitoInterpretazione esitoInterpretazione = this.acquisizioneState.interpreta(segnaleLungo);
        assertNull(esitoInterpretazione.getStatoSuccessivo());
        assertEquals(ETipologiaEventoPersonalizzato.SEGNALE_LUNGO, esitoInterpretazione.getTipologiaEvento());
    }

    @Test
    public void testInterpretaSegnaleTroppoLungoContinuo() {
        this.interpretaSegnaleInizialeContinuo();
        long durataSegnaleLungo = this.profiloUtente.getMappaDurataSegnale().get(ETipologiaEventoPersonalizzato.SEGNALE_LUNGO);
        ISegnale segnaleTroppoLungo = new SegnaleContinuo(new Punto(600.0f, 600.0f), this.timeStampIniziale + 2 * durataSegnaleLungo);
        EsitoInterpretazione esitoInterpretazione = this.acquisizioneState.interpreta(segnaleTroppoLungo);
        assertInstanceOf(AttesaState.class, esitoInterpretazione.getStatoSuccessivo());
        assertEquals(ETipologiaEventoSistema.NESSUN_EVENTO, esitoInterpretazione.getTipologiaEvento());
    }

    private EsitoInterpretazione interpretaSegnaleInizialeContinuo() {
        ISegnale segnaleIniziale = new SegnaleContinuo(new Punto(100.0f, 100.0f), this.timeStampIniziale);
        return this.acquisizioneState.interpreta(segnaleIniziale);
    }

    // ---------- TEST SEGNALE DISCRETO ----------
    
    @Test
    public void testInterpretaSegnaleAttivoDiscreto() {
        ISegnale segnaleAttivo = new SegnaleDiscreto(Costanti.SOGLIA_SEGNALE_STABILE + 1.0f, this.timeStampIniziale + Costanti.DURATA_2_SECONDI);
        EsitoInterpretazione esitoInterpretazione = this.acquisizioneState.interpreta(segnaleAttivo);
        assertNull(esitoInterpretazione.getStatoSuccessivo());
        assertEquals(ETipologiaEventoSistema.NESSUN_EVENTO, esitoInterpretazione.getTipologiaEvento());
    }

    @Test
    public void testInterpretaSegnaleTroppoBreveDiscreto() {
        ISegnale segnaleTroppoBreve = new SegnaleDiscreto(Costanti.SOGLIA_SEGNALE_STABILE - 1.0f, this.timeStampIniziale + (Costanti.DURATA_1_SECONDO / 2));
        EsitoInterpretazione esitoInterpretazione = this.acquisizioneState.interpreta(segnaleTroppoBreve);
        assertInstanceOf(AttesaState.class, esitoInterpretazione.getStatoSuccessivo());
        assertEquals(ETipologiaEventoSistema.NESSUN_EVENTO, esitoInterpretazione.getTipologiaEvento());
    }

    @Test
    public void testInterpretaSegnaleBreveDiscreto() {
        long durataSegnaleBreve = this.profiloUtente.getMappaDurataSegnale().get(ETipologiaEventoPersonalizzato.SEGNALE_BREVE);
        ISegnale segnaleBreve = new SegnaleDiscreto(Costanti.SOGLIA_SEGNALE_STABILE - 1.0f, this.timeStampIniziale + durataSegnaleBreve);
        EsitoInterpretazione esitoInterpretazione = this.acquisizioneState.interpreta(segnaleBreve);
        assertNull(esitoInterpretazione.getStatoSuccessivo());
        assertEquals(ETipologiaEventoPersonalizzato.SEGNALE_BREVE, esitoInterpretazione.getTipologiaEvento());
    }

    @Test
    public void testInterpretaSegnaleMedioDiscreto() {
        long durataSegnaleMedio = this.profiloUtente.getMappaDurataSegnale().get(ETipologiaEventoPersonalizzato.SEGNALE_MEDIO);
        ISegnale segnaleMedio = new SegnaleDiscreto(Costanti.SOGLIA_SEGNALE_STABILE - 1.0f, this.timeStampIniziale + durataSegnaleMedio);
        EsitoInterpretazione esitoInterpretazione = this.acquisizioneState.interpreta(segnaleMedio);
        assertNull(esitoInterpretazione.getStatoSuccessivo());
        assertEquals(ETipologiaEventoPersonalizzato.SEGNALE_MEDIO, esitoInterpretazione.getTipologiaEvento());
    }

    @Test
    public void testInterpretaSegnaleLungoDiscreto() {
        long durataSegnaleLungo = this.profiloUtente.getMappaDurataSegnale().get(ETipologiaEventoPersonalizzato.SEGNALE_LUNGO);
        ISegnale segnaleLungo = new SegnaleDiscreto(Costanti.SOGLIA_SEGNALE_STABILE - 1.0f, this.timeStampIniziale + durataSegnaleLungo);
        EsitoInterpretazione esitoInterpretazione = this.acquisizioneState.interpreta(segnaleLungo);
        assertNull(esitoInterpretazione.getStatoSuccessivo());
        assertEquals(ETipologiaEventoPersonalizzato.SEGNALE_LUNGO, esitoInterpretazione.getTipologiaEvento());
    }

    @Test
    public void testInterpretaSegnaleTroppoLungoDiscreto() {
        long durataSegnaleLungo = this.profiloUtente.getMappaDurataSegnale().get(ETipologiaEventoPersonalizzato.SEGNALE_LUNGO);
        ISegnale segnaleTroppoLungo = new SegnaleDiscreto(Costanti.SOGLIA_SEGNALE_STABILE - 1.0f, this.timeStampIniziale + 2 * durataSegnaleLungo);
        EsitoInterpretazione esitoInterpretazione = this.acquisizioneState.interpreta(segnaleTroppoLungo);
        assertInstanceOf(AttesaState.class, esitoInterpretazione.getStatoSuccessivo());
        assertEquals(ETipologiaEventoSistema.NESSUN_EVENTO, esitoInterpretazione.getTipologiaEvento());
    }
    
}
