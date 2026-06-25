package it.unibas.inputdevicebridge.test.modello.parser;

import it.unibas.inputdevicebridge.modello.parser.ParserContinuo;
import it.unibas.inputdevicebridge.modello.segnale.ISegnale;
import java.awt.Dimension;
import java.awt.Toolkit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestParserContinuo {

    private ParserContinuo parser;
    private float larghezzaSchermo;
    private float altezzaSchermo;

    @BeforeEach
    public void setUp() throws Exception {
        this.parser = new ParserContinuo(",", 0, 1, 2);
        Dimension dimensioniSchermo = Toolkit.getDefaultToolkit().getScreenSize();
        this.larghezzaSchermo = (float) dimensioniSchermo.getWidth();
        this.altezzaSchermo = (float) dimensioniSchermo.getHeight();
        // Formato TL(x,y), TR(x,y), BR(x,y), BL(x,y)
        this.parser.parse("@CALIBRAZIONE, -100.0, -100.0, 1000.0, -100.0, 1000.0, 1000.0, -100.0, 1000.0");
    }

    @Test
    public void testParseRigaCalibrazione() throws Exception {
        ISegnale segnale = this.parser.parse("@CALIBRAZIONE, -100.0, -100.0, 1000.0, -100.0, 1000.0, 1000.0, -100.0, 1000.0");
        assertNull(segnale);
    }

    @Test
    public void testParseValoreCentro() throws Exception {
        ISegnale segnale = this.parser.parse("1000, 450.0, 450.0");
        assertEquals(1_000L, segnale.getTimeStamp());
        assertEquals(this.larghezzaSchermo / 2.0f, segnale.getPunto().getX(), 0.001f);
        assertEquals(this.altezzaSchermo / 2.0f, segnale.getPunto().getY(), 0.001f);
    }

    @Test
    public void testParseValoreMinimo() throws Exception {
        ISegnale segnale = this.parser.parse("1000, -100.0, -100.0");
        assertEquals(1_000L, segnale.getTimeStamp());
        assertEquals(0.0f, segnale.getPunto().getX(), 0.001f);
        assertEquals(0.0f, segnale.getPunto().getY(), 0.001f);
    }

    @Test
    public void testParseValoreMassimo() throws Exception {
        ISegnale segnale = this.parser.parse("1000, 1000.0, 1000.0");
        assertEquals(1_000L, segnale.getTimeStamp());
        assertEquals(this.larghezzaSchermo, segnale.getPunto().getX(), 0.001f);
        assertEquals(this.altezzaSchermo, segnale.getPunto().getY(), 0.001f);
    }

    @Test
    public void testParseValoreFuoriRangeMassimo() throws Exception {
        ISegnale segnale = this.parser.parse("1000, 1500.0, 1200.0");
        assertEquals(1_000L, segnale.getTimeStamp());
        assertEquals(this.larghezzaSchermo, segnale.getPunto().getX(), 0.001f);
        assertEquals(this.altezzaSchermo, segnale.getPunto().getY(), 0.001f);
    }

    @Test
    public void testParseValoreFuoriRangeMinimo() throws Exception {
        ISegnale segnale = this.parser.parse("1000, -200.0, -500.0");
        assertEquals(1_000L, segnale.getTimeStamp());
        assertEquals(0.0f, segnale.getPunto().getX(), 0.001f);
        assertEquals(0.0f, segnale.getPunto().getY(), 0.001f);
    }

}
