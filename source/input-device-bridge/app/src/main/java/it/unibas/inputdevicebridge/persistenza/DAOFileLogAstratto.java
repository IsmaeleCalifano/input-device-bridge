package it.unibas.inputdevicebridge.persistenza;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public abstract class DAOFileLogAstratto implements IDAOFileLog {

    private RandomAccessFile randomAccessFile;
    private String percorsoFile;

    @Override
    public void apriFlussoLetturaFileLog(String percorsoFile) throws DAOException {
        try {
            this.percorsoFile = percorsoFile;
            this.randomAccessFile = new RandomAccessFile(percorsoFile, "r");
            log.info("Flusso in lettura per il file indicato aperto correttamente dal disco!");
            this.verificaStrutturaFile();
        } catch (FileNotFoundException ex) {
            log.error("ERRORE: Impossibile trovare il file di log nel percorso: " + percorsoFile);
            throw new DAOException("ERRORE: Impossibile aprire flusso lettura del file di log indicato!", ex);
        }
    }

    private void verificaStrutturaFile() throws DAOException {
        try {
            this.randomAccessFile.seek(0);
            String rigaCorrente;
            while ((rigaCorrente = this.randomAccessFile.readLine()) != null) {
                rigaCorrente = rigaCorrente.trim();
                if (rigaCorrente.isEmpty() || rigaCorrente.startsWith("#")) {
                    continue;
                }
                if (rigaCorrente.startsWith("@CALIBRAZIONE")) {
                    log.info("File di log strutturato correttamente!");
                    this.randomAccessFile.seek(0);
                    return;
                }
                break;
            }
            log.error("ERRORE: File non strutturato correttamente!");
            throw new DAOException("ERRORE: File non strutturato correttamente!");
        } catch (IOException ex) {
            log.error("ERRORE: Impossibile leggere il file di log", ex);
            throw new DAOException("ERRORE: Impossibile leggere il file di log", ex);
        }
    }

    @Override
    public abstract String leggiUltimaRigaFileLog() throws DAOException;

    @Override
    public void chiudiFlussoLetturaFileLog() throws DAOException {
        try {
            if (this.randomAccessFile != null) {
                this.randomAccessFile.close();
                this.randomAccessFile = null;
                log.info("Flusso in lettura chiuso correttamente!");
            }
        } catch (IOException ex) {
            log.error("ERRORE: Impossibile chiudere flusso in lettura!");
            throw new DAOException("ERRORE: Impossibile chiudere flusso in lettura!", ex);
        }
    }
}
