package it.unibas.inputdevicebridge.modello.processore_segnale;

import it.unibas.inputdevicebridge.modello.Costanti;
import it.unibas.inputdevicebridge.modello.Punto;
import it.unibas.inputdevicebridge.modello.segnale.ISegnale;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FiltroRumore implements IFiltro {

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
                float alphaAttuale = Costanti.ALPHA_SEGNALE_STABILE;
                if (distanzaEuclidea > Costanti.SOGLIA_AREA_MAX) {
                    alphaAttuale = Costanti.ALPHA_SEGNALE_DINAMICO; 
                }
                Punto smoothedPunto = this.smoothPunto(segnaleGrezzo.getPunto(), alphaAttuale);
                segnaleGrezzo.setPunto(smoothedPunto);
            }
            this.ultimoPunto = segnaleGrezzo.getPunto();
        }
    }
    
    private Punto smoothPunto(Punto puntoCorrente, float alpha) {
        float smoothedX = (this.ultimoPunto.getX() * (1 - alpha)) + (puntoCorrente.getX() * alpha);
        float smoothedY = (this.ultimoPunto.getY() * (1 - alpha)) + (puntoCorrente.getY() * alpha);
        return new Punto(smoothedX, smoothedY);
    }
    
}
