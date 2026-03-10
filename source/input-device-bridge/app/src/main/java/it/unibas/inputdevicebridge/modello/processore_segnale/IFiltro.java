package it.unibas.inputdevicebridge.modello.processore_segnale;

import it.unibas.inputdevicebridge.modello.segnale.ISegnale;

public interface IFiltro {
    
    public void filtra(ISegnale segnaleGrezzo);
    
}
