package store.json.objects.in.database.server;


import store.json.objects.in.database.constants.Server;
import store.json.objects.in.database.database.DataHandler;
import store.json.objects.in.database.database.FileRead;
import store.json.objects.in.database.database.FileWriter;

import static java.lang.Integer.parseInt;

public class Main {


    public static void main(String[] args) {
        JavaServer.connect(new DataHandler(), new FileRead(), new FileWriter(), parseInt(Server.TEST1_PORT.value));
    }

}
