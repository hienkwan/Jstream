package JStream;

import java.util.HashMap;
import java.util.Map;

public class JSONObjectBuilder {
    private JsonData jsonData;
    private JsonParser jsonParser;
    private ObjectToJsonConverter objectToJsonConverter;

    public JSONObjectBuilder() {
        jsonData = new JsonData();
    }

    public JSONObjectBuilder withData(Map<String, Object> data) {
        jsonData.setData(data);
        return this;
    }

    public JSONObjectBuilder withInstance(Object instance) throws IllegalAccessException {
        objectToJsonConverter = new ObjectToJsonConverterImpl(jsonData);
        objectToJsonConverter.convert(instance);
        return this;
    }

    public JSONObjectBuilder withJsonString(String jsonString) throws Exception {
        jsonParser = new JsonParserImpl(jsonData);
        jsonData.setData(jsonParser.parse(jsonString));
        return this;
    }

    public JSONObject build() {
        if(jsonParser == null){
            jsonParser = new JsonParserImpl(jsonData);
        }

        if(objectToJsonConverter == null){
            objectToJsonConverter = new ObjectToJsonConverterImpl(jsonData);
        }
        return new JSONObject(jsonData,jsonParser,objectToJsonConverter);
    }
}
