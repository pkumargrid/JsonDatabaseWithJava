package create.database.Database;

import create.database.constants.Status;
import create.database.exceptions.StatusException;

import java.util.Arrays;

public class DataProvider {
    private final String[] dataStore;
    private final int capacity;

    public DataProvider(int capacity) {
        dataStore = new String[capacity];
        this.capacity = capacity;
    }

    public String getData(int idx) throws StatusException {
        statusChecker(idx);
        if(dataStore[idx].isEmpty()){
            throw new StatusException(Status.ERROR);
        }
        return dataStore[idx];
    }

    public void setData(int idx, String val) throws StatusException {
        statusChecker(idx);
        dataStore[idx] = val;
    }

    public void deleteData(int idx) throws StatusException {
        statusChecker(idx);
        dataStore[idx] = "";
    }

    public void init() {
        Arrays.fill(dataStore, "");
    }

    public void statusChecker(int idx) throws StatusException {
        if(idx < 0 || idx >= capacity){
            throw new StatusException(Status.ERROR);
        }
    }

}
