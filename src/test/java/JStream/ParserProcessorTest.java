package JStream;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;


class ParserProcessorTest {

    @Test
    void testWithNestedObject() throws IllegalAccessException {
        // Arrange
        Address address = new Address("123 Mock St","Mock City");

        Person person = new Person("Johnny",address);

        JStream jsonSerializer = new JStream();

        // Act
        String jsonString = jsonSerializer.toJson(person);

        // Assert
        String expectedJson = "{\"address\":{\"city\":\"Mock City\",\"street\":\"123 Mock St\"},\"name\":\"Johnny\"}";
        assertEquals(expectedJson,jsonString);
    }

    @Test
    void testWithNullValueInNestedObject() throws IllegalAccessException {
        // Arrange
        Address address = new Address("123 Mock St",null);

        Person person = new Person(null,address);

        JStream jsonSerializer = new JStream();

        // Act
        String jsonString = jsonSerializer.toJson(person);

        // Assert
        String expectedJson = "{\"address\":{\"city\":null,\"street\":\"123 Mock St\"},\"name\":null}";
        assertEquals(expectedJson,jsonString);
    }

    @Test
    void testWithEmptyStringInNestedObject() throws IllegalAccessException {
        // Arrange
        Address address = new Address("123 Mock St","");

        Person person = new Person("",address);

        JStream jsonSerializer = new JStream();

        // Act
        String jsonString = jsonSerializer.toJson(person);

        // Assert
        String expectedJson = "{\"address\":{\"city\":\"\",\"street\":\"123 Mock St\"},\"name\":\"\"}";
        assertEquals(expectedJson,jsonString);
    }

    @Test
    void testWithNullNestedObject() throws IllegalAccessException{
        // Arrange
        Person person = new Person("Johnny",null);

        JStream jsonSerializer = new JStream();

        // Act
        String jsonString = jsonSerializer.toJson(person);

        // Assert
        String expectedJson = "{\"address\":null,\"name\":\"Johnny\"}";
        assertEquals(expectedJson,jsonString);
    }

    @Test
    void testParseJsonStringToObject() throws Exception {
        // Arrange
        JStream jsonSerializer = new JStream();

        // Act
        String jsonString = "{\"address\":{\"city\":\"Mock City\",\"street\":\"123 Mock St\"},\"name\":\"Johnny\"}";
        Person parsedPerson = jsonSerializer.fromJson(jsonString,Person.class);

        // Assert
        Address address = new Address("123 Mock St","Mock City");

        Person expectedPerson = new Person("Johnny",address);
        assertEquals(expectedPerson,parsedPerson);

    }

    @Test
    void testWithMapInstance() {
        JStream jsonSerializer = new JStream();

        Map<String, Object> map = new MapBuilder()
                .with("apple", 5)
                .with("banana", 3)
                .with("orange", 8)
                .build();

        String jsonString = jsonSerializer.toJson(map);

        assertEquals("{\"banana\":3,\"orange\":8,\"apple\":5}", jsonString);
    }

    @Test
    void testWithEmptyMapInstance() {
        JStream jsonSerializer = new JStream();

        Map<String, Object> map = new MapBuilder()
                .build();

        String jsonString = jsonSerializer.toJson(map);

        assertEquals("{}", jsonString);
    }
}
