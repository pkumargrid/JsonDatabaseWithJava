package store.json.objects.in.database.database;


import store.json.objects.in.database.converter.FileObjectMapper;

import java.io.IOException;

public class FileWriter {

    public void write(DataHandler dataHandler) throws IOException {
        FileObjectMapper.doIntegration(dataHandler.getMap());
    }
}
