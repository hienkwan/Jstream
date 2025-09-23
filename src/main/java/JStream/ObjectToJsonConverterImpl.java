package JStream;

import Reflection.JsonAttribute;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class ObjectToJsonConverterImpl implements ObjectToJsonConverter {
    private static final Logger logger = LogManager.getLogger(ObjectToJsonConverterImpl.class);
    private JsonData jsonData;

    public ObjectToJsonConverterImpl(JsonData jsonData) {
        this.jsonData = jsonData;
    }

    @Override
    public JSONObject convert(Object instance){
        return toJSONObject(instance);
    }

    public JSONObject toJSONObject(Object instance){
        if (instance instanceof Map obj) {
            jsonData.setData(obj);
        } else {
            processObjectFields(instance);
        }

        return new JSONObjectBuilder()
                .withData(jsonData.getData())
                .build();
    }

    private void processObjectFields(Object instance) {
        try {
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
        } catch (IllegalAccessException e) {
            logger.error("Cannot serialize object", e);
        }
    }

    private Map<String, Object> recursiveTrace(Object instance) throws IllegalAccessException {
        if (instance == null) {
            return null;
        }
        Map<String, Object> valueReturned = new HashMap<>();

        try {
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
        } catch (IllegalAccessException e) {
            logger.error(e);
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
