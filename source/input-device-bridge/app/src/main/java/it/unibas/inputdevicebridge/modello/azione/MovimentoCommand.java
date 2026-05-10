package it.unibas.inputdevicebridge.modello.azione;

import it.unibas.inputdevicebridge.Applicazione;
import it.unibas.inputdevicebridge.modello.Costanti;
import it.unibas.inputdevicebridge.modello.Punto;
import it.unibas.inputdevicebridge.modello.interprete.IInterpreteState;
import static java.lang.Math.round;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MovimentoCommand implements IAzioneCommand {

    @Override
    public void esegui() {
        Punto punto = (Punto) Applicazione.getInstance().getModello().getBean(Costanti.PUNTO_MOVIMENTO);
        if (punto != null) {
            int x = round(punto.getX());
            int y = round(punto.getY());
            IniettoreAzione.getInstance().sposta(x, y);
            log.debug("Sto muovendo il cursore nelle cordinate (x = {}, y = {})", x, y);
        }
    }

    @Override
    public IInterpreteState getStatoSuccessivo() {
        return null;
    }

}
