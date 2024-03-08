package store.json.objects.in.database.database;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class FileReadTest {

    @Test
    void read() throws IOException {
        DataHandler dataHandler = mock(DataHandler.class);
        ArgumentCaptor<Map<String, Object>> captor = ArgumentCaptor.forClass(Map.class);
        FileRead fileRead = new FileRead();
        fileRead.read(dataHandler);
        verify(dataHandler, times(1)).setMap(captor.capture());
        assertEquals("{name=pratik}", captor.getValue().toString());
    }
}