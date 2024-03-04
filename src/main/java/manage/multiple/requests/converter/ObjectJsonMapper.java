package manage.multiple.requests.converter;

import com.google.gson.Gson;

public class ObjectJsonMapper {

    public static String mapToJson(Object object){
        Gson gson = new Gson();
        return gson.toJson(object);
    }
}
