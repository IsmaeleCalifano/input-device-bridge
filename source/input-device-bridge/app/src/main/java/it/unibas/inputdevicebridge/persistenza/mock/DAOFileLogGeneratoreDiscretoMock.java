package it.unibas.inputdevicebridge.persistenza.mock;

import it.unibas.inputdevicebridge.persistenza.DAOException;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DAOFileLogGeneratoreDiscretoMock extends DAOFileLogGeneratoreAstrattoMock {

    private static final int VALORE_NEGATIVO = 0;
    private static final int VALORE_RIPOSO = 512;
    private static final int VALORE_POSITIVO = 1023;

    private volatile boolean altPremuto = false;
    private volatile boolean ctrlPremuto = false;
    
    private final KeyEventDispatcher dispatcher;

    public DAOFileLogGeneratoreDiscretoMock() {
        this.dispatcher = new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ALT) {
                    if (e.getID() == KeyEvent.KEY_PRESSED) {
                        altPremuto = true;
                    } else if (e.getID() == KeyEvent.KEY_RELEASED) {
                        altPremuto = false;
                    }
                }
                else if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
                    if (e.getID() == KeyEvent.KEY_PRESSED) {
                        ctrlPremuto = true;
                    } else if (e.getID() == KeyEvent.KEY_RELEASED) {
                        ctrlPremuto = false;
                    }
                }
                return false;
            }
        };
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(this.dispatcher);
    }

    @Override
    protected String[] getIntestazioneFile() {
        return new String[]{
            "# File di test: segnale discreto",
            "# Formato: Timestamp(ns),Valore(int)",
            "# Calibrazione: VALORE_NEGATIVO, VALORE_RIPOSO, VALORE_POSITIVO",
            "@CALIBRAZIONE," + VALORE_NEGATIVO + "," + VALORE_RIPOSO + "," + VALORE_POSITIVO
        };
    }

    @Override
    protected void scriviRigaLog() {
        if (this.getScrittoreFile() == null) {
            return;
        }
        int valore;
        if (altPremuto) {
            valore = VALORE_POSITIVO;
        } else if (ctrlPremuto) {
            valore = VALORE_NEGATIVO;
        } else {
            valore = VALORE_RIPOSO;
        }
        this.getScrittoreFile().println(System.nanoTime() + "," + valore);
        this.getScrittoreFile().flush();
    }

    @Override
    public void chiudiFlussoLetturaFileLog() throws DAOException {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(this.dispatcher);
        super.chiudiFlussoLetturaFileLog();
    }
}