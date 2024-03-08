package store.json.objects.in.database.server;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import store.json.objects.in.database.client.Request;
import store.json.objects.in.database.constants.UserQuery;
import store.json.objects.in.database.database.DataHandler;
import store.json.objects.in.database.database.FileRead;
import store.json.objects.in.database.database.FileWriter;
import store.json.objects.in.database.exceptions.StatusException;
import store.json.objects.in.database.middleware.MiddleWare;

import java.io.IOException;
import java.util.concurrent.locks.Lock;

public class ClientRequestHandler implements Runnable {

    private final DataHandler dataHandler;
    private final Request request;

    private FileRead fileRead;

    private FileWriter fileWriter;

    private JsonElement result;

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
        this.result = switch (query) {
            case GET -> typeGet(request.key);
            case SET -> {
                typeSet(request.key, request.value);
                yield new JsonPrimitive("OK");
            }
            case DELETE -> {
                typeDelete(request.key);
                yield new JsonPrimitive("OK");
            }
            case EXIT -> new JsonPrimitive("OK");
        };
    }

    private void typeDelete(JsonElement key) throws StatusException, IOException {
        try{
            writeLock.lock();
            MiddleWare.delete(key, dataHandler);
            fileWriter.write(dataHandler);
        }finally {
            writeLock.unlock();
        }
    }

    private void typeSet(JsonElement key, JsonElement value) {
        try{
            writeLock.lock();
            MiddleWare.set(key, value, dataHandler);
            fileWriter.write(dataHandler);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            writeLock.unlock();
        }
    }

    private JsonElement typeGet(JsonElement key) throws StatusException {
        try{
            readLock.lock();
            return MiddleWare.get(key, dataHandler);
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

    public JsonElement getResult() {
        return result;
    }

    public void setFileRead(FileRead fileRead) {
        this.fileRead = fileRead;
    }

    public void setFileWriter(FileWriter fileWriter) {
        this.fileWriter = fileWriter;
    }
}