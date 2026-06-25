package it.unibas.inputdevicebridge.modello.interprete;

import it.unibas.inputdevicebridge.Applicazione;
import it.unibas.inputdevicebridge.enums.ETipologiaAzionePersonalizzata;
import it.unibas.inputdevicebridge.enums.ETipologiaEventoPersonalizzato;
import it.unibas.inputdevicebridge.enums.ETipologiaEventoSistema;
import it.unibas.inputdevicebridge.modello.Costanti;
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
        return new EsitoInterpretazione(null, ETipologiaEventoSistema.NESSUN_EVENTO);
    }

    private EsitoInterpretazione esitoInterpretazione(ISegnale segnale) {
        long durataSegnale = segnale.getTimeStamp() - this.timeStampInizioAcquisizione;
        Applicazione.getInstance().getModello().putBean(Costanti.DURATA_SEGNALE_ACQUISITO, durataSegnale);
        Map.Entry<ETipologiaEventoPersonalizzato, ETipologiaAzionePersonalizzata> entryEventoAzioneCalibrazione = (Map.Entry<ETipologiaEventoPersonalizzato, ETipologiaAzionePersonalizzata>) Applicazione.getInstance().getModello().getBean(Costanti.ENTRY_EVENTO_AZIONE_CALIBRAZIONE);
        return new EsitoInterpretazione(new AttesaState(), entryEventoAzioneCalibrazione != null ? entryEventoAzioneCalibrazione.getKey() : ETipologiaEventoSistema.NESSUN_EVENTO);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

}
