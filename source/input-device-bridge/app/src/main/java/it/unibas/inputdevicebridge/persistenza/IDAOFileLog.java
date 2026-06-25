package it.unibas.inputdevicebridge.persistenza;

public interface IDAOFileLog {
    
    public void apriFlussoLetturaFileLog(String percorsoFile) throws DAOException;
    public String leggiUltimaRigaFileLog() throws DAOException;
    public void chiudiFlussoLetturaFileLog() throws DAOException;
    
}
