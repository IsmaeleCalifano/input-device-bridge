package it.unibas.inputdevicebridge.modello;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Punto {
    
    private final float x;
    private final float y;
    
    public double calcolaDistanzaEuclidea(Punto punto) {
        return Math.sqrt(Math.pow(this.x - punto.getX(), 2) + Math.pow(this.y - punto.getY(), 2));
    }

    @Override
    public String toString() {
        return "x = " + this.x + ", y = " + this.y;
    }
    
    
    
}
