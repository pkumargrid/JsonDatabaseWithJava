package store.json.objects.in.database.converter;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileObjectMapper {

    private static final String path = System.getProperty("user.dir").concat("/src/server/data/db.json");

    public static Object dataExtractor(Class<?> clazz) throws IOException, ClassCastException {
        File file = new File(path);
        try(FileReader reader = new FileReader(file)){
            return new Gson().fromJson(reader, clazz);
        }
    }

    public static void doIntegration(Object object) throws IOException, ClassCastException {
        File file = new File(path);
        try(FileWriter writer = new FileWriter(file)){
            new Gson().toJson(object, writer);
        }
    }

}
