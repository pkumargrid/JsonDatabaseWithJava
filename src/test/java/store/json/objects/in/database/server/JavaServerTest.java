package store.json.objects.in.database.server;

import com.google.gson.JsonPrimitive;
import store.json.objects.in.database.constants.Server;
import org.junit.jupiter.api.Test;
import store.json.objects.in.database.client.Request;
import store.json.objects.in.database.converter.ObjectJsonMapper;
import store.json.objects.in.database.database.DataHandler;
import store.json.objects.in.database.database.FileRead;
import store.json.objects.in.database.database.FileWriter;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

import static java.lang.Integer.parseInt;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JavaServerTest {

    @Test
    void connect() throws Exception {
        DataHandler dataHandler = new DataHandler();
        FileRead fileRead = mock(FileRead.class);
        FileWriter fileWriter = mock(FileWriter.class);
        doNothing().when(fileRead).read(dataHandler);
        doNothing().when(fileWriter).write(dataHandler);
        Map<String, Object> map = new HashMap<>();
        map.put("name", "pratik");
        dataHandler.setMap(map);
        Callable<String> client = () -> {
                try(Socket socket = new Socket(Server.ADDRESS.value, parseInt(Server.PORT.value));
                    DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                    DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream())) {
                    Request request = new Request("get",new JsonPrimitive("name"),null, null);
                    String json = ObjectJsonMapper.mapToJson(request);
                    dataOutputStream.writeUTF(json);
                    return dataInputStream.readUTF();
                }
            };
        Callable<Void> server = () -> {
            JavaServer.connect(dataHandler, fileRead, fileWriter, parseInt(Server.PORT.value));
            return null;
        };
        ExecutorService executor = Executors.newFixedThreadPool(2);
        executor.submit(server);
        Thread.sleep(10);
        Future<String> future = executor.submit(client);
        String result = future.get();
        executor.shutdownNow();
        assertEquals("{\"response\":\"OK\",\"value\":\"pratik\"}", result);
    }

    @Test
    void connectionERROR() throws Exception {
        DataHandler dataHandler = new DataHandler();
        FileRead fileRead = mock(FileRead.class);
        FileWriter fileWriter = mock(FileWriter.class);

        doNothing().when(fileRead).read(dataHandler);
        doNothing().when(fileWriter).write(dataHandler);

        Map<String, Object> map = new HashMap<>();
        dataHandler.setMap(map);

        Callable<String> client = () -> {
            try(Socket socket = new Socket(Server.ADDRESS.value, parseInt(Server.TEST_PORT.value));
                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream())) {
                Request request = new Request("get",new JsonPrimitive("name"),null, null);
                String json = ObjectJsonMapper.mapToJson(request);
                dataOutputStream.writeUTF(json);
                return dataInputStream.readUTF();
            }
        };
        Callable<Void> server = () -> {
            JavaServer.connect(dataHandler, fileRead, fileWriter, parseInt(Server.TEST_PORT.value));
            return null;
        };
        ExecutorService executor = Executors.newFixedThreadPool(2);
        executor.submit(server);
        Thread.sleep(100);
        Future<String> future = executor.submit(client);
        String result = future.get();
        executor.shutdownNow();
        assertEquals("{\"response\":\"ERROR\",\"reason\":\"No such key\"}", result);
    }

    @Test
    void connectionException()  {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream original = System.out;
        System.setOut(new PrintStream(outputStream));
        JavaServer.connect(null, null, null, -1);
        assertTrue(outputStream.toString().contains("Exception"));
    }

    @Test
    void testMain()  {
        Callable<Void> server = () -> {
            Main.main(new String[]{});
            return null;
        };
        ExecutorService executor = Executors.newFixedThreadPool(3);
        boolean section = false;
        Future<Void> future = executor.submit(server);
        try {
            future.get(1, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            future.cancel(true);
            section = true;
        }
        executor.shutdown();
        assertTrue(section);
    }
}