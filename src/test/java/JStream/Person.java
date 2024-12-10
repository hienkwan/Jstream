package JStream;

import java.util.Objects;

public class Person {
    private String name;
    private Address address;

    // Constructors, getters, and setters
    public Person(String name, Address address) {
        this.name = name;
        this.address = address;
    }

    public Person() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", address=" + address +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof Person)) {
            return false;
        }

        Person c = (Person) o;

        return c.getAddress().equals(this.getAddress())
                && c.getName().equals(this.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, address);
    }
}
