package functionalities.server;


import functionalities.constants.Server;
import functionalities.exceptions.StatusException;

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
                        output.writeUTF(ClientRequestHandler.queryResolver(messageFromClient));
                        if(messageFromClient.equals("exit")) break;
                    } catch (StatusException statusException){
                        output.writeUTF(statusException.getMessage());
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
    }
}

