package it.unibas.inputdevicebridge.modello.input_device;

import it.unibas.inputdevicebridge.modello.Punto;
import it.unibas.inputdevicebridge.modello.parser.ParserContinuo;
import it.unibas.inputdevicebridge.modello.segnale.ISegnale;
import it.unibas.inputdevicebridge.modello.segnale.SegnaleContinuo;
import it.unibas.inputdevicebridge.persistenza.mock.DAOFileLogGeneratoreContinuoMock;

public class EyeTrackerStrategy extends InputDeviceAstrattoStrategy {

    public EyeTrackerStrategy() {
        super(new DAOFileLogGeneratoreContinuoMock(), new ParserContinuo(",", 2, 0, 1), "fileContinuoMock.log");
        //super(new DAOFileLogCompletoMock(), new ParserContinuo(",", 2, 0, 1), "fileContinuoMock.log");
    }

    @Override
    protected ISegnale creaSegnaleRiposo() {
        return new SegnaleContinuo(new Punto(0.0f, 0.0f), System.nanoTime());
    }

}
