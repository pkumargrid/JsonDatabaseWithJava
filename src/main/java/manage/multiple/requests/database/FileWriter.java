package manage.multiple.requests.database;


import manage.multiple.requests.converter.FileObjectMapper;

import java.io.IOException;

public class FileWriter {

    public void write(DataHandler dataHandler) throws IOException {
        FileObjectMapper.doIntegration(dataHandler.getMap());
    }
}
