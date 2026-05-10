package it.unibas.inputdevicebridge.modello;

public class Costanti {
    
    // ---------- SOGLIE  ---------- 
    public static final float SOGLIA_SEGNALE_STABILE = 0.5f;
    public static final float SOGLIA_AREA_MAX = 15.0f;
    public static final long MAX_TEMPO_INATTIVO = 3_000_000_000L;

    
    // ---------- DURATA EVENTI [ns] ---------- 
    public static final float DURATA_1_MILLISECONDO = 1_000_000.0f;
    public static final long DURATA_1_SECONDO = 1_000_000_000L;
    public static final long DURATA_2_SECONDI = 2_000_000_000L;
    public static final long DURATA_10_SECONDI = 10_000_000_000L;
    
    // ---------- CONFIGURAZIONE FILTRI ---------- 
    public static final float ALPHA_SEGNALE_STABILE = 0.1f;
    public static final float ALPHA_SEGNALE_DINAMICO = 0.9f;
    public static final float ALPHA_INTENSITA = 0.5f;
    public static final float SOGLIA_ZONA_MORTA = 3.0f;
    public static final float FATTORE_SENSIBILTA = 1.2f;
    
    // ---------- MOVIMENTO ---------- 
    public static final long VELOCITA_SCANSIONE = 500_000_000L;
    public static final float PASSO_GRIGLIA = 10.0f;
    public static final String PUNTO_MOVIMENTO = "PUNTO_MOVIMENTO";
    
    
    // ---------- MODELLO ---------- 
    public static final String ARCHIVIO_PROFILI_UTENTE = "ARCHIVIO_PROFILI_UTENTE";
    public static final String PROFILO_UTENTE_SELEZIONATO = "PROFILO_UTENTE_SELEZIONATO";

}
