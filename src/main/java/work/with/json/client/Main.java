package work.with.json.client;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import work.with.json.constants.Server;
import work.with.json.converter.ObjectJsonMapper;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import static java.lang.Integer.parseInt;

public class Main {

    @Parameter(names = "-t")
    private static String type;

    @Parameter(names = "-k")
    private static String index;

    @Parameter(names = "-v")
    private static String inputMessage;

    public static void main(String[] args) {
        Main main = new Main();
        JCommander.newBuilder()
                .addObject(main)
                .build()
                .parse(args);
        System.out.println("Client started!");
        try (
                Socket socket = new Socket(Server.ADDRESS.value,parseInt(Server.PORT.value));
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output  = new DataOutputStream(socket.getOutputStream())
        ) {
            Request request = new Request(type, index, inputMessage);
            String json = ObjectJsonMapper.mapToJson(request);
            System.out.println("Sent: " + json);
            output.writeUTF("Sent: " + json);
            String receivedMsg = input.readUTF();
            System.out.println("Received: " + receivedMsg);
        } catch (IOException | NumberFormatException e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }
}
