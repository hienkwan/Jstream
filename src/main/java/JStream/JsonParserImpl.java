package JStream;

import Reflection.JsonAttribute;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

public class JsonParserImpl implements JsonParser {
    private JsonData jsonData;

    public JsonParserImpl(JsonData jsonData) {
        this.jsonData = jsonData;
    }

    @Override
    public Map<String, Object> parse(String jsonString) throws Exception {
        Map<String, Object> data;

        try {
            data = ParserProcessor.parse(jsonString);
        } catch (Exception ex) {
            throw ex;
        }

        return data;
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

    //TODO adjust to parse data from Map to actual object
    //helper method to parse from json to object
    private <T> T parseJson(T instance) {
        for (var field : instance.getClass().getDeclaredFields()) {
            var annotation = field.getAnnotation(JsonAttribute.class);

            var name = "";
            Object value = null;
            if (annotation != null) {
                name = annotation.name();
                if (jsonData.has(name)) {
                    value = jsonData.get(name);
                }
            }

            if (name.isBlank() || value == null) {
                name = field.getName();
                if (jsonData.has(name)) {
                    value = jsonData.get(name);
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
        } else if (fieldType == List.class) {
            parsedValue = (T) value;
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

        // Enable access to private fields
        field.setAccessible(true);

        // Cast the parsed value to the field's type and set it
        try {
            field.set(object, parsedValue);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
