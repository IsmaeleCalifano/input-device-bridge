package it.unibas.inputdevicebridge.modello.processore_segnale;

import it.unibas.inputdevicebridge.modello.Costanti;
import it.unibas.inputdevicebridge.modello.Punto;
import it.unibas.inputdevicebridge.modello.segnale.ISegnale;
import static java.lang.Math.abs;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FiltroZoneMorte implements IFiltro {

    private Punto ultimoPunto;
    private final float soglia;

    public FiltroZoneMorte(float soglia) {
        this.soglia = Math.max(soglia, 1E-2f);
    }

    @Override
    public void filtra(ISegnale segnaleGrezzo) {
        if (segnaleGrezzo.getIntensita() != null) {
            float sogliaIntensita = (this.soglia / Costanti.SOGLIA_ZONA_MORTA) * 0.95f;
            if (abs(segnaleGrezzo.getIntensita()) <= sogliaIntensita) {
                segnaleGrezzo.setIntensita(0.0f);
            }
        } else if (segnaleGrezzo.getPunto() != null) {
            if (this.ultimoPunto != null) {
                double distanzaEuclidea = segnaleGrezzo.getPunto().calcolaDistanzaEuclidea(this.ultimoPunto);
                if (distanzaEuclidea <= this.soglia) {
                    segnaleGrezzo.setPunto(this.ultimoPunto);
                }
            }
            this.ultimoPunto = segnaleGrezzo.getPunto();
        }
    }

}
