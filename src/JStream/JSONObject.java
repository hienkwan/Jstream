package JStream;

import Reflection.JsonProperty;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JSONObject {
    private Map<String, Object> data;

    public JSONObject() {
        data = new HashMap<>();
    }

    public JSONObject(Map<String, Object> data) {
        this.data = data;
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
            String value = getValue(kvPair, secondIndexOfDoubleQuote);
            data.put(key, value);
        }
    }

    private static String getValue(String kvPair, int secondIndexOfDoubleQuote) {
        int indexOfDoubleQuoteOfValue = kvPair.indexOf("\"", secondIndexOfDoubleQuote + 1);
        String value = null;
        if (indexOfDoubleQuoteOfValue != -1) { //value of string type
            int firstIndexOfDoubleQuoteForValue = indexOfDoubleQuoteOfValue;
            int secondIndexOfDoubleQuoteForValue = kvPair.indexOf("\"", firstIndexOfDoubleQuoteForValue + 1);
            value = kvPair.substring(firstIndexOfDoubleQuoteForValue + 1, secondIndexOfDoubleQuoteForValue);
        } else {
            //TODO: handle value of object type
            int lastColonIndex = kvPair.lastIndexOf(":");
            value = kvPair.substring(lastColonIndex + 1);
        }
        return value;
    }

    //parse from object to JsonObject
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

            if (annotation == null) {
                name = field.getName();
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


    //parse from json to object
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

    //helper method to parse from json to object
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

            if (name.isBlank() || value == null) {
                name = field.getName();
                if (data.containsKey(name)) {
                    value = data.get(name);
                }
            }

            if (name.isBlank() || value == null) {
                throw new RuntimeException("Value for field " + field.getName() + " not found");
            }

            field.setAccessible(true);
            System.out.println(field.getType());
            setFieldValue(instance, field, value);

        }
        return instance;
    }

    private <T> void setFieldValue(Object object, Field field, Object value) {
        Class<?> fieldType = field.getType();

        T parsedValue = null;
        if (fieldType == int.class || fieldType == Integer.class) {
            parsedValue = (T) Integer.valueOf(value.toString());
        } else if (fieldType == double.class || fieldType == Double.class) {
            parsedValue = (T) Double.valueOf(value.toString());
        } else if (fieldType == boolean.class || fieldType == Boolean.class) {
            parsedValue = (T) Boolean.valueOf(value.toString());
        } else if (fieldType == String.class) {
            parsedValue = (T) value.toString();
        } else {
            try {
                Object customObject = fieldType.getDeclaredConstructor().newInstance();
                if (value instanceof Map) {
                    Map<String, Object> customObjectMap = (Map<String, Object>) value;
                    for (Field customField : fieldType.getDeclaredFields()) {
                        String fieldName = customField.getName();
                        if (customObjectMap.containsKey(fieldName)) {
                            Object fieldValue = customObjectMap.get(fieldName);
                            setFieldValue(customObject, customField, fieldValue);
                        }
                    }
                }
                parsedValue = (T) customObject;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }

        // Cast the parsed value to the field's type and set it
        field.setAccessible(true); // Enable access to private fields
        try {
            field.set(object, parsedValue);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public String toJsonString() {
        StringBuilder json = new StringBuilder();

        for (Map.Entry<String, Object> entry : data.entrySet()) {
            if (json.length() > 0) {
                json.append(",");
            } else {
                json.append("{");
            }
            json.append("\"").append(entry.getKey()).append("\":");

            if (entry.getValue() instanceof String) {
                json.append("\"").append(entry.getValue()).append("\"");
            } else {
                json.append(entry.getValue());
            }

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
