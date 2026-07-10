package it.unibas.inputdevicebridge.test.modello.interprete;

import io.quarkus.test.junit.QuarkusTest;
import it.unibas.inputdevicebridge.enums.ETipologiaEventoSistema;
import it.unibas.inputdevicebridge.modello.Costanti;
import it.unibas.inputdevicebridge.modello.Modello;
import it.unibas.inputdevicebridge.modello.Punto;
import it.unibas.inputdevicebridge.modello.interprete.EsitoInterpretazione;
import it.unibas.inputdevicebridge.modello.interprete.MovimentoState;
import it.unibas.inputdevicebridge.modello.segnale.ISegnale;
import it.unibas.inputdevicebridge.modello.segnale.SegnaleDiscreto;
import jakarta.inject.Inject;
import java.awt.Dimension;
import java.awt.Toolkit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class TestMovimentoState {

    @Inject
    private Modello modello;
    private MovimentoState movimentoState;
    private final long timeStampIniziale = 0L;
    private float maxY;
    private float maxX;

    @BeforeEach
    public void setUp() {
        this.movimentoState = new MovimentoState();
        Dimension dimensioniSchermo = Toolkit.getDefaultToolkit().getScreenSize();
        this.maxY = (float) dimensioniSchermo.getHeight();
        this.maxX = (float) dimensioniSchermo.getWidth();
    }

    @Test
    public void testInterpretaSegnaleTroppoVeloce() {
        ISegnale segnale = new SegnaleDiscreto(0.0f, this.timeStampIniziale + (Costanti.VELOCITA_SCANSIONE / 2));
        EsitoInterpretazione esitoInterpretazione = this.movimentoState.interpreta(segnale);
        assertNull(esitoInterpretazione.getStatoSuccessivo());
        assertEquals(ETipologiaEventoSistema.NESSUN_EVENTO, esitoInterpretazione.getTipologiaEvento());
    }

    @Test
    public void testInterpretaSegnaleAvanzaScansioneY() {
        ISegnale segnale = new SegnaleDiscreto(0.0f, this.timeStampIniziale + Costanti.VELOCITA_SCANSIONE + 10L);
        EsitoInterpretazione esitoInterpretazioneesito = this.movimentoState.interpreta(segnale);
        assertNull(esitoInterpretazioneesito.getStatoSuccessivo());
        assertEquals(ETipologiaEventoSistema.AVVIA_MOVIMENTO, esitoInterpretazioneesito.getTipologiaEvento());
    }

    @Test
    public void testInterpretaSegnaleBloccaY() {
        ISegnale segnale = new SegnaleDiscreto(Costanti.SOGLIA_SEGNALE_STABILE + 1.0f, this.timeStampIniziale + Costanti.VELOCITA_SCANSIONE + 10L);
        EsitoInterpretazione esitoInterpretazione = this.movimentoState.interpreta(segnale);
        assertNull(esitoInterpretazione.getStatoSuccessivo());
        assertEquals(ETipologiaEventoSistema.NESSUN_EVENTO, esitoInterpretazione.getTipologiaEvento());
    }

    @Test
    public void testInterpretaSegnaleAvanzaScansioneX() {
        long timeStampY = this.timeStampIniziale + Costanti.VELOCITA_SCANSIONE + 10L;
        this.movimentoState.interpreta(new SegnaleDiscreto(Costanti.SOGLIA_SEGNALE_STABILE + 1.0f, timeStampY));
        long timeStampX = timeStampY + Costanti.VELOCITA_SCANSIONE + 10L;
        ISegnale segnaleX = new SegnaleDiscreto(0.0f, timeStampX);
        EsitoInterpretazione esitoInterpretazione = this.movimentoState.interpreta(segnaleX);
        assertNull(esitoInterpretazione.getStatoSuccessivo());
        assertEquals(ETipologiaEventoSistema.AVVIA_MOVIMENTO, esitoInterpretazione.getTipologiaEvento());
    }

    @Test
    public void testInterpretaSegnaleBloccaX() {
        long timeStamp = this.timeStampIniziale + 10L;
        this.movimentoState.interpreta(new SegnaleDiscreto(Costanti.SOGLIA_SEGNALE_STABILE + 1.0f, timeStamp));
        this.movimentoState.interpreta(new SegnaleDiscreto(0.0f,  timeStamp + 10L));
        ISegnale segnaleX = new SegnaleDiscreto(Costanti.SOGLIA_SEGNALE_STABILE + 1.0f, timeStamp + 20L);
        EsitoInterpretazione esitoInterpretazione = this.movimentoState.interpreta(segnaleX);
        assertNull(esitoInterpretazione.getStatoSuccessivo());
        assertEquals(ETipologiaEventoSistema.FERMA_MOVIMENTO, esitoInterpretazione.getTipologiaEvento());
    }

    @Test
    public void testInterpretaSegnaleOverflowY() {
        long timeStampCorrente = this.timeStampIniziale;
        int passiOverflow = (int) Math.ceil(1.0f / Costanti.PASSO_GRIGLIA) + 1;
        for (int i = 0; i < passiOverflow; i++) {
            timeStampCorrente += Costanti.VELOCITA_SCANSIONE + 10L;
            this.movimentoState.interpreta(new SegnaleDiscreto(0.0f, timeStampCorrente));
        }
        Punto puntoMovimento = (Punto) this.modello.getBean(Costanti.PUNTO_MOVIMENTO);
        assertNotNull(puntoMovimento);
        assertEquals(0.0f, puntoMovimento.getY(), 0.001);
    }

    @Test
    public void testInterpretaSegnaleOverflowX() {
        long timeStampCorrente = this.timeStampIniziale;
        timeStampCorrente += Costanti.VELOCITA_SCANSIONE + 10L;
        this.movimentoState.interpreta(new SegnaleDiscreto(Costanti.SOGLIA_SEGNALE_STABILE + 10.0f, timeStampCorrente));
        int passiOverflow = (int) Math.ceil(1.0f / Costanti.PASSO_GRIGLIA) + 1;
        for (int i = 0; i < passiOverflow; i++) {
            timeStampCorrente += Costanti.VELOCITA_SCANSIONE + 10L;
            this.movimentoState.interpreta(new SegnaleDiscreto(0.0f, timeStampCorrente));
        }
        Punto puntoMovimento = (Punto) this.modello.getBean(Costanti.PUNTO_MOVIMENTO);
        assertNotNull(puntoMovimento);
        assertEquals(0.0f, puntoMovimento.getX(), 0.001);
    }
    
}
