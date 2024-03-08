package store.json.objects.in.database.database;

import org.junit.jupiter.api.Test;
import store.json.objects.in.database.converter.FileObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileWriterTest {

    @Test
    void write() throws IOException {
        DataHandler dataHandler = new DataHandler();
        Map<String, Object> map = new HashMap<>();
        map.put("name", "ansh");
        dataHandler.setMap(map);
        new store.json.objects.in.database.database.FileWriter().write(dataHandler);
        assertEquals("{name=ansh}", FileObjectMapper.dataExtractor(Map.class).toString());
    }
}