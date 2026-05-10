package it.unibas.inputdevicebridge.persistenza;

import it.unibas.inputdevicebridge.modello.profilo_utente.ArchivioProfiliUtente;

public interface IDAOArchivioProfiliUtente {
    
    public ArchivioProfiliUtente caricaArchivioProfiliUtente(String nome) throws DAOException;
    public void salvaArchivioProfiliUtente(ArchivioProfiliUtente archivioProfiliUtente) throws DAOException;
    
}
