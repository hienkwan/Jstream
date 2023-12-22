package JStream;

import java.util.Map;

public class JSONObject {

    private JsonData jsonData;
    private JsonParser jsonParser;
    private ObjectToJsonConverter objectToJsonConverter;

    public JSONObject() {
        jsonData = new JsonData();
        jsonParser = new JsonParserImpl(jsonData);
        objectToJsonConverter = new ObjectToJsonConverterImpl(jsonData);
    }

    public JSONObject(Map<String, Object> data) {
        jsonData = new JsonData();
        jsonData.setData(data);
        jsonParser = new JsonParserImpl(jsonData);
        objectToJsonConverter = new ObjectToJsonConverterImpl(jsonData);
    }

    public JSONObject(String jsonString) {
        jsonData = new JsonData();
        jsonParser = new JsonParserImpl(jsonData);
        jsonData.setData(jsonParser.parse(jsonString));
        objectToJsonConverter = new ObjectToJsonConverterImpl(jsonData);
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
