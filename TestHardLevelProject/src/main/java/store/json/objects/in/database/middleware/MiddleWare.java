package store.json.objects.in.database.middleware;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import store.json.objects.in.database.constants.Status;
import store.json.objects.in.database.converter.ObjectJsonMapper;
import store.json.objects.in.database.database.DataHandler;
import store.json.objects.in.database.exceptions.StatusException;

import java.util.HashMap;
import java.util.Map;

public class MiddleWare {
    public static void delete(JsonElement jsonElement, DataHandler dataHandler) throws StatusException {
        if(jsonElement.isJsonPrimitive()){
            dataHandler.deleteData(jsonElement.getAsString());
            return;
        }
        JsonArray keysArray = jsonElement.getAsJsonArray();
        Map<String, Object> dataMap = dataHandler.getMap();
        for (int i = 0; i < keysArray.size() - 1; i++) {
            String key = keysArray.get(i).getAsString();
            if (!dataMap.containsKey(key)) {
                throw new StatusException(Status.ERROR);
            }
            Object nestedObject = dataMap.get(key);
            if (!(nestedObject instanceof Map)) {
                throw new StatusException(Status.ERROR);
            }
            dataMap = (Map<String, Object>) nestedObject;
        }
        String lastKey = keysArray.get(keysArray.size() - 1).getAsString();
        dataMap.remove(lastKey);
    }

    public static JsonElement get(JsonElement jsonElement, DataHandler dataHandler) throws StatusException {
        if(jsonElement.isJsonPrimitive()){
            return new Gson().toJsonTree(dataHandler.getData(jsonElement.getAsString()));
        }
        JsonArray keysArray = jsonElement.getAsJsonArray();
        Map<String, Object> dataMap = dataHandler.getMap();
        for (int i = 0; i < keysArray.size() - 1; i++) {
            String key = keysArray.get(i).getAsString();
            if (!dataMap.containsKey(key)) {
                throw new StatusException(Status.ERROR);
            }
            Object nestedObject = dataMap.get(key);
            if (!(nestedObject instanceof Map)) {
                throw new StatusException(Status.ERROR);
            }
            dataMap = (Map<String, Object>) nestedObject;
        }
        String lastKey = keysArray.get(keysArray.size() - 1).getAsString();
        if (dataMap.containsKey(lastKey)) {
            Object value = dataMap.get(lastKey);
            return new Gson().toJsonTree(value);
        }
        throw new StatusException(Status.ERROR);
    }

    public static void set(JsonElement keyArray, JsonElement value, DataHandler dataHandler) {
        Map<String, Object> map = dataHandler.getMap();
       if(keyArray.isJsonPrimitive()){
           String key = keyArray.getAsString();
           if(value.isJsonPrimitive()){
               map.put(key, value.getAsString());
           }
           else{
               Gson gson = new Gson();
               Map<String, Object> nestedObject = gson.fromJson(value, Map.class);
               map.put(key, nestedObject);
           }
           return;
       }
       JsonArray jsonArray = keyArray.getAsJsonArray();
       for(int i = 0; i < jsonArray.size() - 1; i++){
           String key = jsonArray.get(i).getAsString();
           if(map.containsKey(key)){
               map = (Map<String, Object>) map.get(key);
           }
           else{
               Map<String, Object> nestedMap = new HashMap<>();
               dataHandler.setData(key, nestedMap);
               map = nestedMap;
           }
       }
       String lastKey = jsonArray.get(jsonArray.size() - 1).getAsString();
        if(value.isJsonPrimitive()){
           map.put(lastKey, value.getAsString());
       }
       else{
           Gson gson = new Gson();
           Map<String, Object> nestedObject = gson.fromJson(value, Map.class);
           map.put(lastKey, nestedObject);
       }
    }
}
