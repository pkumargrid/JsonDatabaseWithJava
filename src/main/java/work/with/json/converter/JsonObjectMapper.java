package work.with.json.converter;

import com.google.gson.Gson;

public class JsonObjectMapper {

    public static Object mapToObj(String json, Class<?> clazz){
        Gson gson = new Gson();
        return gson.fromJson(json, clazz);
    }

}
