package it.unibas.inputdevicebridge.test.modello.processore_segnale;

import it.unibas.inputdevicebridge.modello.Punto;
import it.unibas.inputdevicebridge.modello.processore_segnale.FiltroSensibilita;
import it.unibas.inputdevicebridge.modello.processore_segnale.IFiltro;
import it.unibas.inputdevicebridge.modello.segnale.ISegnale;
import it.unibas.inputdevicebridge.modello.segnale.SegnaleContinuo;
import it.unibas.inputdevicebridge.modello.segnale.SegnaleDiscreto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestFiltroSensibilita {

    private IFiltro filtroSensibilita;
    private final long timeStampIniziale = 0L;
    private final float fattoreSensibilitaTest = 1.2f;

    @BeforeEach
    public void setUp() {
        this.filtroSensibilita = new FiltroSensibilita(this.fattoreSensibilitaTest);
    }

    // ---------- TEST SEGNALE CONTINUO ----------

    @Test
    public void testFiltraSegnaleInizialeContinuo() {
        ISegnale segnale = filtraSegnaleInizialeContinuo();
        assertEquals(100.0f, segnale.getPunto().getX(), 0.001f);
        assertEquals(100.0f, segnale.getPunto().getY(), 0.001f);
    }

    @Test
    public void testFiltraContinuo() {
        this.filtraSegnaleInizialeContinuo();
        ISegnale segnaleNuovo = new SegnaleContinuo(new Punto(110.0f, 105.0f), this.timeStampIniziale + 50L);
        this.filtroSensibilita.filtra(segnaleNuovo);
        float deltaX = 110.0f - 100.0f;
        float deltaY = 105.0f - 100.0f;
        float xAttesa = 100.0f + (deltaX * this.fattoreSensibilitaTest);
        float yAttesa = 100.0f + (deltaY * this.fattoreSensibilitaTest);
        assertEquals(xAttesa, segnaleNuovo.getPunto().getX(), 0.001f);
        assertEquals(yAttesa, segnaleNuovo.getPunto().getY(), 0.001f);
    }

    private ISegnale filtraSegnaleInizialeContinuo() {
        ISegnale segnaleIniziale = new SegnaleContinuo(new Punto(100.0f, 100.0f), this.timeStampIniziale);
        this.filtroSensibilita.filtra(segnaleIniziale);
        return segnaleIniziale;
    }

    // ---------- TEST SEGNALE DISCRETO ----------

    @Test
    public void testFiltraSegnaleDiscreto() {
        ISegnale segnale = new SegnaleDiscreto(0.5f, this.timeStampIniziale);
        this.filtroSensibilita.filtra(segnale);
        float intensitaAttesa = 0.5f * this.fattoreSensibilitaTest;
        assertEquals(intensitaAttesa, segnale.getIntensita(), 0.001f);
    }

    @Test
    public void testFiltraSegnaleDiscretoClampingMassimo() {
        ISegnale segnale = new SegnaleDiscreto(0.9f, this.timeStampIniziale);
        this.filtroSensibilita.filtra(segnale);
        assertEquals(1.0f, segnale.getIntensita(), 0.001f);
    }

    @Test
    public void testFiltraSegnaleDiscretoClampingMinimo() {
        ISegnale segnale = new SegnaleDiscreto(-0.9f, this.timeStampIniziale);
        this.filtroSensibilita.filtra(segnale);
        assertEquals(-1.0f, segnale.getIntensita(), 0.001f);
    }
    
}
