package it.unibas.inputdevicebridge.modello.interprete;

public interface IInterpreteObserver {
    
    public void onDurataSegnaleAggiornata(Long durataSegnale);
    public void onDurataSegnaleStatoTerminato(Long durataSegnale);
    
}
