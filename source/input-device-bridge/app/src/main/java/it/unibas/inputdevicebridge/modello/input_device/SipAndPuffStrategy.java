package it.unibas.inputdevicebridge.modello.input_device;

import it.unibas.inputdevicebridge.modello.parser.ParserDiscretoStrategy;
import it.unibas.inputdevicebridge.modello.segnale.ISegnale;
import it.unibas.inputdevicebridge.modello.segnale.SegnaleDiscreto;
import it.unibas.inputdevicebridge.persistenza.mock.DAOFileLogGeneratoreDiscretoMock;

public class SipAndPuffStrategy extends InputDeviceAstrattoStrategy {
    
    public SipAndPuffStrategy() {
        super(new DAOFileLogGeneratoreDiscretoMock(), new ParserDiscretoStrategy(",", 0, 1), "SipAndPuff.log");
    }

    @Override
    protected ISegnale creaSegnaleRiposo() {
        return new SegnaleDiscreto(0.0f, System.nanoTime());
    }
    
}
