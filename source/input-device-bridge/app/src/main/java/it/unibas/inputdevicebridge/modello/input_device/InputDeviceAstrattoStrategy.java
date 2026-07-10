package it.unibas.inputdevicebridge.modello.input_device;

import it.unibas.inputdevicebridge.modello.parser.IInputParserStrategy;
import it.unibas.inputdevicebridge.modello.segnale.ISegnale;
import it.unibas.inputdevicebridge.persistenza.DAOException;
import it.unibas.inputdevicebridge.persistenza.IDAOFileLog;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public abstract class InputDeviceAstrattoStrategy implements IInputDeviceStrategy {

    private ISegnale segnale;
    private boolean connesso;
    private final IDAOFileLog daoFileLog;
    private final IInputParserStrategy parser;
    private final String percorsoFile;

    public InputDeviceAstrattoStrategy(IDAOFileLog daoFileLog, IInputParserStrategy parser, String nomeFile) {
        this.daoFileLog = daoFileLog;
        this.parser = parser;
        try {
            Path dir = Paths.get(System.getProperty("java.io.tmpdir"), "input-device-bridge");
            Files.createDirectories(dir);
            this.percorsoFile = dir.resolve(nomeFile).toString();
            log.debug("Percorso file: {}", this.percorsoFile);
        } catch (IOException e) {
            throw new IllegalStateException("Impossibile creare la directory temporanea", e);
        }
        this.segnale = this.creaSegnaleRiposo();
    }

    @Override
    public void connetti() {
        try {
            this.daoFileLog.apriFlussoLetturaFileLog(this.percorsoFile);
            this.connesso = true;
            log.info("Flusso del file di log aperto correttamente!");
        } catch (DAOException ex) {
            this.connesso = false;
            log.error("ERRORE: Impossibile aprire il flusso dal file di log!");
        }
    }

    @Override
    public void disconnetti() {
        try {
            this.daoFileLog.chiudiFlussoLetturaFileLog();
        } catch (DAOException ex) {
            log.error("ERRORE: Impossibile chiudere il flusso dal file di log!");
        } finally {
            this.connesso = false;
        }
    }

    @Override
    public ISegnale getSegnale() {
        if (!this.connesso) {
            return this.segnale;
        }
        try {
            String riga = this.daoFileLog.leggiUltimaRigaFileLog();
            if (riga != null) {
                ISegnale segnaleParsato = this.parser.parse(riga);
                if (segnaleParsato != null) {
                    this.segnale = segnaleParsato;
                }
            }
        } catch (Exception ex) {
            log.error("ERRORE: Impossibile effettuare correttamente il parsing!");
        }
        return this.segnale;
    }

    protected abstract ISegnale creaSegnaleRiposo();

}
