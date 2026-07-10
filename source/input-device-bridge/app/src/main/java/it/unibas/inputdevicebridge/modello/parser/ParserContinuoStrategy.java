package it.unibas.inputdevicebridge.modello.parser;

import it.unibas.inputdevicebridge.modello.Punto;
import it.unibas.inputdevicebridge.modello.segnale.ISegnale;
import it.unibas.inputdevicebridge.modello.segnale.SegnaleContinuo;
import java.awt.Dimension;
import java.awt.Toolkit;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ParserContinuoStrategy implements IInputParserStrategy {

    private final String delimitatore;
    private final int indiceTimestamp;
    private final int indiceX;
    private final int indiceY;
    private final float larghezzaSchermo;
    private final float altezzaSchermo;
    private CalibratoreCoordinate calibratore;

    public ParserContinuoStrategy(String delimitatore, int indiceTimestamp, int indiceX, int indiceY) {
        this.delimitatore = delimitatore;
        this.indiceTimestamp = indiceTimestamp;
        this.indiceX = indiceX;
        this.indiceY = indiceY;
        Dimension dimensioniSchermo = Toolkit.getDefaultToolkit().getScreenSize();
        this.larghezzaSchermo = (float) dimensioniSchermo.getWidth();
        this.altezzaSchermo = (float) dimensioniSchermo.getHeight();
    }

    @Override
    public ISegnale parse(String riga)  throws Exception {
        if (riga.trim().startsWith("@CALIBRAZIONE")) {
            this.impostaCalibrazioneDaRiga(riga);
            return null;
        }
        String[] elementi = riga.split(this.delimitatore);
        long timestamp = Long.parseLong(elementi[this.indiceTimestamp].trim());
        float x = Float.parseFloat(elementi[this.indiceX].trim());
        float y = Float.parseFloat(elementi[this.indiceY].trim());
        Punto punto = new Punto(x, y);
        if (this.calibratore != null) {
            punto = this.calibratore.calibra(punto.getX(), punto.getY());
        }
        x = normalizzaValore(punto.getX(), this.larghezzaSchermo);
        y = normalizzaValore(punto.getY(), this.altezzaSchermo);
        return new SegnaleContinuo(new Punto(x, y), timestamp);
    }
    
    private float normalizzaValore(float valore, float dimensioneMassima) {
        if (valore < 0.0f) valore = 0.0f;
        if (valore > 1.0f) valore = 1.0f;
        return valore * dimensioneMassima;
    }
    
    private void impostaCalibrazioneDaRiga(String riga) {
        try {
            String[] valori = riga.replace("@CALIBRAZIONE,", "").split(this.delimitatore);
            Punto topLeft = new Punto(Float.parseFloat(valori[0].trim()), Float.parseFloat(valori[1].trim()));
            Punto topRight = new Punto(Float.parseFloat(valori[2].trim()), Float.parseFloat(valori[3].trim()));
            Punto bottomRight = new Punto(Float.parseFloat(valori[4].trim()), Float.parseFloat(valori[5].trim()));
            Punto bottomLeft = new Punto(Float.parseFloat(valori[6].trim()), Float.parseFloat(valori[7].trim()));
            this.calibratore = new CalibratoreCoordinate(topLeft, topRight, bottomRight, bottomLeft);
            log.info("Calibrazione dinamica impostata con successo dal file di log!");
        } catch (Exception e) {
            log.error("Riga di calibrazione malformata: {}", riga, e);
        }
    }
    
}