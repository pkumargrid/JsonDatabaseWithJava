package manage.multiple.requests.server;


import manage.multiple.requests.client.Request;
import manage.multiple.requests.database.DataHandler;
import manage.multiple.requests.database.FileRead;
import manage.multiple.requests.database.FileWriter;
import manage.multiple.requests.exceptions.StatusException;
import work.with.json.constants.UserQuery;

import java.io.IOException;
import java.util.concurrent.locks.Lock;

public class ClientRequestHandler implements Runnable {

    private final DataHandler dataHandler;
    private final Request request;

    private final FileRead fileRead;

    private final FileWriter fileWriter;

    private String result;

    private final Lock readLock;
    private final Lock writeLock;

    ClientRequestHandler(DataHandler dataHandler, Request request, Lock readLock, Lock writeLock){
        this.dataHandler = dataHandler;
        this.request = request;
        fileRead = new FileRead();
        fileWriter = new FileWriter();
        this.readLock = readLock;
        this.writeLock = writeLock;
    }

    public void queryResolver() throws StatusException, IOException {
        String type = request.type.toUpperCase();
        UserQuery query = UserQuery.valueOf(type);
        result = switch (query) {
            case GET -> typeGet(request.key);
            case SET -> {
                typeSet(request.key, request.value);
                yield "OK";
            }
            case DELETE -> {
                typeDelete(request.key);
                yield "OK";
            }
            case EXIT -> "OK";
        };
    }

    private void typeDelete(String key) throws StatusException, IOException {
        try{
            writeLock.lock();
            dataHandler.deleteData(key);
            fileWriter.write(dataHandler);
        }finally {
            writeLock.unlock();
        }
    }

    private void typeSet(String key, String value) {
        try{
            writeLock.lock();
            dataHandler.setData(key, value);
            fileWriter.write(dataHandler);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            writeLock.unlock();
        }
    }

    private String typeGet(String key) throws StatusException {
        try{
            readLock.lock();
            return dataHandler.getData(key);
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public void run() {
        try {
            try{
                readLock.lock();
                fileRead.read(dataHandler);
            }
            finally {
                readLock.unlock();
            }
            queryResolver();
        } catch (StatusException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getResult() {
        return result;
    }
}