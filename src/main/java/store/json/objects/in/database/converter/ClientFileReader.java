package store.json.objects.in.database.converter;

import java.io.*;

public class ClientFileReader {

    private static String path = System.getProperty("user.dir").concat("/src/client/data/");

    public static String read(String name) throws IOException, ClassCastException {
        path = path.concat(name);
        File file = new File(path);
        StringBuilder stringBuilder = new StringBuilder();
        try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)))){
            stringBuilder.append(br.readLine());
        }
        return stringBuilder.toString();
    }
}
