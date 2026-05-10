package it.unibas.inputdevicebridge.modello.processore_segnale;

import it.unibas.inputdevicebridge.modello.Punto;
import it.unibas.inputdevicebridge.modello.segnale.ISegnale;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FiltroSensibilita implements IFiltro {
    
    private Punto ultimoPunto;
    private final float fattoreSensibilita;

    public FiltroSensibilita(float fattoreSensibilita) {
        this.fattoreSensibilita = fattoreSensibilita;
    }
    
    @Override
    public void filtra(ISegnale segnaleGrezzo) {
        if (segnaleGrezzo.getIntensita() != null) {
            float intensita = segnaleGrezzo.getIntensita() * this.fattoreSensibilita;
            if (intensita > 1.0f) {
                intensita = 1.0f;
            } else if (intensita < -1.0f) {
                intensita = -1.0f;
            }
            segnaleGrezzo.setIntensita(intensita);
        } else if (segnaleGrezzo.getPunto() != null) {
            if (this.ultimoPunto != null) {
                float deltaX = segnaleGrezzo.getPunto().getX() - this.ultimoPunto.getX();
                float deltaY = segnaleGrezzo.getPunto().getY() - this.ultimoPunto.getY();
                float x = this.ultimoPunto.getX() + (deltaX * fattoreSensibilita);
                float y = this.ultimoPunto.getY() + (deltaY * fattoreSensibilita);
                Punto nuovoPunto = new Punto(x, y);
                segnaleGrezzo.setPunto(nuovoPunto);
            }
            this.ultimoPunto = segnaleGrezzo.getPunto();
        }
    }
    
}
