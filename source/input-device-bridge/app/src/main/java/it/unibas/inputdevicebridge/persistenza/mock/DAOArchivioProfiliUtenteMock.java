package it.unibas.inputdevicebridge.persistenza.mock;

import io.quarkus.arc.properties.IfBuildProperty;
import it.unibas.inputdevicebridge.enums.ETipologiaAzionePersonalizzata;
import it.unibas.inputdevicebridge.enums.ETipologiaEventoPersonalizzato;
import it.unibas.inputdevicebridge.modello.Costanti;
import it.unibas.inputdevicebridge.modello.profilo_utente.ArchivioProfiliUtente;
import it.unibas.inputdevicebridge.modello.profilo_utente.ProfiloUtente;
import it.unibas.inputdevicebridge.persistenza.DAOException;
import it.unibas.inputdevicebridge.persistenza.IDAOArchivioProfiliUtente;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@IfBuildProperty(name = "dao", stringValue = "mock")
public class DAOArchivioProfiliUtenteMock implements IDAOArchivioProfiliUtente {

    @Override
    public ArchivioProfiliUtente caricaArchivioProfiliUtente() throws DAOException {
        Map<ETipologiaEventoPersonalizzato, Long> mappaDurataSegnale = new HashMap<>();
        mappaDurataSegnale.put(ETipologiaEventoPersonalizzato.SEGNALE_BREVE, Costanti.DURATA_2_SECONDI);
        mappaDurataSegnale.put(ETipologiaEventoPersonalizzato.SEGNALE_MEDIO, 6 * Costanti.DURATA_1_SECONDO);
        mappaDurataSegnale.put(ETipologiaEventoPersonalizzato.SEGNALE_LUNGO, Costanti.DURATA_10_SECONDI);
        Map<ETipologiaEventoPersonalizzato, ETipologiaAzionePersonalizzata> mappaComandiPersonalizzati = new HashMap<>();
        mappaComandiPersonalizzati.put(ETipologiaEventoPersonalizzato.SEGNALE_BREVE, ETipologiaAzionePersonalizzata.CLICK);
        mappaComandiPersonalizzati.put(ETipologiaEventoPersonalizzato.SEGNALE_MEDIO, ETipologiaAzionePersonalizzata.SCROLL);
        mappaComandiPersonalizzati.put(ETipologiaEventoPersonalizzato.SEGNALE_LUNGO, ETipologiaAzionePersonalizzata.TRASCINAMENTO);
        ProfiloUtente profiloUtente01 = new ProfiloUtente("Utente 01", 0.4f, 1.2f);
        profiloUtente01.setMappaDurataSegnale(mappaDurataSegnale);
        profiloUtente01.setMappaComandiPersonalizzati(mappaComandiPersonalizzati);
        ProfiloUtente profiloUtente02 = new ProfiloUtente("Utente 02", 3.0f, 1.0f);
        profiloUtente02.setMappaDurataSegnale(mappaDurataSegnale);
        profiloUtente02.setMappaComandiPersonalizzati(mappaComandiPersonalizzati);
        ProfiloUtente profiloUtente03 = new ProfiloUtente("Utente 03", 1.2f, 1.5f);
        profiloUtente03.setMappaDurataSegnale(mappaDurataSegnale);
        profiloUtente03.setMappaComandiPersonalizzati(mappaComandiPersonalizzati);
        ProfiloUtente profiloUtente04 = new ProfiloUtente("Utente 04", 2.0f, 2.0f);
        profiloUtente04.setMappaDurataSegnale(mappaDurataSegnale);
        profiloUtente04.setMappaComandiPersonalizzati(mappaComandiPersonalizzati);
        ArchivioProfiliUtente archivioProfiliUtente = new ArchivioProfiliUtente();
        archivioProfiliUtente.aggiungiProfiloUtente(profiloUtente01);
        archivioProfiliUtente.aggiungiProfiloUtente(profiloUtente02);
        archivioProfiliUtente.aggiungiProfiloUtente(profiloUtente03);
        archivioProfiliUtente.aggiungiProfiloUtente(profiloUtente04);
        log.debug("Archivio profili utente caricato correttamente!");
        return archivioProfiliUtente;
    }

    @Override
    public void salvaArchivioProfiliUtente(ArchivioProfiliUtente archivioProfiliUtente) throws DAOException {
        log.debug("Profilo salvato correttamente.");
    }
    
}
