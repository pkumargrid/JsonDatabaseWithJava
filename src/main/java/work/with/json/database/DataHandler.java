package work.with.json.database;


import work.with.json.constants.Status;
import work.with.json.exceptions.StatusException;

import java.util.HashMap;
import java.util.Map;

public class DataHandler {

    private final Map<String, String> map;

    public DataHandler(){
        this.map = new HashMap<>();
    }

    public void statusChecker(String key) throws StatusException {
        if(!map.containsKey(key)) throw new StatusException(Status.ERROR);
    }

    public String getData(String key) throws StatusException {
        statusChecker(key);
        return map.get(key);
    }

    public void setData(String key, String val) {
        map.put(key, val);
    }

    public void deleteData(String key) throws StatusException {
        statusChecker(key);
        map.remove(key);
    }
}
