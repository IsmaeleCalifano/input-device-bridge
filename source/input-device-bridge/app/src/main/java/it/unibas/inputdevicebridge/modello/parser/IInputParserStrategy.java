package it.unibas.inputdevicebridge.modello.parser;

import it.unibas.inputdevicebridge.modello.segnale.ISegnale;

public interface IInputParserStrategy {
    
    public ISegnale parse(String riga) throws Exception;
    
}
