package it.unibas.inputdevicebridge.modello.azione;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IniettoreAzione {

    private static final IniettoreAzione singleton = new IniettoreAzione();
    private Robot robot;

    private IniettoreAzione() {
        try {
            robot = new Robot();
            robot.setAutoDelay(0);
            log.info("Robot inizializzato");
        } catch (AWTException awte) {
            log.error("Errore inizializzazione Robot ", awte);
        }
    }

    public static IniettoreAzione getInstance() {
        return singleton;
    }

    public void sposta(int x, int y) {
        robot.mouseMove(x, y);
    }

    public void premiClickSinistro() {
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
    }

    public void rilasciaClickSinistro() {
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    }

    public void scroll(int quantita) {
        robot.mouseWheel(quantita);
    }
    
}
