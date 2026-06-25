package it.unibas.inputdevicebridge.modello;

public interface IDeviceObserver {

    public void onLatenzaAggiornata(float latenzaMs);
    
    public void onEsecuzioneDispositivoAggiornata(boolean inEsecuzione);

    public void onStatoDispositivoAggiornato(boolean connesso);

    public void onStatoApplicativoAggiornato(String nomeStato);

    public void onUltimaAzioneAggiornata(String nomeAzione);

    public void onDatiSegnaleAggiornati(Punto coordinate, Float intensita);
    
}
