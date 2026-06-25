package it.unibas.inputdevicebridge.modello.input_device;

import it.unibas.inputdevicebridge.modello.parser.ParserDiscreto;
import it.unibas.inputdevicebridge.modello.segnale.ISegnale;
import it.unibas.inputdevicebridge.modello.segnale.SegnaleDiscreto;
import it.unibas.inputdevicebridge.persistenza.mock.DAOFileLogGeneratoreDiscretoMock;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SwitchStrategy extends InputDeviceAstrattoStrategy {
    
    public SwitchStrategy() {
        super(new DAOFileLogGeneratoreDiscretoMock(), new ParserDiscreto(",", 0, 1), "fileDiscretoMock.log");
    }

    @Override
    protected ISegnale creaSegnaleRiposo() {
        return new SegnaleDiscreto(0.0f, System.nanoTime());
    }
    
}
