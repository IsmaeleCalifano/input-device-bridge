package it.unibas.inputdevicebridge.persistenza.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.quarkus.arc.properties.IfBuildProperty;
import it.unibas.inputdevicebridge.modello.profilo_utente.ArchivioProfiliUtente;
import it.unibas.inputdevicebridge.persistenza.DAOException;
import it.unibas.inputdevicebridge.persistenza.IDAOArchivioProfiliUtente;
import jakarta.enterprise.context.ApplicationScoped;
import java.io.File;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

@ApplicationScoped
@IfBuildProperty(name = "dao", stringValue = "json")
public class DAOArchivioProfiliUtenteJson implements IDAOArchivioProfiliUtente {

    @Override
    public ArchivioProfiliUtente caricaArchivioProfiliUtente() throws DAOException {
        ArchivioProfiliUtente archivio = null;
        FileReader flusso = null;
        try {
            flusso = new FileReader(this.getPercorsoFile());
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            archivio = gson.fromJson(flusso, ArchivioProfiliUtente.class);
        } catch (Exception e) {
            throw new DAOException("ERRORE: Impossibile caricare l'archivio dei profili utente da file json!", e);
        } finally {
            try {
                if (flusso != null) {
                    flusso.close();
                }
            } catch (IOException ioe) {
            }
        }
        return archivio;
    }

    @Override
    public void salvaArchivioProfiliUtente(ArchivioProfiliUtente archivioProfiliUtente) throws DAOException {
        PrintWriter flusso = null;
        try {
            FileWriter fileWriter = new FileWriter(this.getPercorsoFile());
            flusso = new PrintWriter(fileWriter);
            GsonBuilder builder = new GsonBuilder();
            builder.setPrettyPrinting();
            Gson gson = builder.create();
            String stringaJson = gson.toJson(archivioProfiliUtente);
            flusso.print(stringaJson);
        } catch (IOException ioe) {
            throw new DAOException("ERRORE: Impossibile salvare l'archivio dei profili utente su file json!", ioe);
        } finally {
            if (flusso != null) {
                flusso.close();
            }
        }
    }

    private String getPercorsoFile() {
        String userHome = System.getProperty("user.home");
        File cartellaApp = new File(userHome + "/AppData/Local/", ".input-device-bridge");
        if (!cartellaApp.exists() && !cartellaApp.mkdirs()) {
            throw new IllegalStateException("Impossibile creare la cartella " + cartellaApp.getAbsolutePath());
        }
        return new File(cartellaApp, "profiliUtente.json").getAbsolutePath();
    }

}
