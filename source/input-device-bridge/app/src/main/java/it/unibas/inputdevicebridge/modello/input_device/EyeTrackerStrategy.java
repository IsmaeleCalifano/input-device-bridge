package it.unibas.inputdevicebridge.modello.input_device;

import it.unibas.inputdevicebridge.modello.Punto;
import it.unibas.inputdevicebridge.modello.segnale.ISegnale;
import it.unibas.inputdevicebridge.modello.segnale.SegnaleContinuo;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;

public class EyeTrackerStrategy extends InputDeviceAstrattoStrategy {

    public EyeTrackerStrategy() {
        super.connetti();
    }

    @Override
    public ISegnale getSegnale() {
        this.catturaSegnaleMouse();
        return super.getSegnale();
    }
    
    private void catturaSegnaleMouse() {
        PointerInfo info = MouseInfo.getPointerInfo();
        if (info != null) {
            Point puntoCorrente = info.getLocation();
            Punto punto = new Punto((float) puntoCorrente.getX(), (float) puntoCorrente.getY());
            super.setSegnale(new SegnaleContinuo(punto, System.nanoTime())); 
        }
    }

}
