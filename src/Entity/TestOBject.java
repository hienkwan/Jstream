package Entity;
import Reflection.JsonProperty;

public class TestOBject {
    @JsonProperty(name = "user_name")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "TestOBject{" +
                "name='" + name + '\'' +
                '}';
    }
}
