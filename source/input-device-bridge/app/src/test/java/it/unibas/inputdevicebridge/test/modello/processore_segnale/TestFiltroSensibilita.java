package it.unibas.inputdevicebridge.test.modello.processore_segnale;

import it.unibas.inputdevicebridge.modello.Costanti;
import it.unibas.inputdevicebridge.modello.Punto;
import it.unibas.inputdevicebridge.modello.processore_segnale.FiltroSensibilita;
import it.unibas.inputdevicebridge.modello.processore_segnale.IFiltro;
import it.unibas.inputdevicebridge.modello.segnale.ISegnale;
import it.unibas.inputdevicebridge.modello.segnale.SegnaleContinuo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestFiltroSensibilita {

    private IFiltro filtroSensibilita;
    private final long timeStampIniziale = 0L;

    @BeforeEach
    public void setUp() {
        this.filtroSensibilita = new FiltroSensibilita(1.2f);
    }

    @Test
    public void testFiltraSegnaleIniziale() {
        ISegnale segnale = new SegnaleContinuo(new Punto(100.0f, 100.0f), this.timeStampIniziale);
        this.filtroSensibilita.filtra(segnale);
        assertEquals(100.0f, segnale.getPunto().getX(), 0.001f);
        assertEquals(100.0f, segnale.getPunto().getY(), 0.001f);
    }

    @Test
    public void testFiltra() {
        this.filtraSegnaleIniziale();
        ISegnale segnaleNuovo = new SegnaleContinuo(new Punto(110.0f, 105.0f), this.timeStampIniziale + 50L);
        this.filtroSensibilita.filtra(segnaleNuovo);
        float deltaX = 110.0f - 100.0f;
        float deltaY = 105.0f - 100.0f;
        float xAttesa = 100.0f + (deltaX * Costanti.FATTORE_SENSIBILTA);
        float yAttesa = 100.0f + (deltaY * Costanti.FATTORE_SENSIBILTA);
        assertEquals(xAttesa, segnaleNuovo.getPunto().getX(), 0.001f);
        assertEquals(yAttesa, segnaleNuovo.getPunto().getY(), 0.001f);
    }

    private void filtraSegnaleIniziale() {
        ISegnale segnaleIniziale = new SegnaleContinuo(new Punto(100.0f, 100.0f), this.timeStampIniziale);
        this.filtroSensibilita.filtra(segnaleIniziale);
    }

}
