package store.json.objects.in.database.server;

import com.google.gson.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import store.json.objects.in.database.client.Request;
import store.json.objects.in.database.converter.ObjectJsonMapper;
import store.json.objects.in.database.database.DataHandler;
import store.json.objects.in.database.database.FileRead;
import store.json.objects.in.database.database.FileWriter;
import store.json.objects.in.database.exceptions.StatusException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientRequestHandlerTest {

    @Mock
    private Lock readLock;

    @Mock
    private Lock writeLock;

    @Mock
    private FileWriter fileWriter;

    @Mock
    private FileRead fileRead;


    @Test
    void queryResolverGet() throws StatusException, IOException {
        Request request = new Request("get",null, null, null);
        doNothing().when(readLock).lock();
        doNothing().when(readLock).unlock();
        DataHandler dataHandler = new DataHandler();
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> name = new HashMap<>();
        name.put("firstName", "pratik");
        name.put("lastName", "verma");
        Map<String, Object> person = new HashMap<>();
        person.put("name", name);
        map.put("person", person);
        dataHandler.setMap(map);
        JsonArray jsonArray = new JsonArray();
        jsonArray.add("person");
        jsonArray.add("name");
        jsonArray.add("lastName");
        request.key = jsonArray;
        ClientRequestHandler client = new ClientRequestHandler(dataHandler, request, readLock, writeLock);
        client.queryResolver();
        JsonElement jsonElement = new JsonPrimitive("verma");
        assertEquals(jsonElement, client.getResult());
    }

    @Test
    void queryResolverSet() throws StatusException, IOException {
        Map<String, Object> input = new HashMap<>();
        input.put("city","New York");
        input.put("zip","10001");
        JsonArray jsonArray = new JsonArray();
        jsonArray.add("person");
        jsonArray.add("address");
        Gson gson = new Gson();
        Request request = new Request("set", jsonArray, gson.toJsonTree(input), null);
        doNothing().when(writeLock).lock();
        doNothing().when(writeLock).unlock();
        DataHandler dataHandler = new DataHandler();
        doNothing().when(fileWriter).write(dataHandler);
        ClientRequestHandler client = new ClientRequestHandler(dataHandler, request, readLock, writeLock);
        client.setFileWriter(fileWriter);
        client.queryResolver();
        Map<String, Object> map = dataHandler.getMap();
        JsonElement jsonElement = new JsonPrimitive("{\"person\":{\"address\":{\"zip\":\"10001\",\"city\":\"New York\"}}}");
        assertEquals(jsonElement.getAsString(), ObjectJsonMapper.mapToJson(map));
    }

    @Test
    void queryResolverSet1() throws StatusException, IOException {
        Map<String, Object> input = new HashMap<>();
        input.put("city","New York");
        input.put("zip","10001");
        JsonElement key = new JsonPrimitive("person");
        Gson gson = new Gson();
        Request request = new Request("set", key, gson.toJsonTree(input), null);
        doNothing().when(writeLock).lock();
        doNothing().when(writeLock).unlock();
        DataHandler dataHandler = new DataHandler();
        doNothing().when(fileWriter).write(dataHandler);
        ClientRequestHandler client = new ClientRequestHandler(dataHandler, request, readLock, writeLock);
        client.setFileWriter(fileWriter);
        client.queryResolver();
        Map<String, Object> map = dataHandler.getMap();
        JsonElement jsonElement = new JsonPrimitive("{\"person\":{\"zip\":\"10001\",\"city\":\"New York\"}}");
        assertEquals(jsonElement.getAsString(), ObjectJsonMapper.mapToJson(map));
    }

    @Test
    void queryResolverDelete() throws StatusException, IOException {
        JsonArray jsonArray = new JsonArray();
        jsonArray.add("person");
        jsonArray.add("name");
        jsonArray.add("lastName");
        DataHandler dataHandler = new DataHandler();
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> name = new HashMap<>();
        name.put("firstName", "pratik");
        name.put("lastName", "verma");
        Map<String, Object> person = new HashMap<>();
        person.put("name", name);
        map.put("person", person);
        dataHandler.setMap(map);
        Request request = new Request("delete", jsonArray, null, null);
        doNothing().when(writeLock).lock();
        doNothing().when(writeLock).unlock();
        doNothing().when(fileWriter).write(dataHandler);
        ClientRequestHandler client = new ClientRequestHandler(dataHandler, request, readLock, writeLock);
        client.setFileWriter(fileWriter);
        client.queryResolver();
        Map<String, Object> dataMap = dataHandler.getMap();
        name = (Map<String, Object>) dataMap.get("person");
        String lastName = (String)name.get("lastName");
        assertNull(lastName);
    }

    @Test
    void queryResolverExit() throws StatusException, IOException {
        DataHandler dataHandler = new DataHandler();
        Request request = new Request("exit", null, null, null);
        ClientRequestHandler client = new ClientRequestHandler(dataHandler, request, readLock, writeLock);
        client.queryResolver();
        assertEquals("OK", client.getResult().getAsString());
    }

    @Test
    void queryResolverNullPointerException() {
        doNothing().when(writeLock).lock();
        doNothing().when(writeLock).unlock();
        Request request = new Request("set", null, null, null);
        DataHandler dataHandler = new DataHandler();
        ClientRequestHandler client = new ClientRequestHandler(dataHandler, request, readLock, writeLock);
        assertThrows(RuntimeException.class, client::queryResolver);
    }

    @Test
    void queryResolverStatusException()  {
        doNothing().when(readLock).lock();
        doNothing().when(readLock).unlock();
        DataHandler dataHandler = new DataHandler();
        Request request = new Request("get", new JsonPrimitive("address"), null, null);
        ClientRequestHandler client = new ClientRequestHandler(dataHandler, request, readLock, writeLock);
        assertThrows(StatusException.class, client::queryResolver);
    }

    @Test
    void run() throws IOException {
        doNothing().when(readLock).lock();
        doNothing().when(readLock).unlock();
        DataHandler dataHandler = new DataHandler();
        doNothing().when(fileRead).read(dataHandler);
        Request request = new Request("exit", null, null, null);
        ClientRequestHandler client = new ClientRequestHandler(dataHandler, request, readLock, writeLock);
        client.setFileRead(fileRead);
        client.run();
        assertEquals("OK", client.getResult().getAsString());
    }

    @Test
    void runException() throws IOException {
        doNothing().when(readLock).lock();
        doNothing().when(readLock).unlock();
        DataHandler dataHandler = new DataHandler();
        doNothing().when(fileRead).read(dataHandler);
        Request request = new Request("get", new JsonPrimitive("hello"), null, null);
        ClientRequestHandler client = new ClientRequestHandler(dataHandler, request, readLock, writeLock);
        client.setFileRead(fileRead);
        assertThrows(RuntimeException.class, client::run);
    }

    @Test
    void queryResolverIOException() throws IOException {
        DataHandler datahandler = new DataHandler();
        doThrow(IOException.class).when(fileWriter).write(datahandler);
        doNothing().when(writeLock).lock();
        doNothing().when(writeLock).unlock();
        Request request = new Request("set", new JsonPrimitive("profession"), new JsonPrimitive("teaching"), null);
        ClientRequestHandler client = new ClientRequestHandler(datahandler, request, readLock, writeLock);
        client.setFileWriter(fileWriter);
        assertThrows(RuntimeException.class, client::queryResolver);
    }

}