package it.unibas.inputdevicebridge.modello.segnale;

import it.unibas.inputdevicebridge.modello.Punto;

public interface ISegnale {

    public long getTimeStamp();
    public void setTimeStamp(long timeStamp);
    public Float getIntensita();
    public void setIntensita(Float intensita);
    public Punto getPunto();
    public void setPunto(Punto punto);
    
}
