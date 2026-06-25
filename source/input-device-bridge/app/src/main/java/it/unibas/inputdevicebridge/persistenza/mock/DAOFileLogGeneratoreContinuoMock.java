package it.unibas.inputdevicebridge.persistenza.mock;

import java.awt.Dimension;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Toolkit;
import java.util.Locale;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DAOFileLogGeneratoreContinuoMock extends DAOFileLogGeneratoreAstrattoMock {

    private final float larghezzaSchermo;
    private final float altezzaSchermo;

    public DAOFileLogGeneratoreContinuoMock() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.larghezzaSchermo = (float) screenSize.getWidth();
        this.altezzaSchermo = (float) screenSize.getHeight();
    }

    @Override
    protected String[] getIntestazioneFile() {
        return new String[]{
            "# File di test: segnale continuo",
            "# Formato: X(float),Y(float),Timestamp(ns)",
            "# Calibrazione: xTL; yTL; xTR; yTR; xBR; yBR; xBL; yBL",
            "@CALIBRAZIONE, 0.0,0.0, 1.0,0.0, 1.0,1.0, 0.0,1.0"
        };
    }

    @Override
    protected void scriviRigaLog() {
        if (this.getScrittoreFile() == null) {
            return;
        }
        Point punto = MouseInfo.getPointerInfo().getLocation();
        float normX = Math.max(0.0f, Math.min(1.0f, punto.x / larghezzaSchermo));
        float normY = Math.max(0.0f, Math.min(1.0f, punto.y / altezzaSchermo));
        this.getScrittoreFile().println(String.format(Locale.US, "%.2f,%.2f,%d", normX, normY, System.nanoTime()));
        this.getScrittoreFile().flush();
    }
}
