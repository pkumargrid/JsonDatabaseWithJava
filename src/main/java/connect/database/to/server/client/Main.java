package connect.database.to.server.client;

import connect.database.to.server.constants.Server;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import static java.lang.Integer.parseInt;

public class Main {

    public static void main(String[] args) {
        System.out.println("Client started!");
        try (
                Socket socket = new Socket(Server.ADDRESS.value,parseInt(Server.PORT.value));
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output  = new DataOutputStream(socket.getOutputStream())
        ) {
            String msg = "Give me a record # 12";
            System.out.println("Sent: " + msg);
            output.writeUTF(msg);
            String receivedMsg = input.readUTF();
            System.out.println("Received: " + receivedMsg);
        } catch (IOException | NumberFormatException e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }

}
