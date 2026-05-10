package it.unibas.inputdevicebridge.modello.input_device;

import it.unibas.inputdevicebridge.modello.segnale.ISegnale;
import it.unibas.inputdevicebridge.modello.segnale.SegnaleDiscreto;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SwitchStrategy extends InputDeviceAstrattoStrategy {
    
    private float intensita;
    private KeyEventDispatcher dispatcherTastiera;

    public SwitchStrategy() {
        this.catturaSegnaleSwitch();
    }

    @Override
    public ISegnale getSegnale() {
        return new SegnaleDiscreto(this.intensita, System.nanoTime());
    }

    private void catturaSegnaleSwitch() {
        this.dispatcherTastiera = new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ALT) {
                    if (e.getID() == KeyEvent.KEY_PRESSED) {
                        intensita = 1.0f;
                    } 
                    else if (e.getID() == KeyEvent.KEY_RELEASED) {
                        intensita = 0.0f;
                    }
                }
                return false; 
            }
        };
        KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(this.dispatcherTastiera);
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(this.dispatcherTastiera);
    }
    
}
