package de.allroundjonu.Utils;

import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class ConfigValidator {

    private JSONObject config;
    private boolean valid;

    public ConfigValidator(JSONObject config) {
        this.config = config;
    }

    public boolean validateField(String key){
        return getObject(key) != null;
    }

    public boolean doValidationCheck(Predicate<List<Object>> validationCheck, List<String> keys){
         List<Object> values = new ArrayList<>();
        for(String s: keys){
            if(!validateField(s)) return false;
            values.add(config.get(s));
        }

       return validationCheck.test(values);
    }

    public Object getObject(String key){
        String[] keys = key.split("\\.");
        JSONObject previousObject = null;
        for(String k: keys){
            if(previousObject == null ){
                previousObject = (JSONObject) config.get(k);
            }else{
                if(k.equals(keys[keys.length-1])){
                    return previousObject.get(k);
                }else{
                    previousObject = (JSONObject) previousObject.get(k);
                }
            }

        }
        return null;
    }

}

