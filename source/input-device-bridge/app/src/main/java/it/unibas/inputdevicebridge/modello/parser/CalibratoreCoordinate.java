package it.unibas.inputdevicebridge.modello.parser;

import it.unibas.inputdevicebridge.modello.Punto;

public class CalibratoreCoordinate {

    private final float xLeft;
    private final float xRight;
    private final float yTop;
    private final float yBottom;

    public CalibratoreCoordinate(Punto topLeft, Punto topRight, Punto bottomRight, Punto bottomLeft) {
        this.xLeft = (topLeft.getX() + bottomLeft.getX()) / 2.0f; 
        this.xRight = (topRight.getX() + bottomRight.getX()) / 2.0f; 
        this.yTop = (topLeft.getY() + topRight.getY()) / 2.0f; 
        this.yBottom = (bottomLeft.getY() + bottomRight.getY()) / 2.0f;
    }

    public Punto calibra(float xGrezzo, float yGrezzo) { 
        float xNorm = (xGrezzo - this.xLeft) / (this.xRight - this.xLeft);
        float yNorm = (yGrezzo - this.yTop) / (this.yBottom - this.yTop);
        xNorm = Math.max(0.0f, Math.min(1.0f, xNorm));
        yNorm = Math.max(0.0f, Math.min(1.0f, yNorm));
        return new Punto(xNorm, yNorm);
    }
    
}
