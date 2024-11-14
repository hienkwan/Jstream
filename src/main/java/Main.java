import Entity.NestedObj;
import Entity.TestOBject;
import JStream.JStream;

public class Main {
    public static void main(String[] args) throws Exception {
        TestOBject testOBject =new TestOBject();
        testOBject.setName("abc");
        testOBject.setAge(10);

        NestedObj nestedObj = new NestedObj("cfe","sde");
        testOBject.setNestedObj(nestedObj);

        JStream jStream = new JStream();
        //TestOBject testOBject1 = jStream.fromJson("{\"user_name\":\"abc\",\"nestedObj\":{\"property2\":\"sde\",\"property1\":\"cfe\"},\"age\":10}",TestOBject.class);
        //String result = jStream.toJson(testOBject);
        TestOBject result = jStream.fromJson("{\"user_name\":\"abc\",\"nestedObj\":{\"property2\":\"sde\",\"property1\":\"cfe\"},\"age\":10}",TestOBject.class);
        System.out.println(result);
    }



}