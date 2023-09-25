package JStream;

import Reflection.JsonProperty;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JSONObject {
    private Map<String, Object> data;

    public JSONObject() {
        data = new HashMap<>();
    }

    public JSONObject(String jsonString) {
        jsonString = jsonString.replace("{", "");
        jsonString = jsonString.replace("}", "");

        List<String> pairFields = List.of(jsonString.split(","));
        data = new HashMap<>();
        for (String kvPair : pairFields) {
            int firstIndexOfDoubleQuote = kvPair.indexOf("\"");
            int secondIndexOfDoubleQuote = kvPair.indexOf("\"", firstIndexOfDoubleQuote + 1);
            String key = kvPair.substring(firstIndexOfDoubleQuote + 1, secondIndexOfDoubleQuote);
            int indexOfDoubleQuoteOfValue = kvPair.indexOf("\"", secondIndexOfDoubleQuote + 1);
            String value = null;
            if (indexOfDoubleQuoteOfValue != -1) { //value of string type
                int firstIndexOfDoubleQuoteForValue = indexOfDoubleQuoteOfValue;
                int secondIndexOfDoubleQuoteForValue = kvPair.indexOf("\"", firstIndexOfDoubleQuoteForValue + 1);
                value = kvPair.substring(firstIndexOfDoubleQuoteForValue + 1, secondIndexOfDoubleQuoteForValue);
            } else {
                //handle value of object type
            }
            data.put(key, value);
        }
    }

    public JSONObject toJSONObject(Object instance) throws IllegalAccessException {
        Class<?> clazz = instance.getClass();
        for (var field : clazz.getDeclaredFields()) {
            var annotation = field.getAnnotation(JsonProperty.class);
            field.setAccessible(true);
            Object value = field.get(instance);

            String name = "";
            if (annotation != null) {
                name = annotation.name();
                put(name, value);
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

    public boolean has(String key) {
        return data.containsKey(key);
    }


    public <T> T fromJson(Class<T> clazz) {
        try {
            Constructor<T> ctor = clazz.getDeclaredConstructor();
            ctor.setAccessible(true);
            T instance = ctor.newInstance();
            return parseJson(instance);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            System.out.println(e);
        }

        return null;
    }

    private <T> T parseJson(T instance) {
        for (var field : instance.getClass().getDeclaredFields()) {
            var annotation = field.getAnnotation(JsonProperty.class);

            var name = "";
            Object value = null;
            if (annotation != null) {
                name = annotation.name();
                if (data.containsKey(name)) {
                    value = data.get(name);
                }
            }

            if(name.isBlank() || value == null){
                name = field.getName();
                if(data.containsKey(name)){
                    value = data.get(name);
                }
            }

            if(name.isBlank() || value == null){
                throw new RuntimeException("Value for field "+field.getName()+" not found");
            }

            field.setAccessible(true);
            try {
                field.set(instance,value);
            }catch (IllegalAccessException e){
                throw new RuntimeException(e);
            }

        }
        return instance;
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

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            result.append(entry.getKey()).append(" : ").append(entry.getValue());
            result.append("\n");
        }
        return result.toString();
    }
}
