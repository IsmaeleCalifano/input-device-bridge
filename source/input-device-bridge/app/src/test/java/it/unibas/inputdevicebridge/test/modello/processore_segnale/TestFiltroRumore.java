package it.unibas.inputdevicebridge.test.modello.processore_segnale;

import it.unibas.inputdevicebridge.modello.Costanti;
import it.unibas.inputdevicebridge.modello.Punto;
import it.unibas.inputdevicebridge.modello.processore_segnale.FiltroRumore;
import it.unibas.inputdevicebridge.modello.processore_segnale.IFiltro;
import it.unibas.inputdevicebridge.modello.segnale.ISegnale;
import it.unibas.inputdevicebridge.modello.segnale.SegnaleContinuo;
import it.unibas.inputdevicebridge.modello.segnale.SegnaleDiscreto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestFiltroRumore {

    private IFiltro filtroRumore;
    private final long timeStampIniziale = 0L;

    @BeforeEach
    public void setUp() {
        this.filtroRumore = new FiltroRumore();
    }

    // ---------- TEST SEGNALE CONTINUO ----------

    @Test
    public void testFiltraSegnaleInizialeContinuo() {
        ISegnale segnale = this.filtraSegnaleInizialeContinuo();
        assertEquals(100.0f, segnale.getPunto().getX(), 0.001f);
        assertEquals(100.0f, segnale.getPunto().getY(), 0.001f);
    }

    @Test
    public void testFiltraSegnaleStabileContinuo() {
        this.filtraSegnaleInizialeContinuo();
        ISegnale segnaleStabile = new SegnaleContinuo(new Punto(102.0f, 102.0f), this.timeStampIniziale + 50L);
        this.filtroRumore.filtra(segnaleStabile);
        float xAttesa = (100.0f * (1 - Costanti.ALPHA_SEGNALE_STABILE)) + (102.0f * Costanti.ALPHA_SEGNALE_STABILE);
        float yAttesa = (100.0f * (1 - Costanti.ALPHA_SEGNALE_STABILE)) + (102.0f * Costanti.ALPHA_SEGNALE_STABILE);
        assertEquals(xAttesa, segnaleStabile.getPunto().getX(), 0.001f);
        assertEquals(yAttesa, segnaleStabile.getPunto().getY(), 0.001f);
    }

    @Test
    public void testFiltraSegnaleDinamicoContinuo() {
        this.filtraSegnaleInizialeContinuo();
        ISegnale segnaleDinamico = new SegnaleContinuo(new Punto(200.0f, 200.0f), this.timeStampIniziale + 50L);
        this.filtroRumore.filtra(segnaleDinamico);
        float xAttesa = (100.0f * (1 - Costanti.ALPHA_SEGNALE_DINAMICO)) + (200.0f * Costanti.ALPHA_SEGNALE_DINAMICO);
        float yAttesa = (100.0f * (1 - Costanti.ALPHA_SEGNALE_DINAMICO)) + (200.0f * Costanti.ALPHA_SEGNALE_DINAMICO);
        assertEquals(xAttesa, segnaleDinamico.getPunto().getX(), 0.001f);
        assertEquals(yAttesa, segnaleDinamico.getPunto().getY(), 0.001f);
    }

    private ISegnale filtraSegnaleInizialeContinuo() {
        ISegnale segnaleIniziale = new SegnaleContinuo(new Punto(100.0f, 100.0f), this.timeStampIniziale);
        this.filtroRumore.filtra(segnaleIniziale);
        return segnaleIniziale;
    }

    // ---------- TEST SEGNALE DISCRETO ----------

    @Test
    public void testFiltraSegnaleInizialeDiscreto() {
        ISegnale segnale = this.filtraSegnaleInizialeDiscreto();
        assertEquals(0.5f, segnale.getIntensita(), 0.001f);
    }

    @Test
    public void testFiltraSegnaleSuccessivoDiscreto() {
        this.filtraSegnaleInizialeDiscreto(); 
        ISegnale segnaleSuccessivo = new SegnaleDiscreto(1.0f, this.timeStampIniziale + 500L);
        this.filtroRumore.filtra(segnaleSuccessivo);
        float intensitaAttesa = (0.5f * (1 - Costanti.ALPHA_INTENSITA)) + (1.0f * Costanti.ALPHA_INTENSITA);
        assertEquals(intensitaAttesa, segnaleSuccessivo.getIntensita(), 0.001f);
    }

    private ISegnale filtraSegnaleInizialeDiscreto() {
        ISegnale segnaleIniziale = new SegnaleDiscreto(0.5f, this.timeStampIniziale);
        this.filtroRumore.filtra(segnaleIniziale);
        return segnaleIniziale;
    }

}
