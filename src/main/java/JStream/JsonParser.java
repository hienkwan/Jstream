package JStream;

import java.util.Map;

public interface JsonParser {
    Map<String, Object> parse(String jsonString) throws Exception;
    <T> T fromJson(Class<T> clazz);
}
