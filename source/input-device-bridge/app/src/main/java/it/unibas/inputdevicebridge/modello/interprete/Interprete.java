package it.unibas.inputdevicebridge.modello.interprete;

import it.unibas.inputdevicebridge.modello.Costanti;
import it.unibas.inputdevicebridge.modello.Modello;
import it.unibas.inputdevicebridge.modello.profilo_utente.ProfiloUtente;
import it.unibas.inputdevicebridge.modello.segnale.ISegnale;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
@ApplicationScoped
public class Interprete {

    private Modello modello;
    private IInterpreteState statoCorrente;
    private List<IInterpreteObserver> interpreteObserver = new ArrayList<>();

    @Inject
    public Interprete(Modello modello) {
        this.modello = modello;
        this.statoCorrente = new AttesaState();
    }

    public EsitoInterpretazione interpreta(ISegnale segnale) {
        EsitoInterpretazione esitoInterpretazione = this.statoCorrente.interpreta(segnale);
        this.notificaDurataSegnale(esitoInterpretazione.getDurataSegnale());
        ProfiloUtente profiloUtenteTemporaneo = (ProfiloUtente) this.modello.getBean(Costanti.PROFILO_UTENTE_TEMPORANEO);
        if (profiloUtenteTemporaneo!= null && esitoInterpretazione.getStatoSuccessivo() != null && esitoInterpretazione.getDurataSegnale() != null) {
            this.notificaDurataSegnaleStatoTerminato(esitoInterpretazione.getDurataSegnale());
        }
        return esitoInterpretazione;
    }

    public void addObserver(IInterpreteObserver observer) {
        if (!this.interpreteObserver.contains(observer)) {
            this.interpreteObserver.add(observer);
        }
    }

    private void notificaDurataSegnale(Long durataSegnale) {
        for (IInterpreteObserver observer : this.interpreteObserver) {
            observer.onDurataSegnaleAggiornata(durataSegnale);
        }
    }
    
    private void notificaDurataSegnaleStatoTerminato(Long durataSegnale) {
        for (IInterpreteObserver observer : this.interpreteObserver) {
            observer.onDurataSegnaleStatoTerminato(durataSegnale);
        }
    }

}
