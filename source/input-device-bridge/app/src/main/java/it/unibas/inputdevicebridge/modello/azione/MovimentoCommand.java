package it.unibas.inputdevicebridge.modello.azione;

import io.quarkus.arc.Arc;
import it.unibas.inputdevicebridge.modello.Costanti;
import it.unibas.inputdevicebridge.modello.Modello;
import it.unibas.inputdevicebridge.modello.Punto;
import it.unibas.inputdevicebridge.modello.interprete.IInterpreteState;
import static java.lang.Math.round;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MovimentoCommand implements IAzioneCommand {
    
    @Override
    public void esegui() {
        Modello modello = Arc.container().instance(Modello.class).get();
        Punto punto = (Punto) modello.getBean(Costanti.PUNTO_MOVIMENTO);
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
