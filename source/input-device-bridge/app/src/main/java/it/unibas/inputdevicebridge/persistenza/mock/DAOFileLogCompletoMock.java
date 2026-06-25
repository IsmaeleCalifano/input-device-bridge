package it.unibas.inputdevicebridge.persistenza.mock;

import it.unibas.inputdevicebridge.persistenza.DAOException;
import it.unibas.inputdevicebridge.persistenza.DAOFileLogAstratto;
import java.io.IOException;
import java.io.RandomAccessFile;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DAOFileLogCompletoMock extends DAOFileLogAstratto {

    @Override
    public String leggiUltimaRigaFileLog() throws DAOException {
        RandomAccessFile randomAccessFile = this.getRandomAccessFile();
        if (randomAccessFile == null) {
            log.error("ERRORE: Flusso in lettura non aperto!");
            throw new DAOException("ERRORE: Flusso in lettura non aperto!");
        }
        try {
            while (true) {
                String rigaCorrente = randomAccessFile.readLine();
                if (rigaCorrente != null) {
                    if (!rigaCorrente.trim().isEmpty() && !rigaCorrente.trim().startsWith("#")) {
                        return rigaCorrente;
                    }
                    continue;
                }
                log.debug("Raggiunto EOF, inizio lettura dall'inizio!");
                randomAccessFile.seek(0);
            }

        } catch (IOException ex) {
            log.error("ERRORE: Impossibile leggere il file mock", ex);
            throw new DAOException("ERRORE: Impossibile leggere il file mock", ex);
        }
    }
}