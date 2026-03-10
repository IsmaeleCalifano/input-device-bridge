package it.unibas.inputdevicebridge.modello.segnale;

import it.unibas.inputdevicebridge.modello.Punto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SegnaleContinuo extends SegnaleAstratto {
    
    private Punto punto;

    public SegnaleContinuo(Punto punto, long timeStamp) {
        super(timeStamp);
        this.punto = punto;
    }
    
    @Override
    public String toString() {
        return "(" + this.punto + ", time stamp = " + super.getTimeStamp() + ")";
    }

}
