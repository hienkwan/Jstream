import Entity.TestOBject;
import JStream.JSONObject;
import JStream.JStream;

public class Main {
    public static void main(String[] args) throws IllegalAccessException {
        TestOBject testOBject =new TestOBject();
        testOBject.setName("abc");

        JStream jStream = new JStream();
        TestOBject result = jStream.fromJson("{\"user_name\":\"cdf\"}",TestOBject.class);
        System.out.println(result);

    }

}