package JStream;

import java.util.HashMap;
import java.util.Map;

public class JsonData {
    private Map<String, Object> data;

    public JsonData() {
        data = new HashMap<>();
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public Map<String, Object> getData(){
        return data;
    }

    public void put(String key, Object value) {
        data.put(key, value);
    }

    public Object get(String key) {
        return data.get(key);
    }

    public boolean has(String key) {
        return data.containsKey(key);
    }

    public String toJsonString() {
        StringBuilder json = new StringBuilder();

        for (Map.Entry<String, Object> entry : data.entrySet()) {
            if (!json.isEmpty()) {
                json.append(",");
            } else {
                json.append("{");
            }
            json.append("\"").append(entry.getKey()).append("\":");

            if (entry.getValue() instanceof String) {
                json.append("\"").append(entry.getValue()).append("\"");
            } else if(entry.getValue() instanceof Map){
                String recursiveReturnedString = recursiveHelper((Map)entry.getValue());
                json.append(recursiveReturnedString);
            }else {
                json.append(entry.getValue());
            }

        }

        //In case no field in the object
        if (data.isEmpty()) {
            json.append("{");
        }

        json.append("}");
        return json.toString();
    }

    private String recursiveHelper(Map<String,Object> value){
        StringBuilder json = new StringBuilder();

        for (Map.Entry<String, Object> entry : value.entrySet()) {
            if (json.length() > 0) {
                json.append(",");
            } else {
                json.append("{");
            }
            json.append("\"").append(entry.getKey()).append("\":");

            if (entry.getValue() instanceof String) {
                json.append("\"").append(entry.getValue()).append("\"");
            } else if(entry.getValue() instanceof Map){
                String recursiveReturnedString = recursiveHelper((Map) entry.getValue());
                json.append(recursiveReturnedString);
            }else {
                json.append(entry.getValue());
            }
        }

        json.append("}");
        return json.toString();
    }
}
