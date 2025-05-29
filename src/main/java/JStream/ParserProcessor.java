package JStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

import Exception.InvalidJsonStringException;

public class ParserProcessor {
    private static final Logger logger = LogManager.getLogger(ParserProcessor.class);

    public static Integer index;

    public static Map<String, Object> parse(String jsonStr) throws Exception {
        index = 0;
        return parseObject(jsonStr);
    }

    public static Map<String, Object> parseObject(String jsonStr) throws Exception {
        Map<String, Object> map = new HashMap<>();
        boolean isEndOfNestedObject = false;

        if (jsonStr.charAt(index) == '{') {
            logger.info("'{' found,  beginning of the object");
            index++;
        }
        skipWhiteSpace(jsonStr);

        while (jsonStr.charAt(index) != '}' || index != jsonStr.length()-1) {
            String key = "";
            while(jsonStr.charAt(index) == '}' || jsonStr.charAt(index) == ','){
                index++;
                isEndOfNestedObject = true;
            }

            if(isEndOfNestedObject){
                return map;
            }

            if (jsonStr.charAt(index) == '"') {
                key = parseKey(jsonStr);
            }
            skipWhiteSpace(jsonStr);

            if (jsonStr.charAt(index) == ':') {
                index++;
            }
            skipWhiteSpace(jsonStr);
            Object value = parseValue(jsonStr);

            map.put(key, value);
            skipWhiteSpace(jsonStr);

            if(index == jsonStr.length()){
                break;
            }

            if (jsonStr.charAt(index) == ',') {
                index++;
                skipWhiteSpace(jsonStr);
            }
        }

        return map;
    }

    public static String parseKey(String json) {
        return parseString(json);
    }

    public static Object parseValue(String json) throws Exception {
        char currentChar = json.charAt(index);

        if (currentChar == '{') {
            return parseObject(json);
        } else if (currentChar == '[') {
            return parseArray(json);
        } else if (currentChar == '"') {
            return parseString(json);
        } else if (Character.isDigit(currentChar) || currentChar == '-') {
            return parseNumber(json);
        } else if (json.startsWith("true", index)) {
            return true;
        } else if (json.startsWith("false", index)) {
            return false;
        } else if (json.startsWith("null", index)) {
            return null;
        } else {
            throw new InvalidJsonStringException("Unexpected value at position: " + index);
        }
    }

    private static String parseString(String json) {
        StringBuilder sb = new StringBuilder();
        if (json.charAt(index) == '"') {
            index++;
        }

        while (json.charAt(index) != '"') {
            sb.append(json.charAt(index));
            index++;
        }
        index++;

        return sb.toString();
    }

    private static Number parseNumber(String json) {
        StringBuilder sb = new StringBuilder();
        boolean isFloatingPoint = false;

        if (json.charAt(index) == '-') {
            sb.append(json.charAt(index));
            index++;
        }

        while (Character.isDigit(json.charAt(index)) || json.charAt(index) == '.') {
            if (json.charAt(index) == '.') {
                isFloatingPoint = true;
            }

            sb.append(json.charAt(index));
            index++;
        }
        index++;

        if (isFloatingPoint) {
            return Float.parseFloat(sb.toString().trim());
        } else {
            return Integer.parseInt(sb.toString().trim());
        }
    }

    private static List<Object> parseArray(String json) throws Exception {
        List<Object> list = new ArrayList<>();
        index++; // skipping '['

        while (json.charAt(index) != ']') {
            skipWhiteSpace(json);
            Object value = parseValue(json);
            list.add(value);

            if (json.charAt(index) == ',') {
                index++;
            }
        }
        index++; // skipping ']'

        return list;
    }

    private static void skipWhiteSpace(String str) {
        if (index >= str.length()) {
            return;
        }

        while (Character.isWhitespace(str.charAt(index))) {
            index++;
        }
    }

    public static void main(String[] args) throws Exception {
//        var test =
//                parse("{\n" +
//                        "  \"name\": \"John Doe\",\n" +
//                        "  \"age\": \"test\"\n" +
//                        "  \"test\": -100.5\n" +
//                        "  \"array\": [300,200,\"string\"]\n" +
//                        "}");
        var test =  parse("{\"user_name\":\"abc\",\"nestedObj\":{\"property2\":\"sde\",\"property1\":\"cfe\"},\"age\":10}");

        System.out.println(test);
    }

}
