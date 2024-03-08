package store.json.objects.in.database.middleware;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import org.junit.jupiter.api.Test;
import store.json.objects.in.database.database.DataHandler;
import store.json.objects.in.database.exceptions.StatusException;


import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class MiddleWareTest {

    @Test
    void deleteException() {
        DataHandler dataHandler = new DataHandler();
        JsonElement key = new JsonPrimitive("name");
        assertThrows(StatusException.class, () -> MiddleWare.delete(key, dataHandler));

        JsonArray jsonArray = new JsonArray();
        jsonArray.add("person");
        jsonArray.add("name");
        assertThrows(StatusException.class, () -> MiddleWare.delete(jsonArray, dataHandler));

        JsonArray jsonArray1 = new JsonArray();
        jsonArray1.add("person");
        jsonArray1.add("name");
        List<Integer> arr = Arrays.asList(1,2,3);
        Map<String, Object> map = new HashMap<>();
        map.put("person", arr);
        dataHandler.setMap(map);
        assertThrows(StatusException.class, () -> MiddleWare.delete(jsonArray1, dataHandler));
    }

    @Test
    void getException() {
        DataHandler dataHandler = new DataHandler();
        JsonElement key = new JsonPrimitive("name");
        assertThrows(StatusException.class, () -> MiddleWare.get(key, dataHandler));

        JsonArray jsonArray = new JsonArray();
        jsonArray.add("person");
        jsonArray.add("name");
        assertThrows(StatusException.class, () -> MiddleWare.get(jsonArray, dataHandler));

        JsonArray jsonArray1 = new JsonArray();
        jsonArray1.add("person");
        jsonArray1.add("name");
        List<Integer> arr = Arrays.asList(1,2,3);
        Map<String, Object> map = new HashMap<>();
        map.put("person", arr);
        dataHandler.setMap(map);
        assertThrows(StatusException.class, () -> MiddleWare.get(jsonArray1, dataHandler));

        map = new HashMap<>();
        Map<String, Object> newmap = new HashMap<>();
        newmap.put("firstName","pratik");
        map.put("person", newmap);
        JsonArray jsonArray2 = new JsonArray();
        jsonArray2.add("person");
        jsonArray2.add("name");
        dataHandler.setMap(map);
        assertThrows(StatusException.class, () -> MiddleWare.get(jsonArray2, dataHandler));
    }

    @Test
    void set() {
        DataHandler dataHandler = new DataHandler();
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> newmap = new HashMap<>();
        newmap.put("firstName","pratik");
        map.put("name", newmap);
        dataHandler.setMap(map);

        JsonArray jsonArray = new JsonArray();
        jsonArray.add("name");
        jsonArray.add("firstName");
        JsonElement value = new JsonPrimitive("pratik");
        MiddleWare.set(jsonArray,value, dataHandler);
        Map<String, Object> checkMap = (Map<String, Object>) dataHandler.getMap().get("name");
        assertEquals((String)checkMap.get("firstName"), "pratik");
    }
}