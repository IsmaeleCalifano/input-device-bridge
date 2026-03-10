package it.unibas.inputdevicebridge.modello.processore_segnale;

import it.unibas.inputdevicebridge.modello.Costanti;
import it.unibas.inputdevicebridge.modello.Punto;
import it.unibas.inputdevicebridge.modello.segnale.ISegnale;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FiltroSensibilita implements IFiltro {
    
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
                float deltaX = segnaleGrezzo.getPunto().getX() - this.ultimoPunto.getX();
                float deltaY = segnaleGrezzo.getPunto().getY() - this.ultimoPunto.getY();
                float x = this.ultimoPunto.getX() + (deltaX * Costanti.FATTORE_SENSIBILTA);
                float y = this.ultimoPunto.getY() + (deltaY * Costanti.FATTORE_SENSIBILTA);
                Punto nuovoPunto = new Punto(x, y);
                segnaleGrezzo.setPunto(nuovoPunto);
            }
            this.ultimoPunto = segnaleGrezzo.getPunto();
        }
    }
    
}
