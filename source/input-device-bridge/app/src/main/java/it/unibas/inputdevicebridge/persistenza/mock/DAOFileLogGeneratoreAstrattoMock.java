package it.unibas.inputdevicebridge.persistenza.mock;

import it.unibas.inputdevicebridge.persistenza.DAOException;
import it.unibas.inputdevicebridge.persistenza.DAOFileLog;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class DAOFileLogGeneratoreAstrattoMock extends DAOFileLog {

    private PrintWriter scrittoreFile;

    protected abstract String[] getIntestazioneFile();

    protected abstract void scriviRigaLog();

    protected PrintWriter getScrittoreFile() {
        return this.scrittoreFile;
    }

    @Override
    public void apriFlussoLetturaFileLog(String percorsoFile) throws DAOException {
        try {
            File file = new File(percorsoFile);
            boolean fileDaInizializzare = !file.exists() || file.length() == 0;
            File parent = file.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }
            this.scrittoreFile = new PrintWriter(new FileWriter(file, true));
            if (fileDaInizializzare) {
                for (String riga : this.getIntestazioneFile()) {
                    this.scrittoreFile.println(riga);
                }
                this.scrittoreFile.flush();
            }
        } catch (IOException ex) {
            log.error("ERRORE: Impossibile aprire il flusso in scrittura!", ex);
            throw new DAOException("ERRORE: Impossibile aprire il flusso in scrittura", ex);
        }

        super.apriFlussoLetturaFileLog(percorsoFile);
    }

    @Override
    public String leggiUltimaRigaFileLog() throws DAOException {
        this.scriviRigaLog();
        return super.leggiUltimaRigaFileLog();
    }

    @Override
    public void chiudiFlussoLetturaFileLog() throws DAOException {
        if (this.scrittoreFile != null) {
            this.scrittoreFile.close();
            this.scrittoreFile = null;
        }
        super.chiudiFlussoLetturaFileLog();
    }
}
