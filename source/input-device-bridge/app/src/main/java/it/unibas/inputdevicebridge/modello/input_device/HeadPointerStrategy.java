package it.unibas.inputdevicebridge.modello.input_device;

import it.unibas.inputdevicebridge.modello.Punto;
import it.unibas.inputdevicebridge.modello.parser.ParserContinuoStrategy;
import it.unibas.inputdevicebridge.modello.segnale.ISegnale;
import it.unibas.inputdevicebridge.modello.segnale.SegnaleContinuo;
import it.unibas.inputdevicebridge.persistenza.mock.DAOFileLogGeneratoreContinuoMock;

public class HeadPointerStrategy extends InputDeviceAstrattoStrategy {
    
    public HeadPointerStrategy() {
        super(new DAOFileLogGeneratoreContinuoMock(), new ParserContinuoStrategy(",", 2, 0, 1), "HeadPointer.log");
    }
    
    @Override
    protected ISegnale creaSegnaleRiposo() {
        return new SegnaleContinuo(new Punto(0.0f, 0.0f), System.nanoTime());
    }

}
