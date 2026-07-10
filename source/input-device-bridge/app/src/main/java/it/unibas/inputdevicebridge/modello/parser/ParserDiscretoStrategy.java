package it.unibas.inputdevicebridge.modello.parser;

import it.unibas.inputdevicebridge.modello.segnale.ISegnale;
import it.unibas.inputdevicebridge.modello.segnale.SegnaleDiscreto;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ParserDiscretoStrategy implements IInputParserStrategy {

    private final String delimitatore;
    private final int indiceTimestamp;
    private final int indiceIntensita;
    private float valoreMinimo = -1.0f;
    private float valoreRiposo = 0.0f;
    private float valoreMassimo = 1.0f;

    public ParserDiscretoStrategy(String delimitatore, int indiceTimestamp, int indiceIntensita) {
        this.delimitatore = delimitatore;
        this.indiceTimestamp = indiceTimestamp;
        this.indiceIntensita = indiceIntensita;
    }

    @Override
    public ISegnale parse(String riga) throws Exception {
        if (riga.trim().startsWith("@CALIBRAZIONE")) {
            log.debug("Calibrazione trovata!");
            this.impostaCalibrazioneDaRiga(riga);
            return null;
        }
        String[] elementi = riga.split(this.delimitatore);
        long timeStamp = Long.parseLong(elementi[this.indiceTimestamp].trim());
        float intensita = Float.parseFloat(elementi[this.indiceIntensita].trim());
        intensita = this.normalizzaValore(intensita);
        return new SegnaleDiscreto(intensita, timeStamp);
    }

    private void impostaCalibrazioneDaRiga(String riga) {
        try {
            String[] valori = riga.replace("@CALIBRAZIONE,", "").split(this.delimitatore);
            this.valoreMinimo = Float.parseFloat(valori[0].trim());
            this.valoreRiposo = Float.parseFloat(valori[1].trim());
            this.valoreMassimo = Float.parseFloat(valori[2].trim());
            log.info("Parser discreto calibrato con successo dal log: Min={}, Riposo={}, Max={}", this.valoreMinimo, this.valoreRiposo, this.valoreMassimo);
        } catch (Exception e) {
            log.error("Riga di calibrazione malformata: {}", riga, e);
        }
    }

    private float normalizzaValore(float valore) {
        if (valore >= this.valoreRiposo) {
            float rangePositivo = this.valoreMassimo - this.valoreRiposo;
            if (rangePositivo == 0) return 0.0f;
            float norm = (valore - this.valoreRiposo) / rangePositivo;
            return Math.min(1.0f, norm);
        } else {
            float rangeNegativo = this.valoreRiposo - this.valoreMinimo;
            if (rangeNegativo == 0) return 0.0f;
            float norm = (valore - this.valoreRiposo) / rangeNegativo;
            return Math.max(-1.0f, norm);
        }
    }

}