package it.unibas.inputdevicebridge.modello.segnale;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class SegnaleDiscreto extends SegnaleAstratto {
    
    private Boolean attivo;

    public SegnaleDiscreto(boolean attivo, long timeStamp) {
        super(timeStamp);
        this.attivo = attivo;
    }
    
}
