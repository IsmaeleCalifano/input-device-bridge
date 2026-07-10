package it.unibas.inputdevicebridge.persistenza;

import it.unibas.inputdevicebridge.modello.profilo_utente.ArchivioProfiliUtente;

public interface IDAOArchivioProfiliUtente {
    
    public ArchivioProfiliUtente caricaArchivioProfiliUtente() throws DAOException;
    public void salvaArchivioProfiliUtente(ArchivioProfiliUtente archivioProfiliUtente) throws DAOException;
    
}
