package it.unibas.inputdevicebridge.modello.processore_segnale;

import it.unibas.inputdevicebridge.modello.Costanti;
import it.unibas.inputdevicebridge.modello.segnale.ISegnale;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;

public class ProcessoreSegnaleResponsability {

    private final Lock lock;
    private final List<IFiltro> filtri = new ArrayList<>();

    public ProcessoreSegnaleResponsability() {
        this.lock = new java.util.concurrent.locks.ReentrantLock();
        this.applicaConfigurazione(Costanti.SOGLIA_ZONA_MORTA, Costanti.FATTORE_SENSIBILTA);
    }

    public void applicaConfigurazione(float sogliaZonaMorta, float sogliaSensibilita) {
        this.lock.lock();
        try {
            this.filtri.clear();
            this.addFiltro(new FiltroRumore());
            this.addFiltro(new FiltroZoneMorte(sogliaZonaMorta));
            this.addFiltro(new FiltroSensibilita(sogliaSensibilita));
        } finally {
            this.lock.unlock();
        }
    }

    private void addFiltro(IFiltro filtro) {
        this.filtri.add(filtro);
    }

    public void processa(ISegnale segnaleGrezzo) {
        this.lock.lock();
        try {
            for (IFiltro filtro : this.filtri) {
                filtro.filtra(segnaleGrezzo);
            }
        } finally {
            this.lock.unlock();
        }
    }

}
