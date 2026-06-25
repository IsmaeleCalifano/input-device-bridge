package it.unibas.inputdevicebridge.modello.segnale;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SegnaleDiscreto extends SegnaleAstratto {
    
    private Float intensita;

    public SegnaleDiscreto(float intensita, long timeStamp) {
        super(timeStamp);
        this.intensita = intensita;
    }
    
    @Override
    public String toString() {
        return "(Intensita' = " + this.intensita + ", time stamp = " + super.getTimeStamp() + ")";
    }
    
}
