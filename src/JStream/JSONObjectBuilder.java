package JStream;

import java.util.HashMap;
import java.util.Map;

public class JSONObjectBuilder {
    private Map<String, Object> data;

    public JSONObjectBuilder() {
        data = new HashMap<>();
    }

    public JSONObjectBuilder add(String key, Object value) {
        data.put(key, value);
        return this;
    }

    public JSONObject build() {
        return new JSONObject(data);
    }
}
