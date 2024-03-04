package manage.multiple.requests.client;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import manage.multiple.requests.constants.Server;
import manage.multiple.requests.converter.ClientFileReader;
import manage.multiple.requests.converter.ObjectJsonMapper;

import static java.lang.Integer.parseInt;

public class Main {

    @Parameter(names = "-t")
    private static String type;

    @Parameter(names = "-k")
    private static String index;

    @Parameter(names = "-v")
    private static String inputMessage;

    @Parameter(names = "-in")
    private static String fileName;

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
            Request request = new Request(type, index, inputMessage, fileName);
            String json = ObjectJsonMapper.mapToJson(request);
            if(fileName != null){
                json = ClientFileReader.read(fileName);
            }
            System.out.println("Sent: " + json);
            output.writeUTF("Sent: " + json);
            String receivedMsg = input.readUTF();
            System.out.println("Received: " + receivedMsg);
        } catch (IOException | NumberFormatException e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }
}
