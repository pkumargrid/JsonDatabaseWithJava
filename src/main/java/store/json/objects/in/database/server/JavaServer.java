package store.json.objects.in.database.server;


import com.google.gson.JsonElement;
import store.json.objects.in.database.client.Request;
import store.json.objects.in.database.constants.Status;
import store.json.objects.in.database.converter.JsonObjectMapper;
import store.json.objects.in.database.converter.ObjectJsonMapper;
import store.json.objects.in.database.database.DataHandler;
import store.json.objects.in.database.database.FileRead;
import store.json.objects.in.database.database.FileWriter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class JavaServer {
    private static final Object lock = new Object();
    private static final ReadWriteLock locker = new ReentrantReadWriteLock();
    public static void connect(DataHandler dataHandler, FileRead fileRead, FileWriter fileWriter, int port){
        ExecutorService executorService = Executors.newCachedThreadPool();
        Lock readLock = locker.readLock();
        Lock writeLock = locker.writeLock();
        System.out.println("Server started!");
        try (ServerSocket server = new ServerSocket(port)) {
            while(true){
                try (
                        Socket socket = server.accept(); // accept a new client
                        DataInputStream input = new DataInputStream(socket.getInputStream());
                        DataOutputStream output = new DataOutputStream(socket.getOutputStream())
                ) {
                    try{
                        String messageFromClient = input.readUTF(); // read a message from the client
                        Request request = (Request) JsonObjectMapper.mapToObj(messageFromClient, Request.class);
                        ClientRequestHandler clientRequestHandler =
                                new ClientRequestHandler(dataHandler, request, readLock, writeLock);
                        clientRequestHandler.setFileWriter(fileWriter);
                        clientRequestHandler.setFileRead(fileRead);
                        Future<?> future = executorService.submit(clientRequestHandler);
                        try {
                            future.get(); // This will block until the task completes or throws an exception
                        } catch (ExecutionException e) {
                            if(!e.getMessage().contains("StatusException"))
                                throw new Exception(e);
                            else{
                                throw new RuntimeException(e);
                            }
                        }
                        JsonElement result = clientRequestHandler.getResult();
                        Response response = Response.generator(request, result);
                        output.writeUTF(ObjectJsonMapper.mapToJson(response));
                        if(request.type.equals("exit")){
                            break;
                        }
                    } catch (RuntimeException e){
                        Response response = new Response(Status.ERROR.name(), null, "No such key");
                        output.writeUTF(ObjectJsonMapper.mapToJson(response));
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
        finally {
            executorService.shutdown();
        }
    }
}

