package work.with.json.server;

import work.with.json.client.Request;
import work.with.json.constants.Server;
import work.with.json.constants.Status;
import work.with.json.converter.JsonObjectMapper;
import work.with.json.converter.ObjectJsonMapper;
import work.with.json.exceptions.StatusException;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

import static java.lang.Integer.parseInt;

public class JavaServer {
    public static void connect(){
        ClientRequestHandler.setUp();
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
                        String valueToReturn = ClientRequestHandler.queryResolver(request);
                        Response response = Response.generator(request, valueToReturn);
                        output.writeUTF(ObjectJsonMapper.mapToJson(response));
                        if(request.type.equals("exit")) break;
                    } catch (NumberFormatException | StatusException statusException){
                        Response response = new Response(Status.ERROR.name(), null, "No such key");
                        output.writeUTF(ObjectJsonMapper.mapToJson(response));
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
    }
}

