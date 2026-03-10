package it.unibas.inputdevicebridge.test.modello.processore_segnale;

import it.unibas.inputdevicebridge.modello.Punto;
import it.unibas.inputdevicebridge.modello.processore_segnale.FiltroZoneMorte;
import it.unibas.inputdevicebridge.modello.processore_segnale.IFiltro;
import it.unibas.inputdevicebridge.modello.segnale.ISegnale;
import it.unibas.inputdevicebridge.modello.segnale.SegnaleContinuo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestFiltroZoneMorte {
    
    private IFiltro filtroZoneMorte;
    private final long timeStampIniziale = 0L;
    
    @BeforeEach
    public void setUp() {
        this.filtroZoneMorte = new FiltroZoneMorte();
    }
    
    @Test
    public void testFiltraSegnaleIniziale() {
        ISegnale segnale = new SegnaleContinuo(new Punto(100.0f, 100.0f), this.timeStampIniziale);
        this.filtroZoneMorte.filtra(segnale);
        assertEquals(100.0f, segnale.getPunto().getX(), 0.001f);
        assertEquals(100.0f, segnale.getPunto().getY(), 0.001f);
    }

    @Test
    public void testFiltraSegnaleSottoSoglia() {
        this.filtraSegnaleIniziale();
        ISegnale segnaleSuccessivo = new SegnaleContinuo(new Punto(101.0f, 101.0f), this.timeStampIniziale + 50L);
        this.filtroZoneMorte.filtra(segnaleSuccessivo);
        assertEquals(100.0f, segnaleSuccessivo.getPunto().getX(), 0.001f);
        assertEquals(100.0f, segnaleSuccessivo.getPunto().getY(), 0.001f);
    }

    @Test
    public void testFiltraSegnaleOltreSoglia() {
        this.filtraSegnaleIniziale();
        ISegnale segnaleSuccessivo = new SegnaleContinuo(new Punto(150.0f, 150.0f), this.timeStampIniziale + 50L);
        this.filtroZoneMorte.filtra(segnaleSuccessivo);
        assertEquals(150.0f, segnaleSuccessivo.getPunto().getX(), 0.001f);
        assertEquals(150.0f, segnaleSuccessivo.getPunto().getY(), 0.001f);
    }
    
    private void filtraSegnaleIniziale() {
        ISegnale segnaleIniziale = new SegnaleContinuo(new Punto(100.0f, 100.0f), this.timeStampIniziale);
        this.filtroZoneMorte.filtra(segnaleIniziale);
    }
    
}
