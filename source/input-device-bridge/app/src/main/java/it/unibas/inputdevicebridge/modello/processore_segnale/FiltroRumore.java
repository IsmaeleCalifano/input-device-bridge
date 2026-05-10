package it.unibas.inputdevicebridge.modello.processore_segnale;

import it.unibas.inputdevicebridge.modello.Costanti;
import it.unibas.inputdevicebridge.modello.Punto;
import it.unibas.inputdevicebridge.modello.segnale.ISegnale;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FiltroRumore implements IFiltro {

    private Float ultimaIntensita;
    private Punto ultimoPunto;

    @Override
    public void filtra(ISegnale segnaleGrezzo) {
         if (segnaleGrezzo.getIntensita()!= null) {
             if (this.ultimaIntensita != null) {
                float smoothedIntensita = this.smoothFloat(this.ultimaIntensita, segnaleGrezzo.getIntensita(), Costanti.ALPHA_INTENSITA);
                segnaleGrezzo.setIntensita(smoothedIntensita);
            }
            this.ultimaIntensita = segnaleGrezzo.getIntensita();
        } else if (segnaleGrezzo.getPunto() != null) {
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
        float smoothedX = this.smoothFloat(this.ultimoPunto.getX(), puntoCorrente.getX(), alpha);
        float smoothedY = this.smoothFloat(this.ultimoPunto.getY(), puntoCorrente.getY(), alpha);
        return new Punto(smoothedX, smoothedY);
    }
    
    private float smoothFloat(float valorePrecedente, float valoreCorrente, float alpha) {
        return (valorePrecedente * (1 - alpha)) + (valoreCorrente * alpha);
    }
    
}
