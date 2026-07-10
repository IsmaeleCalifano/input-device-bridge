package it.unibas.inputdevicebridge.modello.azione;

import it.unibas.inputdevicebridge.enums.ETipologiaAzionePersonalizzata;
import it.unibas.inputdevicebridge.enums.ETipologiaEventoPersonalizzato;
import it.unibas.inputdevicebridge.enums.ETipologiaEventoSistema;
import it.unibas.inputdevicebridge.enums.ITipologiaEvento;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GestoreAzione {
    
    private final Map<ETipologiaEventoSistema, IAzioneCommand> mappaAzioniSistema = new HashMap<>();
    private final Map<ETipologiaEventoPersonalizzato, ETipologiaAzionePersonalizzata> mappaComandiPersonalizzati = new HashMap<>();
    private final Map<ETipologiaAzionePersonalizzata, IAzioneCommand> mappaAzioniPersonalizzate = new HashMap<>();

    public GestoreAzione() {
        this.inizializzaMappaAzioniSistema();
        this.inizializzaMappaAzioniPersonalizzate();
    }
    
    private void inizializzaMappaAzioniSistema() {
        this.mappaAzioniSistema.put(ETipologiaEventoSistema.RILASCIO, new RilascioCommand());
        this.mappaAzioniSistema.put(ETipologiaEventoSistema.SCROLL_SU, new ScrollCommand(-1));
        this.mappaAzioniSistema.put(ETipologiaEventoSistema.SCROLL_GIU, new ScrollCommand(1));
        this.mappaAzioniSistema.put(ETipologiaEventoSistema.AVVIA_MOVIMENTO, new MovimentoCommand());
        this.mappaAzioniSistema.put(ETipologiaEventoSistema.FERMA_MOVIMENTO, new NullObjectCommand());
    }
    
    public void inizializzamappaComandiPersonalizzati(Map<ETipologiaEventoPersonalizzato, ETipologiaAzionePersonalizzata> mappaComandiPersonalizzati) {
        this.mappaComandiPersonalizzati.putAll(mappaComandiPersonalizzati);
    }
    
    private void inizializzaMappaAzioniPersonalizzate() {
        this.mappaAzioniPersonalizzate.put(ETipologiaAzionePersonalizzata.CLICK, new ClickCommand());
        this.mappaAzioniPersonalizzate.put(ETipologiaAzionePersonalizzata.DOPPIO_CLICK, new DoppioClickCommand());
        this.mappaAzioniPersonalizzate.put(ETipologiaAzionePersonalizzata.SCROLL, new ScrollCommand());
        this.mappaAzioniPersonalizzate.put(ETipologiaAzionePersonalizzata.TRASCINAMENTO, new PressioneProlungataCommand());
    }
    
    public void putComandiPersonalizzati(Map<ETipologiaEventoPersonalizzato, ETipologiaAzionePersonalizzata> mappaComandiPersonalizzati) {
        this.mappaComandiPersonalizzati.putAll(mappaComandiPersonalizzati);
    }
    
    public void putComando(ETipologiaEventoPersonalizzato tipologiaEvento, ETipologiaAzionePersonalizzata tipologiaAzione) {
        this.mappaComandiPersonalizzati.put(tipologiaEvento, tipologiaAzione);
    }
    
    public IAzioneCommand gestisci(ITipologiaEvento tipologiaEvento) {
        if (tipologiaEvento == ETipologiaEventoSistema.NESSUN_EVENTO || tipologiaEvento == null) {
            return new NullObjectCommand(); 
        }
        IAzioneCommand azione = this.mappaAzioniSistema.get(tipologiaEvento);
        if (azione != null) {
            return azione; 
        }
        ETipologiaAzionePersonalizzata tipologiaAzione = this.mappaComandiPersonalizzati.get(tipologiaEvento);
        if (tipologiaAzione != null) {
            azione = this.mappaAzioniPersonalizzate.get(tipologiaAzione);
            if (azione != null) {
                return azione;
            }
        }
        return new NullObjectCommand();
    }
    
}
