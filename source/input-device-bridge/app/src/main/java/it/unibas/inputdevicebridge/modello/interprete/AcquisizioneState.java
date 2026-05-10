package it.unibas.inputdevicebridge.modello.interprete;

import it.unibas.inputdevicebridge.Applicazione;
import it.unibas.inputdevicebridge.modello.segnale.ISegnale;
import it.unibas.inputdevicebridge.enums.ETipologiaEventoPersonalizzato;
import it.unibas.inputdevicebridge.enums.ETipologiaEventoSistema;
import it.unibas.inputdevicebridge.modello.Costanti;
import it.unibas.inputdevicebridge.modello.Punto;
import it.unibas.inputdevicebridge.modello.profilo_utente.ProfiloUtente;
import static java.lang.Math.abs;

public class AcquisizioneState implements IInterpreteState {
    
    private Punto ultimoPunto;
    private final long timeStampInizioAcquisizione;

    public AcquisizioneState(long timeStampInizioAcquisizione) {
        this.timeStampInizioAcquisizione = timeStampInizioAcquisizione;
    }
    
    @Override
    public EsitoInterpretazione interpreta(ISegnale segnale) {
        if (segnale.getIntensita() != null) {
            if (abs(segnale.getIntensita()) <= Costanti.SOGLIA_SEGNALE_STABILE) {
                return this.esitoInterpretazione(segnale);
            }
        }
        if (segnale.getPunto() != null) {
            if (this.ultimoPunto != null) {
                double distanzaEuclidea = segnale.getPunto().calcolaDistanzaEuclidea(this.ultimoPunto);
                if (distanzaEuclidea > Costanti.SOGLIA_AREA_MAX) {
                    return this.esitoInterpretazione(segnale);
                }
            }
            this.ultimoPunto = segnale.getPunto();
        }
        return new EsitoInterpretazione(null, ETipologiaEventoSistema.NESSUN_EVENTO);
    }
    
    private EsitoInterpretazione esitoInterpretazione(ISegnale segnale) {
        ProfiloUtente profiloUtente = (ProfiloUtente) Applicazione.getInstance().getModello().getBean(Costanti.PROFILO_UTENTE_SELEZIONATO);
        long durataSegnaleBreve = profiloUtente.getMappaDurataSegnale().get(ETipologiaEventoPersonalizzato.SEGNALE_BREVE);
        long durataSegnaleMedio = profiloUtente.getMappaDurataSegnale().get(ETipologiaEventoPersonalizzato.SEGNALE_MEDIO);
        long durataSegnaleLungo = profiloUtente.getMappaDurataSegnale().get(ETipologiaEventoPersonalizzato.SEGNALE_LUNGO);
        long durataSegnale = segnale.getTimeStamp() - this.timeStampInizioAcquisizione;
        if (durataSegnale >= durataSegnaleBreve && durataSegnale < durataSegnaleMedio) {
            return new EsitoInterpretazione(null, ETipologiaEventoPersonalizzato.SEGNALE_BREVE);
        } else if (durataSegnale >= durataSegnaleMedio && durataSegnale < durataSegnaleLungo) {
            return new EsitoInterpretazione(null, ETipologiaEventoPersonalizzato.SEGNALE_MEDIO);
        } else if (durataSegnale >= durataSegnaleLungo && durataSegnale < 2 * durataSegnaleLungo) {
            return new EsitoInterpretazione(null, ETipologiaEventoPersonalizzato.SEGNALE_LUNGO);
        } 
        return new EsitoInterpretazione(new AttesaState(), ETipologiaEventoSistema.NESSUN_EVENTO);
    }
    
    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
    
}
