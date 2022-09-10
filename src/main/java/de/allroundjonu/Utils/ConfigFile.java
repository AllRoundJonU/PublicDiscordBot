package de.allroundjonu.Utils;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class ConfigFile extends JSONObject {

    public ConfigFile createFromJsonObject(JSONObject object){
        this.putAll(object);
        return this;
    }

    public Object getObject(String key){
        String[] keys = key.split("\\.");
        JSONObject previousObject = null;
        for(String k: keys){
            if(previousObject == null ){
                previousObject = (JSONObject) this.get(k);
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
