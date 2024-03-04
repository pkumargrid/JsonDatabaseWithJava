package store.json.objects.in.database.database;


import store.json.objects.in.database.converter.FileObjectMapper;

import java.io.IOException;
import java.util.Map;

public class FileRead {

    public void read(DataHandler dataHandler) throws IOException {
        dataHandler.setMap((Map<String, Object>) FileObjectMapper.dataExtractor(Map.class));
    }

}
