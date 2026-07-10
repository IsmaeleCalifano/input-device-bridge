package it.unibas.inputdevicebridge.test.modello.parser;

import it.unibas.inputdevicebridge.modello.parser.ParserDiscretoStrategy;
import it.unibas.inputdevicebridge.modello.segnale.ISegnale;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestParserDiscreto {

    private ParserDiscretoStrategy parser;

    @BeforeEach
    public void setUp() throws Exception {
        this.parser = new ParserDiscretoStrategy(",", 0, 1);
        this.parser.parse("@CALIBRAZIONE, 10, 50, 100");
    }

    @Test
    public void testParseRigaCalibrazione() throws Exception {
        ISegnale segnale = this.parser.parse("@CALIBRAZIONE, 10, 50, 100");
        assertNull(segnale);
    }

    @Test
    public void testParseValoreRiposo() throws Exception {
        ISegnale segnale = this.parser.parse("1000, 50");
        assertEquals(1_000L, segnale.getTimeStamp());
        assertEquals(0.0f, segnale.getIntensita(), 0.001f);
    }

    @Test
    public void testParseValoreIntermedioPositivo() throws Exception {
        ISegnale segnale = this.parser.parse("1000, 75");
        assertEquals(1_000L, segnale.getTimeStamp());
        assertEquals(0.5f, segnale.getIntensita(), 0.001f);
    }

    @Test
    public void testParseValoreIntermedioNegativo() throws Exception {
        ISegnale segnale = this.parser.parse("1000, 30");
        assertEquals(1_000L, segnale.getTimeStamp());
        assertEquals(-0.5f, segnale.getIntensita(), 0.001f);
    }

    @Test
    public void testPareValoreMassimo() throws Exception {
        ISegnale segnale = this.parser.parse("1000, 100");
        assertEquals(1_000L, segnale.getTimeStamp());
        assertEquals(1.0f, segnale.getIntensita(), 0.001f);
    }

    @Test
    public void testParseValoreMinimo() throws Exception {
        ISegnale segnale = this.parser.parse("1000, 10");
        assertEquals(1_000L, segnale.getTimeStamp());
        assertEquals(-1.0f, segnale.getIntensita(), 0.001f);
    }

    @Test
    public void testParseValoreFuoriRangeMassimo() throws Exception {
        ISegnale segnale = this.parser.parse("1000, 150");
        assertEquals(1_000L, segnale.getTimeStamp());
        assertEquals(1.0f, segnale.getIntensita(), 0.001f);
    }

    @Test
    public void testParseValoreFuoriRangeMinimo() throws Exception {
        ISegnale segnale = this.parser.parse("1000, 0");
        assertEquals(1_000L, segnale.getTimeStamp());
        assertEquals(-1.0f, segnale.getIntensita(), 0.001f);
    }
    
}