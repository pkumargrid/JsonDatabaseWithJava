package store.json.objects.in.database.converter;

import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

class ClientFileReaderTest {

    @Test
    void read() throws IOException {
        String path = System.getProperty("user.dir").concat("/src/test/java/store/json/objects/in/database/client/data/setFile.json");
        File file = new File(path);
        System.out.println(path);
        StringBuilder stringBuilder = new StringBuilder();
        try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
            stringBuilder.append(bufferedReader.readLine());
        }
        assertEquals(stringBuilder.toString(), ClientFileReader.read("setFile.json"));
    }
}