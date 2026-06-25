package it.unibas.inputdevicebridge.test.modello.processore_segnale;

import it.unibas.inputdevicebridge.modello.Punto;
import it.unibas.inputdevicebridge.modello.processore_segnale.FiltroZoneMorte;
import it.unibas.inputdevicebridge.modello.processore_segnale.IFiltro;
import it.unibas.inputdevicebridge.modello.segnale.ISegnale;
import it.unibas.inputdevicebridge.modello.segnale.SegnaleContinuo;
import it.unibas.inputdevicebridge.modello.segnale.SegnaleDiscreto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestFiltroZoneMorte {
    
    private IFiltro filtroZoneMorte;
    private final long timeStampIniziale = 0L;
    private final float sogliaTest = 3.0f;
    
    @BeforeEach
    public void setUp() {
        this.filtroZoneMorte = new FiltroZoneMorte(this.sogliaTest);
    }
    
    // ---------- TEST SEGNALE CONTINUO ----------

    @Test
    public void testFiltraSegnaleInizialeContinuo() {
        ISegnale segnale = new SegnaleContinuo(new Punto(100.0f, 100.0f), this.timeStampIniziale);
        this.filtroZoneMorte.filtra(segnale);
        assertEquals(100.0f, segnale.getPunto().getX(), 0.001f);
        assertEquals(100.0f, segnale.getPunto().getY(), 0.001f);
    }

    @Test
    public void testFiltraSegnaleSottoSogliaContinuo() {
        this.filtraSegnaleInizialeContinuo();
        ISegnale segnaleSuccessivo = new SegnaleContinuo(new Punto(101.0f, 101.0f), this.timeStampIniziale + 50L);
        this.filtroZoneMorte.filtra(segnaleSuccessivo);
        assertEquals(100.0f, segnaleSuccessivo.getPunto().getX(), 0.001f);
        assertEquals(100.0f, segnaleSuccessivo.getPunto().getY(), 0.001f);
    }

    @Test
    public void testFiltraSegnaleOltreSogliaContinuo() {
        this.filtraSegnaleInizialeContinuo();
        ISegnale segnaleSuccessivo = new SegnaleContinuo(new Punto(150.0f, 150.0f), this.timeStampIniziale + 50L);
        this.filtroZoneMorte.filtra(segnaleSuccessivo);
        assertEquals(150.0f, segnaleSuccessivo.getPunto().getX(), 0.001f);
        assertEquals(150.0f, segnaleSuccessivo.getPunto().getY(), 0.001f);
    }
    
    private void filtraSegnaleInizialeContinuo() {
        ISegnale segnaleIniziale = new SegnaleContinuo(new Punto(100.0f, 100.0f), this.timeStampIniziale);
        this.filtroZoneMorte.filtra(segnaleIniziale);
    }

    // ---------- TEST SEGNALE DISCRETO ----------

    @Test
    public void testFiltraSegnaleSottoSogliaPositivoDiscreto() {
        ISegnale segnale = new SegnaleDiscreto(2.0f, this.timeStampIniziale);
        this.filtroZoneMorte.filtra(segnale);
        assertEquals(0.0f, segnale.getIntensita(), 0.001f);
    }

    @Test
    public void testFiltraSegnaleSottoSogliaNegativoDiscreto() {
        ISegnale segnale = new SegnaleDiscreto(-2.5f, this.timeStampIniziale);
        this.filtroZoneMorte.filtra(segnale);
        assertEquals(0.0f, segnale.getIntensita(), 0.001f);
    }

    @Test
    public void testFiltraSegnaleOltreSogliaPositivoDiscreto() {
        ISegnale segnale = new SegnaleDiscreto(5.0f, this.timeStampIniziale);
        this.filtroZoneMorte.filtra(segnale);
        assertEquals(5.0f, segnale.getIntensita(), 0.001f);
    }

    @Test
    public void testFiltraSegnaleOltreSogliaNegativoDiscreto() {
        ISegnale segnale = new SegnaleDiscreto(-4.0f, this.timeStampIniziale);
        this.filtroZoneMorte.filtra(segnale);
        assertEquals(-4.0f, segnale.getIntensita(), 0.001f);
    }
    
}
