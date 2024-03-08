package store.json.objects.in.database.client;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.google.gson.JsonElement;

import store.json.objects.in.database.constants.Server;
import store.json.objects.in.database.converter.ClientFileReader;
import store.json.objects.in.database.converter.JsonElementConverter;
import store.json.objects.in.database.converter.ObjectJsonMapper;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import static java.lang.Integer.parseInt;

public class Main {

    @Parameter(names = "-t")
    static String type;

    @Parameter(names = "-k", converter = JsonElementConverter.class)
    private static JsonElement key;

    @Parameter(names = "-v", converter = JsonElementConverter.class)
    private static JsonElement value;

    @Parameter(names = "-in")
    private static String fileName;

    public static void main(String[] args) {
        JCommander.newBuilder()
                .addObject(new Main())
                .build()
                .parse(args);
//        System.out.println("Client started!");
        try (
                Socket socket = new Socket(Server.ADDRESS.value,parseInt(Server.TEST_PORT2.value));
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output  = new DataOutputStream(socket.getOutputStream())
        ) {
            Request request = new Request(type, key, value, fileName);
            String json = ObjectJsonMapper.mapToJson(request);
            if(fileName != null){
                json = ClientFileReader.read(fileName);
            }
//            System.out.println("Sent: " + json);
            output.writeUTF(json);
            String receivedMsg = input.readUTF();
            System.out.println("Received: " + receivedMsg);
        } catch (IOException | NumberFormatException e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }
}
