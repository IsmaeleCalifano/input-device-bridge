package it.unibas.inputdevicebridge.modello;

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
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
@ApplicationScoped
public class DeviceBridgeFacade {

    @Inject
    Modello modello;

    private volatile IInputDeviceStrategy device;
    private final ProcessoreSegnaleResponsability processoreSegnale;
    @Inject
    private Interprete interprete;
    private final GestoreAzione gestoreAzione;
    private volatile boolean inEsecuzione;
    private long ultimoTimeStampProcessato = -1;
    private final Lock lock;
    private final Condition dispositivoDisponibile;
    private final List<IDeviceObserver> deviceObserver = new ArrayList<>();

    public DeviceBridgeFacade() {
        this.processoreSegnale = new ProcessoreSegnaleResponsability();
        this.gestoreAzione = new GestoreAzione();
        this.inEsecuzione = false;
        this.lock = new java.util.concurrent.locks.ReentrantLock();
        this.dispositivoDisponibile = this.lock.newCondition();
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
            dispositivoDisponibile.signalAll();
        } finally {
            this.lock.unlock();
        }
    }

    public void applicaProfiloUtente(ProfiloUtente profiloUtente) {
        this.processoreSegnale.applicaConfigurazione(profiloUtente.getSogliaZonaMorta(), profiloUtente.getSogliaSensibilita());
        this.gestoreAzione.inizializzamappaComandiPersonalizzati(profiloUtente.getMappaComandiPersonalizzati());
    }

    public void fermaEsecuzione() {
        lock.lock();
        try {
            this.inEsecuzione = false;
            dispositivoDisponibile.signalAll();
        } finally {
            lock.unlock();
        }
        this.notificaEsecuzioneDispositivo(false);
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
                this.attesaDispositivo();
                if (!inEsecuzione) {
                    break;
                }
                continue;
            }
            ISegnale segnale = deviceCorrente.getSegnale();
            if (segnale.getTimeStamp() == this.ultimoTimeStampProcessato) {
                continue;
            }
            this.ultimoTimeStampProcessato = segnale.getTimeStamp();
            log.debug("Segnale grezzo: {}", segnale);
            this.processoreSegnale.processa(segnale);
            log.debug("Segnale filtrato: {}", segnale);
            this.notificaDatiSegnale(segnale);
            IAzioneCommand azioneMovimento = new MovimentoCommand();
            if (segnale.getPunto() != null) {
                this.modello.putBean(Costanti.PUNTO_MOVIMENTO, segnale.getPunto());
                azioneMovimento.esegui();
            }
            EsitoInterpretazione esitoInterpretazione = this.interprete.interpreta(segnale);
            if (esitoInterpretazione.getPuntoAzione() != null) {
                modello.putBean(Costanti.PUNTO_MOVIMENTO, esitoInterpretazione.getPuntoAzione());
                azioneMovimento.esegui();
            }
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
            
            // /*
            try {
                // Utilizzato per rallentare il loop in modo da poter testare meglio
                Thread.sleep(50);
            } catch (InterruptedException ex) {
                this.inEsecuzione = false;
                Thread.currentThread().interrupt();
                log.error(ex.toString());
            }
            // */
            
        }
    }

    private void attesaDispositivo() {
        this.lock.lock();
        try {
            while (this.inEsecuzione && (this.device == null || !this.device.isConnesso())) {
                log.debug("In attesa di un dispositivo...");
                this.dispositivoDisponibile.await();
            }
        } catch (InterruptedException ex) {
            this.inEsecuzione = false;
            Thread.currentThread().interrupt();
            log.error(ex.toString());
        } finally {
           this.lock.unlock();
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
        for (IDeviceObserver observer : this.deviceObserver) {
            observer.onLatenzaAggiornata(latenzaMs);
        }
    }

    private void notificaEsecuzioneDispositivo(boolean inEsecuzione) {
        for (IDeviceObserver observer : this.deviceObserver) {
            observer.onEsecuzioneDispositivoAggiornata(inEsecuzione);
        }
    }

    private void notificaStatoDispositivo(boolean connesso) {
        for (IDeviceObserver observer : this.deviceObserver) {
            observer.onStatoDispositivoAggiornato(connesso);
        }
    }

    private void notificaStatoApplicativo(String nomeStato) {
        for (IDeviceObserver observer : this.deviceObserver) {
            observer.onStatoApplicativoAggiornato(nomeStato);
        }
    }

    private void notificaUltimaAzione(EsitoInterpretazione esitoInterpretazione) {
        ProfiloUtente profiloUtente = (ProfiloUtente) this.modello.getBean(Costanti.PROFILO_UTENTE_SELEZIONATO);
        if (profiloUtente == null || !profiloUtente.getMappaComandiPersonalizzati().containsKey(esitoInterpretazione.getTipologiaEvento())) {
            return;
        }
        String nomeAzione = profiloUtente.getMappaComandiPersonalizzati().get(esitoInterpretazione.getTipologiaEvento()).toString();
        for (IDeviceObserver observer : this.deviceObserver) {
            observer.onUltimaAzioneAggiornata(nomeAzione);
        }
    }

    private void notificaDatiSegnale(ISegnale segnale) {
        for (IDeviceObserver observer : this.deviceObserver) {
            observer.onDatiSegnaleAggiornati(segnale.getPunto(), segnale.getIntensita());
        }
    }

}
