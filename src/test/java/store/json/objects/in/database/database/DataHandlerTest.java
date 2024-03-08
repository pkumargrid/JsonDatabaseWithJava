package store.json.objects.in.database.database;

import org.junit.jupiter.api.Test;
import store.json.objects.in.database.exceptions.StatusException;

import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class DataHandlerTest {

    @Test
    void statusChecker() {
        Map<String, Object> mockMap = mock(Map.class);
        when(mockMap.containsKey(anyString())).thenReturn(false);
        DataHandler dataHandler = new DataHandler();
        dataHandler.setMap(mockMap);
        assertThrows(StatusException.class, () -> dataHandler.statusChecker("pratik"));
    }

    @Test
    void getData() throws StatusException {
        Map<String, Object> mockMap = mock(Map.class);
        when(mockMap.get(anyString())).thenReturn("kumar");
        when(mockMap.containsKey(anyString())).thenReturn(true);
        DataHandler dataHandler = new DataHandler();
        dataHandler.setMap(mockMap);
        assertEquals("kumar", dataHandler.getData("pratik"));
    }

    @Test
    void setData() {
        Map<String, Object> mockMap = mock(Map.class);
        when(mockMap.put(eq("pratik"), eq("kumar"))).thenReturn(null);
        DataHandler dataHandler = new DataHandler();
        dataHandler.setMap(mockMap);
        dataHandler.setData("pratik", "kumar");
        verify(mockMap).put("pratik", "kumar");
    }

    @Test
    void deleteData() throws StatusException {
        Map<String, Object> mockMap = mock(Map.class);
        when(mockMap.remove(eq("pratik"))).thenReturn(null);
        DataHandler dataHandler = new DataHandler();
        dataHandler.setMap(mockMap);
        when(mockMap.containsKey(anyString())).thenReturn(true);
        dataHandler.deleteData("pratik");
        verify(mockMap).remove("pratik");
    }

    @Test
    void getMap() {
        Map<String, Object> mockMap = mock(Map.class);
        DataHandler dataHandler = new DataHandler();
        dataHandler.setMap(mockMap);
        assertEquals(mockMap, dataHandler.getMap());
    }

    @Test
    void setMap() {
        Map<String, Object> mockMap = mock(Map.class);
        DataHandler dataHandler = new DataHandler();
        dataHandler.setMap(mockMap);
        assertEquals(mockMap, dataHandler.getMap());
    }
}