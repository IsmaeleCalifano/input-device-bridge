package it.unibas.inputdevicebridge.vista;

import it.unibas.inputdevicebridge.modello.Costanti;
import it.unibas.inputdevicebridge.modello.DeviceBridgeFacade;
import it.unibas.inputdevicebridge.modello.IDeviceObserver;
import it.unibas.inputdevicebridge.modello.Punto;
import it.unibas.inputdevicebridge.modello.interprete.IInterpreteObserver;
import it.unibas.inputdevicebridge.modello.interprete.Interprete;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import javax.swing.*;
import java.awt.*;

@Singleton
public class WindowDurataSegnale extends javax.swing.JWindow implements IDeviceObserver, IInterpreteObserver {

    private final DeviceBridgeFacade deviceBridgeFacade;
    private final Interprete interprete;
    private final VistaDurataSegnale vistaDurataSegnale;

    @Inject
    public WindowDurataSegnale(DeviceBridgeFacade deviceBridgeFacade, Interprete interprete, VistaDurataSegnale vistaDurataSegnale) {
        this.deviceBridgeFacade = deviceBridgeFacade;
        this.interprete = interprete;
        this.vistaDurataSegnale = vistaDurataSegnale;
    }

    public void inizializza() {
        this.inizializzaComponenti();
        this.deviceBridgeFacade.addObserver(this);
        this.interprete.addObserver(this);

    }

    private void inizializzaComponenti() {
        this.setAlwaysOnTop(true);
        this.setFocusableWindowState(false);
        this.setBackground(new Color(0, 0, 0, 0));
        this.setContentPane(this.vistaDurataSegnale);
        this.pack();
    }

    @Override
    public void onDurataSegnaleAggiornata(Long durataSegnale) {
        SwingUtilities.invokeLater(() -> {
            if (durataSegnale != null) {
                float secondi = durataSegnale / (float) Costanti.DURATA_1_SECONDO;
                this.vistaDurataSegnale.setTextLabelSecondi(secondi);
                if (secondi > 0.2) {
                    this.setVisible(true);
                    Point posizioneCursore = MouseInfo.getPointerInfo().getLocation();
                    int xFinestra = (int) posizioneCursore.getX() + 25;
                    int yFinestra = (int) posizioneCursore.getY() + 25;
                    this.setLocation(xFinestra, yFinestra);
                }
            } else {
                this.setVisible(false);
                this.vistaDurataSegnale.resetTextLabelSecondi();
            }
        });
    }

    @Override
    public void onDurataSegnaleStatoTerminato(Long durataSegnale) {
    }

    @Override
    public void onLatenzaAggiornata(float latenzaMs) {
    }

    @Override
    public void onEsecuzioneDispositivoAggiornata(boolean inEsecuzione) {
        SwingUtilities.invokeLater(() -> {
            if (!inEsecuzione) {
                this.setVisible(false);
            }
        });
    }

    @Override
    public void onStatoDispositivoAggiornato(boolean connesso) {
    }

    @Override
    public void onStatoApplicativoAggiornato(String nomeStato) {
    }

    @Override
    public void onUltimaAzioneAggiornata(String nomeAzione) {
    }

    @Override
    public void onDatiSegnaleAggiornati(Punto coordinate, Float intensita) {
    }

}
