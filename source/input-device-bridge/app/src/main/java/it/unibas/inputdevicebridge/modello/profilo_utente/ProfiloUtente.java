package it.unibas.inputdevicebridge.modello.profilo_utente;

import it.unibas.inputdevicebridge.enums.ETipologiaAzionePersonalizzata;
import it.unibas.inputdevicebridge.enums.ETipologiaEventoPersonalizzato;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProfiloUtente {
    
    private String nome;
    private float sogliaZonaMorta;
    private float sogliaSensibilita;
    private Map<ETipologiaEventoPersonalizzato, Long> mappaDurataSegnale = new HashMap<>();
    private Map<ETipologiaEventoPersonalizzato, ETipologiaAzionePersonalizzata> mappaComandiPersonalizzati = new HashMap<>();

    public ProfiloUtente(String nome, float sogliaZonaMorta, float sogliaSensibilita) {
        this.nome = nome;
        this.sogliaZonaMorta = sogliaZonaMorta;
        this.sogliaSensibilita = sogliaSensibilita;
    }
    
}
