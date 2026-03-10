 package it.unibas.inputdevicebridge.modello.segnale;

import it.unibas.inputdevicebridge.modello.Punto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class SegnaleAstratto implements ISegnale {
    
    private long timeStamp;

    protected SegnaleAstratto(long timeStamp) {
        this.timeStamp = timeStamp;
    }
    
    @Override
    public Boolean getAttivo() {
        return null;
    }
    
    @Override
    public void setAttivo(Boolean attivo){}

    @Override
    public Punto getPunto() {
        return null;
    }
    
    @Override
    public void setPunto(Punto punto) {}
    
}
