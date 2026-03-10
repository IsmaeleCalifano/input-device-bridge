package it.unibas.inputdevicebridge.modello.processore_segnale;

import it.unibas.inputdevicebridge.modello.Costanti;
import it.unibas.inputdevicebridge.modello.Punto;
import it.unibas.inputdevicebridge.modello.segnale.ISegnale;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FiltroZoneMorte implements IFiltro {
    
    private Punto ultimoPunto;

    @Override
    public void filtra(ISegnale segnaleGrezzo) {
         if (segnaleGrezzo.getAttivo() != null) {
            //TODO: Implementa logica per segnale discreto
            log.warn("TODO: Implementa logica per segnale discreto");
            return;
        }
        if (segnaleGrezzo.getPunto() != null) {
            if (this.ultimoPunto != null) {
                double distanzaEuclidea = segnaleGrezzo.getPunto().calcolaDistanzaEuclidea(this.ultimoPunto);
                if (distanzaEuclidea <= Costanti.SOGLIA_ZONA_MORTA) {
                    segnaleGrezzo.setPunto(this.ultimoPunto);
                }
            }
            this.ultimoPunto = segnaleGrezzo.getPunto();
        }
    }
    
}
