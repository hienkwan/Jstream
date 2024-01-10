package JStream;

public interface ObjectToJsonConverter {
    JSONObject convert(Object instance) throws IllegalAccessException;
}
