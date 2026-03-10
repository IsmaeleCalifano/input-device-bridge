package it.unibas.inputdevicebridge.modello.processore_segnale;

import it.unibas.inputdevicebridge.modello.segnale.ISegnale;
import java.util.ArrayList;
import java.util.List;

public class ProcessoreSegnaleResponsability {
    
    List<IFiltro> filtri = new ArrayList<>();

    public ProcessoreSegnaleResponsability() {
        this.addFiltro(new FiltroZoneMorte());
        this.addFiltro(new FiltroRumore());
        this.addFiltro(new FiltroSensibilita());
    }
    
    public void addFiltro(IFiltro filtro) {
        this.filtri.add(filtro);
    }
    
    public void processa(ISegnale segnaleGrezzo) {
        for (IFiltro filtro : this.filtri) {
            filtro.filtra(segnaleGrezzo);
        }
    }
    
}

