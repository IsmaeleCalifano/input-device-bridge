package it.unibas.inputdevicebridge;

import it.unibas.inputdevicebridge.modello.DeviceBridgeFacade;
import it.unibas.inputdevicebridge.modello.Modello;
import lombok.Getter;

@Getter
public class Applicazione {
    
    private static final Applicazione singleton = new Applicazione();
    
    public static Applicazione getInstance() {
        return singleton;
    }
    
    private Applicazione() {}
    
    private final Modello modello = new Modello();

    public static void main(String[] args) {
        singleton.esegui();
    }
    
    private void esegui() {
        DeviceBridgeFacade deviceBridge = new DeviceBridgeFacade();
        deviceBridge.esegui();
    }
    
}
