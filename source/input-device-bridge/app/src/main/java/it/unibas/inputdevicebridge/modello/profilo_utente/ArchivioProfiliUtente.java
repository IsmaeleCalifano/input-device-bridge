package it.unibas.inputdevicebridge.modello.profilo_utente;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArchivioProfiliUtente {

    List<ProfiloUtente> listaProfiliUtente = new ArrayList<>();
    
    public void aggiungiProfiloUtente(ProfiloUtente profiloUtente) {
        this.listaProfiliUtente.add(profiloUtente);
    }
    
    public void rimuoviProfiloUtente(ProfiloUtente profiloUtente) {
        this.listaProfiliUtente.remove(profiloUtente);
    }
    
    public int size() {
        return this.listaProfiliUtente.size();
    }
    
    public ProfiloUtente getProfiloUtentePerIndice(int indice) {
        if (indice < 0 || indice >= this.listaProfiliUtente.size()) {
            return null;
        }
        return this.listaProfiliUtente.get(indice);
    }
    
    public ProfiloUtente getProfiloUtentePerNome(String nome) {
        for (ProfiloUtente profiloUtente : this.listaProfiliUtente) {
            if (profiloUtente.getNome().equals(nome)) {
                return profiloUtente;
            }
        }
        return null;
    }
    
}
