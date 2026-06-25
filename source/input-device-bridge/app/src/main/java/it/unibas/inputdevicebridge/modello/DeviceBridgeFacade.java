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
import java.util.ArrayList;
import java.util.List;
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
    private final List<IDeviceObserver> deviceObserver = new ArrayList<>();

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
            if (this.device != null && this.device.isConnesso()) {
                this.device.disconnetti();
            }
            this.device = device;
            this.device.connetti();
            this.notificaStatoDispositivo(this.device.isConnesso());
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
        this.notificaEsecuzioneDispositivo(this.inEsecuzione);
    }

    public void esegui() {
        this.inEsecuzione = true;
        this.notificaEsecuzioneDispositivo(this.inEsecuzione);
        while (this.inEsecuzione) {
            IInputDeviceStrategy deviceCorrente;
            this.lock.lock();
            try {
                deviceCorrente = this.device;
            } finally {
                this.lock.unlock();
            }
            if (deviceCorrente == null || !deviceCorrente.isConnesso()) {
                this.attesaDispositivo(deviceCorrente);
                if (!this.inEsecuzione) {
                    break;
                }
            }
            ISegnale segnale = deviceCorrente.getSegnale();
            log.debug("Segnale grezzo: {}", segnale);
            this.processoreSegnale.processa(segnale);
            log.debug("Segnale filtrato: {}", segnale);
            this.notificaDatiSegnale(segnale);
            if (segnale.getPunto() != null) {
                Applicazione.getInstance().getModello().putBean(Costanti.PUNTO_MOVIMENTO, segnale.getPunto());
                IAzioneCommand azioneMovimento = new MovimentoCommand();
                azioneMovimento.esegui();
            }
            EsitoInterpretazione esitoInterpretazione = this.interprete.interpreta(segnale);
            IAzioneCommand azione = this.gestoreAzione.gestisci(esitoInterpretazione.getTipologiaEvento());
            azione.esegui();
            this.notificaUltimaAzione(esitoInterpretazione);
            IInterpreteState statoSuccessivo = esitoInterpretazione.getStatoSuccessivo();
            if (azione.getStatoSuccessivo() != null) {
                statoSuccessivo = azione.getStatoSuccessivo();
            }
            if (statoSuccessivo != null && statoSuccessivo != this.interprete.getStatoCorrente()) {
                log.info(">> Procedo con il cambio di stato in {}. <<", statoSuccessivo.toString().toUpperCase());
                this.interprete.setStatoCorrente(statoSuccessivo);
                this.notificaStatoApplicativo(statoSuccessivo.toString());
            }
            float latenza = (System.nanoTime() - segnale.getTimeStamp()) / Costanti.DURATA_1_MILLISECONDO;
            this.notificaLatenza(latenza);
            log.debug("Latenza: {} ms\n", String.format("%.3f", latenza));
            try {
                // Utilizzato per rallentare il loop in modo da poter testare meglio
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                this.inEsecuzione = false;
                Thread.currentThread().interrupt();
                log.error(ex.toString());
            }
        }
    }

    private void attesaDispositivo(IInputDeviceStrategy deviceCorrente) {
        while (this.inEsecuzione && (deviceCorrente == null || !deviceCorrente.isConnesso())) {
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

    public void addObserver(IDeviceObserver observer) {
        if (!this.deviceObserver.contains(observer)) {
            this.deviceObserver.add(observer);
        }
    }

    public void removeObserver(IDeviceObserver observer) {
        this.deviceObserver.remove(observer);
    }

    private void notificaLatenza(float latenzaMs) {
        for (IDeviceObserver obs : this.deviceObserver) {
            obs.onLatenzaAggiornata(latenzaMs);
        }
    }
    
    private void notificaEsecuzioneDispositivo(boolean inEsecuzione) {
        for (IDeviceObserver obs : this.deviceObserver) {
            obs.onEsecuzioneDispositivoAggiornata(inEsecuzione);
        }
    }

    private void notificaStatoDispositivo(boolean connesso) {
        for (IDeviceObserver obs : this.deviceObserver) {
            obs.onStatoDispositivoAggiornato(connesso);
        }
    }

    private void notificaStatoApplicativo(String nomeStato) {
        for (IDeviceObserver obs : this.deviceObserver) {
            obs.onStatoApplicativoAggiornato(nomeStato);
        }
    }

    private void notificaUltimaAzione(EsitoInterpretazione esitoInterpretazione) {
        ProfiloUtente profiloUtente = (ProfiloUtente) Applicazione.getInstance().getModello().getBean(Costanti.PROFILO_UTENTE_SELEZIONATO);
        if (profiloUtente == null || !profiloUtente.getMappaComandiPersonalizzati().containsKey(esitoInterpretazione.getTipologiaEvento())) {
            return;
        }
        String nomeAzione = profiloUtente.getMappaComandiPersonalizzati().get(esitoInterpretazione.getTipologiaEvento()).toString();
        for (IDeviceObserver obs : this.deviceObserver) {
            obs.onUltimaAzioneAggiornata(nomeAzione);
        }
    }

    private void notificaDatiSegnale(ISegnale segnale) {
        for (IDeviceObserver obs : this.deviceObserver) {
            obs.onDatiSegnaleAggiornati(segnale.getPunto(), segnale.getIntensita());
        }
    }

}
