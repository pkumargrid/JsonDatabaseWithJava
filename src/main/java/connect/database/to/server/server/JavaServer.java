package connect.database.to.server.server;


import connect.database.to.server.constants.Server;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static java.lang.Integer.parseInt;

public class JavaServer {
    public static void connect(){
        System.out.println("Server started!");
        try (ServerSocket server = new ServerSocket(parseInt(Server.PORT.value))) {
            try (
                    Socket socket = server.accept(); // accept a new client
                    DataInputStream input = new DataInputStream(socket.getInputStream());
                    DataOutputStream output = new DataOutputStream(socket.getOutputStream())
            ) {
                String msg = input.readUTF(); // read a message from the client
                System.out.println("Received: " + msg);
                System.out.println("Sent: " + "A record # 12 was sent!");
                output.writeUTF("A record # 12 was sent!"); // resend it to the client
            }
        } catch (NumberFormatException | IOException e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }
}

