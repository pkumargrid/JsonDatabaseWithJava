package manage.multiple.requests.database;


import manage.multiple.requests.converter.FileObjectMapper;
import java.io.IOException;
import java.util.Map;

public class FileRead {

    public void read(DataHandler dataHandler) throws IOException, ClassCastException{
        dataHandler.setMap((Map<String, String>) FileObjectMapper.dataExtractor(Map.class));
    }

}
