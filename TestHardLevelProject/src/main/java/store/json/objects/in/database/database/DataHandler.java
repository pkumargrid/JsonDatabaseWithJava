package store.json.objects.in.database.database;



import store.json.objects.in.database.constants.Status;
import store.json.objects.in.database.exceptions.StatusException;

import java.util.HashMap;
import java.util.Map;

public class DataHandler {

    private Map<String, Object> map;

    public DataHandler() {
        this.map = new HashMap<>();
    }

    public void statusChecker(String key) throws StatusException {
        if(!map.containsKey(key))
            throw new StatusException(Status.ERROR);
    }

    public Object getData(String key) throws StatusException {
        statusChecker(key);
        return map.get(key);
    }

    public void setData(String key, Object val) {
        map.put(key, val);
    }

    public void deleteData(String key) throws StatusException {
        statusChecker(key);
        map.remove(key);
    }

    public Map<String, Object> getMap() {
        return map;
    }

    public void setMap(Map<String, Object> map){
        this.map = map;
    }
}
