package store.json.objects.in.database.client;

import connect.database.to.server.constants.Server;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import static java.lang.Integer.parseInt;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MainTest {

    @Test
    void testMain() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
        try(ServerSocket serverSocket = new ServerSocket(parseInt(Server.PORT.value));){
            Thread serverThread = new Thread(() -> {
                try (Socket clientSocket = serverSocket.accept()) {
                    String messageFromClient = new DataInputStream(clientSocket.getInputStream()).readUTF();
                    new DataOutputStream(clientSocket.getOutputStream()).writeUTF("Server Response");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            Thread clientThread = new Thread(() -> {
                try {
                    Main.main(new String[] {"-in", "setFile.json"});
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            serverThread.start();
            clientThread.start();
            try {
                serverThread.join();
                clientThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            assertEquals("Received: Server Response\n", outputStream.toString());
            System.setOut(originalOut);
        }


    }
    @Test
    void failTestMain() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
        Main.main(new String[]{"-in","setFile.json"});
        assertEquals("Exception: Connection refused\n", outputStream.toString());
    }
}