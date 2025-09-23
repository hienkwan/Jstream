package JStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class JStream {
    private static final Logger logger = LogManager.getLogger(JStream.class);
    private JSONObject jsonObject;

    public JStream() {
        jsonObject = new JSONObjectBuilder().build();
    }

    public <T> T fromJson(String jsonString, Class<?> clazz) {
        jsonObject = new JSONObjectBuilder()
                .withJsonString(jsonString)
                .build();

        return (T) jsonObject.fromJson(clazz);
    }

    public String toJson(Object instance) {
        jsonObject = new JSONObjectBuilder()
                .withInstance(instance)
                .build();

        return jsonObject.toJsonString();
    }
}
