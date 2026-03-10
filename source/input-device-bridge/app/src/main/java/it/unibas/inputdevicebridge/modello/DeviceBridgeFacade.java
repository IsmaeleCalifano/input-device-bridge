package it.unibas.inputdevicebridge.modello;

import it.unibas.inputdevicebridge.enums.ETipologiaAzionePersonalizzata;
import it.unibas.inputdevicebridge.enums.ETipologiaEventoPersonalizzato;
import it.unibas.inputdevicebridge.modello.azione.GestoreAzione;
import it.unibas.inputdevicebridge.modello.azione.IAzioneCommand;
import it.unibas.inputdevicebridge.modello.azione.MovimentoCommand;
import it.unibas.inputdevicebridge.modello.input_device.EyeTrackerStrategy;
import it.unibas.inputdevicebridge.modello.interprete.Interprete;
import it.unibas.inputdevicebridge.modello.processore_segnale.ProcessoreSegnaleResponsability;
import it.unibas.inputdevicebridge.modello.segnale.ISegnale;
import it.unibas.inputdevicebridge.modello.input_device.IInputDeviceStrategy;
import it.unibas.inputdevicebridge.modello.interprete.EsitoInterpretazione;
import it.unibas.inputdevicebridge.modello.interprete.IInterpreteState;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class DeviceBridgeFacade {
    
    private final IInputDeviceStrategy device;
    private final ProcessoreSegnaleResponsability processoreSegnale;
    private final Interprete interprete;
    private final GestoreAzione gestoreAzione;

    public DeviceBridgeFacade() {
        this.device = new EyeTrackerStrategy();
        this.processoreSegnale = new ProcessoreSegnaleResponsability();
        this.interprete = new Interprete();
        this.gestoreAzione = new GestoreAzione();
        this.inizializzaComandiPersonalizzati();
    }
    
    private void inizializzaComandiPersonalizzati() {
        this.gestoreAzione.putComando(ETipologiaEventoPersonalizzato.SEGNALE_BREVE, ETipologiaAzionePersonalizzata.CLICK);
        this.gestoreAzione.putComando(ETipologiaEventoPersonalizzato.SEGNALE_MEDIO, ETipologiaAzionePersonalizzata.SCROLL);
        this.gestoreAzione.putComando(ETipologiaEventoPersonalizzato.SEGNALE_LUNGO, ETipologiaAzionePersonalizzata.TRASCINAMENTO);
    }
    
    public void esegui() {
        while(true) {
            if (!device.isConnesso()) {
                this.attesaDispositivo();
            }
            ISegnale segnale = this.device.getSegnale();
            log.debug("Segnale grezzo: {}", segnale);
            this.processoreSegnale.processa(segnale);
            log.debug("Segnale filtrato: {}", segnale);
            if (segnale.getPunto() != null) {
                IAzioneCommand azioneMovimento = new MovimentoCommand(segnale.getPunto());
                azioneMovimento.esegui();
            }
            EsitoInterpretazione esitoInterpretazione = this.interprete.interpreta(segnale);
            IAzioneCommand azione = this.gestoreAzione.gestisci(esitoInterpretazione.getTipologiaEvento());
            azione.esegui();
            IInterpreteState statoSuccessivo = esitoInterpretazione.getStatoSuccessivo();
            if (azione.getStatoSuccessivo() != null) {
                statoSuccessivo = azione.getStatoSuccessivo();
            }
            if (statoSuccessivo != null && statoSuccessivo != this.interprete.getStatoCorrente()) {
                log.info(">> Procedo con il cambio di stato in {}. <<", statoSuccessivo.toString().toUpperCase());
                this.interprete.setStatoCorrente(statoSuccessivo);
            }
            float latenza = (System.nanoTime() - segnale.getTimeStamp()) / Costanti.DURATA_1_MILLISECONDO;
            log.debug("Latenza: {} ms\n", String.format("%.3f", latenza));
            try {
                // Utilizzato per rallentare il loop in modo da poter testare meglio
                Thread.sleep(100);
            } catch (InterruptedException ex) {
               log.error(ex.toString());
            }
        }
    }
    
    private void attesaDispositivo() {
        while (!this.device.isConnesso()) {            
            log.debug("In attesa di un dispositivo...");
            try {
                Thread.sleep(1_000);
            } catch (InterruptedException ex) {
               log.error(ex.toString());
            }
        }
    }
    
}
