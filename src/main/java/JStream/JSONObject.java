package JStream;

import java.util.Map;

public class JSONObject {

    private JsonData jsonData;
    private JsonParser jsonParser;
    private ObjectToJsonConverter objectToJsonConverter;

    public JSONObject(JsonData jsonData,JsonParser jsonParser,ObjectToJsonConverter objectToJsonConverter) {
        this.jsonData = jsonData;
        this.jsonParser = jsonParser;
        this.objectToJsonConverter = objectToJsonConverter;
    }

    //parse from object to JsonObject
    public JSONObject toJSONObject(Object instance) throws IllegalAccessException {
        return objectToJsonConverter.convert(instance);
    }


    public Object get(String key) {
        return jsonData.get(key);
    }

    public boolean has(String key) {
        return jsonData.has(key);
    }


    //parse from json to object
    public Object fromJson(Class<?> clazz) {
        return jsonParser.fromJson(clazz);
    }


    public String toJsonString() {
       return jsonData.toJsonString();
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (Map.Entry<String, Object> entry : jsonData.getData().entrySet()) {
            result.append(entry.getKey()).append(" : ").append(entry.getValue());
            result.append("\n");
        }
        return result.toString();
    }


}
