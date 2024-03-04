package manage.multiple.requests.server;


import manage.multiple.requests.client.Request;
import manage.multiple.requests.constants.Server;
import manage.multiple.requests.constants.Status;
import manage.multiple.requests.converter.JsonObjectMapper;
import manage.multiple.requests.converter.ObjectJsonMapper;
import manage.multiple.requests.database.DataHandler;
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

import static java.lang.Integer.parseInt;

public class JavaServer {
    private static final ReadWriteLock locker = new ReentrantReadWriteLock();

    public static void connect(){
        ExecutorService executorService = Executors.newCachedThreadPool();
        DataHandler dataHandler = new DataHandler();
        Lock readLock = locker.readLock();
        Lock writeLock = locker.writeLock();
        System.out.println("Server started!");
        try (ServerSocket server = new ServerSocket(parseInt(Server.PORT.value))) {
            while(true){
                try (
                        Socket socket = server.accept(); // accept a new client
                        DataInputStream input = new DataInputStream(socket.getInputStream());
                        DataOutputStream output = new DataOutputStream(socket.getOutputStream())
                ) {
                    try{
                        String messageFromClient = input.readUTF(); // read a message from the client
                        messageFromClient = messageFromClient.substring(messageFromClient.indexOf(":") + 2);
                        Request request = (Request) JsonObjectMapper.mapToObj(messageFromClient, Request.class);
                        ClientRequestHandler clientRequestHandler =
                                new ClientRequestHandler(dataHandler, request, readLock, writeLock);
                        Future<?> future = executorService.submit(clientRequestHandler);
                        try {
                            future.get(); // This will block until the task completes or throws an exception
                        } catch (ExecutionException e) {
                            if(!e.getMessage().contains("StatusException"))
                                throw new Exception(e);
                            else{
                                Response response = new Response(Status.ERROR.name(), null, "No such key");
                                output.writeUTF(ObjectJsonMapper.mapToJson(response));
                                continue;
                            }
                        }
                        String result = clientRequestHandler.getResult();
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
        }finally {
            executorService.shutdown();
        }
    }
}

