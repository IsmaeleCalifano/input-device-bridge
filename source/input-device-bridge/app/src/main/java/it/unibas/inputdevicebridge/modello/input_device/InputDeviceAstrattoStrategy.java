package it.unibas.inputdevicebridge.modello.input_device;

import it.unibas.inputdevicebridge.modello.segnale.ISegnale;
import lombok.Data;

@Data
public abstract class InputDeviceAstrattoStrategy implements IInputDeviceStrategy {
    
    private boolean connesso;
    private ISegnale segnale;

    protected InputDeviceAstrattoStrategy() {}
    
    @Override
    public void connetti() {
        this.connesso = true;
    }

    @Override
    public void disconnetti() {
        this.connesso = false;
    }
    
}
