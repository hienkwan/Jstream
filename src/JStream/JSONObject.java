package JStream;

import Reflection.JsonProperty;

import java.util.HashMap;
import java.util.Map;

public class JSONObject{
    private Map<String, Object> data;

    public JSONObject() {
        data = new HashMap<>();
    }

    public JSONObject(String jsonString){

    }

    public JSONObject toJSONObject(Object instance) throws IllegalAccessException {
        Class<?> tClass = instance.getClass();
        for(var field : tClass.getDeclaredFields()){
            var annotation = field.getAnnotation(JsonProperty.class);
            field.setAccessible(true);
            Object value = field.get(instance);

            String name = "";
            if(annotation!=null){
                name = annotation.name();
                put(name,value);
            }
        }
        return this;
    }

    public void put(String key, Object value) {
        data.put(key, value);
    }

    public Object get(String key) {
        return data.get(key);
    }

    public boolean has(String key){
        return data.containsKey(key);
    }

    public String fromJsonString(String str){
        return "";
    }

    public String toJsonString() {
        StringBuilder json = new StringBuilder("{");
        boolean first = true;

        for (Map.Entry<String, Object> entry : data.entrySet()) {
            if (!first) {
                json.append(",");
            }
            json.append("\"").append(entry.getKey()).append("\":");

            if (entry.getValue() instanceof String) {
                json.append("\"").append(entry.getValue()).append("\"");
            } else {
                json.append(entry.getValue());
            }

            first = false;
        }

        json.append("}");
        return json.toString();
    }

}
