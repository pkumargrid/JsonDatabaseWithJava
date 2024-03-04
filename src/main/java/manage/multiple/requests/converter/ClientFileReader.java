package manage.multiple.requests.converter;

import java.io.*;

public class ClientFileReader {

    private static String path = System.getProperty("user.dir").concat("/src/client/data/");

    public static String read(String fileName) throws IOException {
        path = path.concat(fileName);
        File file = new File(path);
        try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)))){
            return br.readLine();
        }
    }
}
