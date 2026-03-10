package it.unibas.inputdevicebridge.modello.input_device;

import it.unibas.inputdevicebridge.modello.segnale.ISegnale;

public interface IInputDeviceStrategy {

    public boolean isConnesso();
    public ISegnale getSegnale();
    public void connetti();
    public void disconnetti();
    
    
}
