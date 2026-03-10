package it.unibas.inputdevicebridge.modello.azione;

import it.unibas.inputdevicebridge.modello.interprete.ScrollState;
import it.unibas.inputdevicebridge.modello.interprete.IInterpreteState;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class ScrollCommand implements IAzioneCommand {

    private Integer quantitaScorrimento;

    @Override
    public void esegui() {
        if (this.quantitaScorrimento != null) {
            log.info(">> Eseguo azione SCROLL di {}. <<", this.quantitaScorrimento);
        }
    }

    @Override
    public IInterpreteState getStatoSuccessivo() {
        if (this.quantitaScorrimento == null) {
            return new ScrollState(System.nanoTime());
        }
        return null;
    }
}
