package it.unibas.inputdevicebridge.modello;

import it.unibas.inputdevicebridge.enums.ETipologiaAzionePersonalizzata;
import it.unibas.inputdevicebridge.enums.ETipologiaEventoPersonalizzato;
import it.unibas.inputdevicebridge.modello.profilo_utente.ProfiloUtente;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
public class CalibratoreSegnale {

    private final Map<ETipologiaAzionePersonalizzata, List<Long>> duratePerAzione = new HashMap<>();

    public void aggiungiDurataAzione(ETipologiaAzionePersonalizzata azione, long durata) {
        List<Long> listaDurate = this.duratePerAzione.get(azione);
        if (listaDurate == null) {
            listaDurate = new ArrayList<>();
            this.duratePerAzione.put(azione, listaDurate);
        }
        listaDurate.add(durata);
    }

    public int numeroElementiDurateAzione(ETipologiaAzionePersonalizzata azione) {
        List<Long> lista = this.duratePerAzione.get(azione);
        return lista != null ? lista.size() : 0;
    }

    public void resetCalibratore() {
        this.duratePerAzione.clear();
    }

    private float calcolaMedia(List<Long> listaDurate) {
        if (listaDurate == null || listaDurate.isEmpty()) {
            return 0.0f;
        }
        float sommaDurate = 0;
        for (Long durataCorrente : listaDurate) {
            sommaDurate += durataCorrente;
        }
        return sommaDurate / listaDurate.size();
    }

    private Map<ETipologiaAzionePersonalizzata, Float> creaMappaMedie() {
        Map<ETipologiaAzionePersonalizzata, Float> mappaMedie = new HashMap<>();
        for (Map.Entry<ETipologiaAzionePersonalizzata, List<Long>> entry : this.duratePerAzione.entrySet()) {
            mappaMedie.put(entry.getKey(), calcolaMedia(entry.getValue()));
        }
        return mappaMedie;
    }

    private List<Map.Entry<ETipologiaAzionePersonalizzata, Float>> ordinaMedieCrescente() {
        Map<ETipologiaAzionePersonalizzata, Float> mappaMedie = this.creaMappaMedie();
        List<Map.Entry<ETipologiaAzionePersonalizzata, Float>> listaOrdinata = new ArrayList<>(mappaMedie.entrySet());
        log.debug("Mappa non ordinata:\n{}", listaOrdinata);
        listaOrdinata.sort(Map.Entry.comparingByValue());
        log.debug("Lista ordinata crescente:\n{}", listaOrdinata);
        return listaOrdinata;
    }

    public ProfiloUtente calibraProfilo(ProfiloUtente profiloUtente) {
        List<Map.Entry<ETipologiaAzionePersonalizzata, Float>> medieOrdinate = this.ordinaMedieCrescente();
        if (medieOrdinate.size() < 3) {
            log.warn("Impossibile calibrare il profilo: numero di azioni calibrate insufficiente ({})", medieOrdinate.size());
            return null;
        }
        // SEGNALE BREVE
        ETipologiaAzionePersonalizzata azioneBreve = medieOrdinate.get(0).getKey();
        Long durataBreve = medieOrdinate.get(0).getValue().longValue();
        profiloUtente.getMappaDurataSegnale().put(ETipologiaEventoPersonalizzato.SEGNALE_BREVE, durataBreve);
        profiloUtente.getMappaComandiPersonalizzati().put(ETipologiaEventoPersonalizzato.SEGNALE_BREVE, azioneBreve);
        // SEGNALE MEDIO
        ETipologiaAzionePersonalizzata azioneMedia = medieOrdinate.get(1).getKey();
        Long durataMedia = medieOrdinate.get(1).getValue().longValue();
        profiloUtente.getMappaDurataSegnale().put(ETipologiaEventoPersonalizzato.SEGNALE_MEDIO, durataMedia);
        profiloUtente.getMappaComandiPersonalizzati().put(ETipologiaEventoPersonalizzato.SEGNALE_MEDIO, azioneMedia);
        // SEGNALE LUNGO
        ETipologiaAzionePersonalizzata azioneLunga = medieOrdinate.get(2).getKey();
        Long durataLunga = medieOrdinate.get(2).getValue().longValue();
        profiloUtente.getMappaDurataSegnale().put(ETipologiaEventoPersonalizzato.SEGNALE_LUNGO, durataLunga);
        profiloUtente.getMappaComandiPersonalizzati().put(ETipologiaEventoPersonalizzato.SEGNALE_LUNGO, azioneLunga);
        log.info("Calibrazione completata!");
        return profiloUtente;
    }
}