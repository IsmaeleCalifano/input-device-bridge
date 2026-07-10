package it.unibas.inputdevicebridge.modello.interprete;

import io.quarkus.arc.Arc;
import it.unibas.inputdevicebridge.enums.ETipologiaAzionePersonalizzata;
import it.unibas.inputdevicebridge.enums.ETipologiaEventoPersonalizzato;
import it.unibas.inputdevicebridge.enums.ETipologiaEventoSistema;
import it.unibas.inputdevicebridge.modello.Costanti;
import it.unibas.inputdevicebridge.modello.Modello;
import it.unibas.inputdevicebridge.modello.Punto;
import it.unibas.inputdevicebridge.modello.segnale.ISegnale;
import static java.lang.Math.abs;
import java.util.Map;

public class CalibrazioneState implements IInterpreteState {

    private Punto ultimoPunto;
    private final long timeStampInizioAcquisizione;

    public CalibrazioneState(long timeStampInizioAcquisizione) {
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
        long durataAttuale = segnale.getTimeStamp() - this.timeStampInizioAcquisizione;
        return new EsitoInterpretazione(null, ETipologiaEventoSistema.NESSUN_EVENTO, durataAttuale);
    }

    private EsitoInterpretazione esitoInterpretazione(ISegnale segnale) {
        Modello modello = Arc.container().instance(Modello.class).get();
        Map.Entry<ETipologiaEventoPersonalizzato, ETipologiaAzionePersonalizzata> entryEventoAzioneCalibrazione = (Map.Entry<ETipologiaEventoPersonalizzato, ETipologiaAzionePersonalizzata>) modello.getBean(Costanti.ENTRY_EVENTO_AZIONE_CALIBRAZIONE);
        long durataSegnale = segnale.getTimeStamp() - this.timeStampInizioAcquisizione;
        return new EsitoInterpretazione(new AttesaState(), entryEventoAzioneCalibrazione != null ? entryEventoAzioneCalibrazione.getKey() : ETipologiaEventoSistema.NESSUN_EVENTO, this.ultimoPunto, durataSegnale);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

}
