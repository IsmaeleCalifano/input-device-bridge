package it.unibas.inputdevicebridge.persistenza;

import java.io.IOException;
import java.io.RandomAccessFile;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DAOFileLog extends DAOFileLogAstratto {

    @Override
    public String leggiUltimaRigaFileLog() throws DAOException {
        RandomAccessFile randomAccessFile = this.getRandomAccessFile();
        if (randomAccessFile == null) {
            log.error("ERRORE: Flusso in lettura non aperto!");
            throw new DAOException("ERRORE: Flusso in lettura non aperto!");
        }
        try {
            if (randomAccessFile.length() < randomAccessFile.getFilePointer()) {
                randomAccessFile.seek(0);
            }
            String rigaCorrente;
            String ultimaRiga = null;
            while ((rigaCorrente = randomAccessFile.readLine()) != null) {
                if (rigaCorrente.trim().startsWith("@CALIBRAZIONE")) {
                    return rigaCorrente;
                }
                if (!rigaCorrente.trim().isEmpty() && !rigaCorrente.trim().startsWith("#")) {
                    ultimaRiga = rigaCorrente;
                }
            }
            return ultimaRiga;
        } catch (IOException ex) {
            log.error("ERRORE: Impossibile leggere il file", ex);
            throw new DAOException("ERRORE: Impossibile leggere il file", ex);
        }
    }
}