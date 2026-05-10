package it.unibas.inputdevicebridge.modello;

import it.unibas.inputdevicebridge.Applicazione;
import it.unibas.inputdevicebridge.modello.azione.GestoreAzione;
import it.unibas.inputdevicebridge.modello.azione.IAzioneCommand;
import it.unibas.inputdevicebridge.modello.azione.MovimentoCommand;
import it.unibas.inputdevicebridge.modello.interprete.Interprete;
import it.unibas.inputdevicebridge.modello.processore_segnale.ProcessoreSegnaleResponsability;
import it.unibas.inputdevicebridge.modello.segnale.ISegnale;
import it.unibas.inputdevicebridge.modello.input_device.IInputDeviceStrategy;
import it.unibas.inputdevicebridge.modello.interprete.EsitoInterpretazione;
import it.unibas.inputdevicebridge.modello.interprete.IInterpreteState;
import it.unibas.inputdevicebridge.modello.profilo_utente.ProfiloUtente;
import java.util.concurrent.locks.Lock;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class DeviceBridgeFacade {

    private volatile IInputDeviceStrategy device;
    private final ProcessoreSegnaleResponsability processoreSegnale;
    private final Interprete interprete;
    private final GestoreAzione gestoreAzione;
    private volatile boolean inEsecuzione;
    private final Lock lock;
    
    public DeviceBridgeFacade() {
        this.processoreSegnale = new ProcessoreSegnaleResponsability();
        this.interprete = new Interprete();
        this.gestoreAzione = new GestoreAzione();
        this.inEsecuzione = false;
        this.lock = new java.util.concurrent.locks.ReentrantLock();
    }

    public void setDevice(IInputDeviceStrategy device) {
        this.lock.lock();
        try {
            this.device = device;

        } finally {
            this.lock.unlock();
        }
    }

    public void applicaProfiloUtente(ProfiloUtente profiloUtente) {
        this.processoreSegnale.applicaConfigurazione(profiloUtente.getSogliaZonaMorta(), profiloUtente.getSogliaSensibilita());
        this.gestoreAzione.inizializzamappaComandiPersonalizzati(profiloUtente.getMappaComandiPersonalizzati());
    }

    public void fermaEsecuzione() {
        this.inEsecuzione = false;
    }

    public void esegui() {
        this.inEsecuzione = true;
        while (this.inEsecuzione) {
            this.lock.lock();
            try {
                if (this.device == null) {
                    this.attesaDispositivo();
                    if (!this.inEsecuzione) {
                        break;
                    }
                }
                ISegnale segnale = this.device.getSegnale();
                log.debug("Segnale grezzo: {}", segnale);
                this.processoreSegnale.processa(segnale);
                log.debug("Segnale filtrato: {}", segnale);
                if (segnale.getPunto() != null) {
                    Applicazione.getInstance().getModello().putBean(Costanti.PUNTO_MOVIMENTO, segnale.getPunto());
                    IAzioneCommand azioneMovimento = new MovimentoCommand();
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
            } finally {
                this.lock.unlock();
            }
            try {
                // Utilizzato per rallentare il loop in modo da poter testare meglio
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                this.inEsecuzione = false;
                Thread.currentThread().interrupt();
                log.error(ex.toString());
            }
        }
    }

    private void attesaDispositivo() {
        while (this.device == null && this.inEsecuzione) {
            log.debug("In attesa di un dispositivo...");
            try {
                Thread.sleep(1_000);
            } catch (InterruptedException ex) {
                this.inEsecuzione = false;
                Thread.currentThread().interrupt();
                log.error(ex.toString());
            }
        }
    }

}
