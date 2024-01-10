package JStream;


import Reflection.JsonAttribute;

public class JStream {

    private JSONObject jsonObject;

    public JStream() {
        jsonObject = new JSONObjectBuilder().build();
    }

    public <T> T fromJson(String jsonString,Class<?> clazz){
        jsonObject = new JSONObject(jsonString);

        return (T) jsonObject.fromJson(clazz);
    }

    public  String toJson(Object instance) throws IllegalAccessException {
        jsonObject = new JSONObjectBuilder()
                .withInstance(instance)
                .build();

        return jsonObject.toJsonString();
    }

    private static <T> T parseJson(JSONObject json,T instance){
        for(var field : instance.getClass().getDeclaredFields()){
            var annotation = field.getAnnotation(JsonAttribute.class);

            var name = "";
            Object value = null;
            if(annotation != null){
                name = annotation.name();
                if(json.has(name)){
                    value = json.get(name);
                }
            }

            if(name.isBlank() || value == null){
                name = field.getName();
                if(json.has(name)){
                    value = json.get(name);
                }
            }

            if(name.isBlank() || value == null){
                throw new RuntimeException("Value for field" + field.getName() + "not found");
            }

            field.setAccessible(true);
            try{
                field.set(instance,value);
            }catch (IllegalAccessException e){
                throw new RuntimeException(e);
            }
        }
        return instance;
    }
}