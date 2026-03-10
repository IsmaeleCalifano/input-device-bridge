package it.unibas.inputdevicebridge.modello.segnale;

import it.unibas.inputdevicebridge.modello.Punto;

public interface ISegnale {

    public long getTimeStamp();
    public void setTimeStamp(long timeStamp);
    public Boolean getAttivo();
    public void setAttivo(Boolean attivo);
    public Punto getPunto();
    public void setPunto(Punto punto);
    
}
