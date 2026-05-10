package it.unibas.inputdevicebridge.modello.input_device;

import it.unibas.inputdevicebridge.modello.segnale.ISegnale;
import lombok.Data;

@Data
public abstract class InputDeviceAstrattoStrategy implements IInputDeviceStrategy {
    
    private ISegnale segnale;

    protected InputDeviceAstrattoStrategy() {}
    
}
