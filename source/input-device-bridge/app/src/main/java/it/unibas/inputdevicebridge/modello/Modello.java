package it.unibas.inputdevicebridge.modello;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class Modello {
    
    private final Map<String, Object> beans = new HashMap<>();
    
    public Object getBean(String key){
        return this.beans.get(key);
    }
    
    public void putBean(String key, Object value){
        this.beans.put(key, value);
    }
    
}
