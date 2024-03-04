package functionalities.database;


import functionalities.constants.Status;
import functionalities.exceptions.StatusException;

import java.util.Arrays;

public class DataHandler {

    private final String[] dataStore;
    private final int capacity;

    public DataHandler(int capacity){
        this.dataStore = new String[capacity];
        this.capacity = capacity;
    }

    public void statusChecker(int index) throws StatusException {
        if(index < 0 || index >= capacity){
            throw new StatusException(Status.ERROR);
        }
    }

    public void init(){
        Arrays.fill(dataStore,"");
    }

    public String getData(int index) throws StatusException {
        statusChecker(index);
        if(dataStore[index].isEmpty()) throw new StatusException(Status.ERROR);
        return dataStore[index];
    }

    public void setData(int index, String val) throws StatusException {
        statusChecker(index);
        dataStore[index] = val;
    }

    public void deleteData(int index) throws StatusException {
        statusChecker(index);
        dataStore[index] = "";
    }
}
