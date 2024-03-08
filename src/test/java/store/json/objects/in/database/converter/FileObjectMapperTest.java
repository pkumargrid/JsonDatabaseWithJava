package store.json.objects.in.database.converter;

import com.google.gson.Gson;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class FileObjectMapperTest {

    @Test
    void dataExtractor() throws IOException {
        Map<String, Object> map = (Map<String, Object>) FileObjectMapper.dataExtractor(Map.class);
        assertEquals(Map.of("name","ansh"), map);
    }

    @Test
    void doIntegration() throws IOException {
        final String path = System.getProperty("user.dir").concat("/src/test/java/store/json/objects/in/database/server/data/db.json");
        Map<String, Object> map = Map.of("name", "pratik");
        FileObjectMapper.doIntegration(map);
        File file = new File(path);
        StringBuilder stringBuilder = new StringBuilder();
        try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)))){
            String line = null;
            while((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
        }
        assertEquals((Map<String, Object>) (new Gson().fromJson(stringBuilder.toString(), Map.class)), map);
    }
}