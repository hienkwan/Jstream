package JStream;

import java.util.Map;

public interface JsonParser {
    Map<String, Object> parse(String jsonString);
    <T> T fromJson(Class<T> clazz);
}
