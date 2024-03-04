package store.json.objects.in.database.converter;

import com.google.gson.Gson;

public class ObjectJsonMapper {

    public static String mapToJson(Object object){
        Gson gson = new Gson();
        return gson.toJson(object);
    }
}
