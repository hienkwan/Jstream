package Entity;
import Reflection.JsonAttribute;

public class TestOBject {
    @JsonAttribute(name = "user_name")
    private String name;

    private Integer age;

    private NestedObj nestedObj;

    public TestOBject() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public NestedObj getNestedObj() {
        return nestedObj;
    }

    public void setNestedObj(NestedObj nestedObj) {
        this.nestedObj = nestedObj;
    }

    @Override
    public String toString() {
        return "TestOBject{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", nestedObj=" + nestedObj +
                '}';
    }
}
