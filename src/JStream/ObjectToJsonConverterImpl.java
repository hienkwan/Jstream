package JStream;

import Reflection.JsonAttribute;

import java.util.HashMap;
import java.util.Map;

public class ObjectToJsonConverterImpl implements ObjectToJsonConverter{

    private JsonData jsonData;
    public ObjectToJsonConverterImpl(JsonData jsonData) {
        this.jsonData = jsonData;
    }

    @Override
    public JSONObject convert(Object instance) throws IllegalAccessException {
        return toJSONObject(instance);
    }

    public JSONObject toJSONObject(Object instance) throws IllegalAccessException {
        Class<?> clazz = instance.getClass();
        for (var field : clazz.getDeclaredFields()) {
            var annotation = field.getAnnotation(JsonAttribute.class);
            field.setAccessible(true);
            Object value = field.get(instance);

            String name = (annotation != null) ? annotation.name() : field.getName();

            if (!isWrapperType(field.getType())) {
                jsonData.put(name, recursiveTrace(value));
            } else {
                jsonData.put(name, value);
            }
        }
        return new JSONObject(jsonData.getData());
    }

    private Map<String, Object> recursiveTrace(Object instance) throws IllegalAccessException {
        Map<String, Object> valueReturned = new HashMap<>();
        Class<?> clazz = instance.getClass();

        for (var field : clazz.getDeclaredFields()) {
            var annotation = field.getAnnotation(JsonAttribute.class);
            field.setAccessible(true);
            Object value = field.get(instance);

            String name = (annotation != null) ? annotation.name() : field.getName();

            if (!isWrapperType(field.getType())) {
                valueReturned.put(name, recursiveTrace(value));
            } else {
                valueReturned.put(name, value);
            }
        }
        return valueReturned;
    }

    private static boolean isWrapperType(Class<?> clazz) {
        return clazz == Boolean.class ||
                clazz == Character.class ||
                clazz == Byte.class ||
                clazz == Short.class ||
                clazz == Integer.class ||
                clazz == Long.class ||
                clazz == Float.class ||
                clazz == Double.class ||
                clazz == String.class;
    }
}
