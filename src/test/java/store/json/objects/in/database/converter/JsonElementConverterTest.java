package store.json.objects.in.database.converter;

import com.google.gson.JsonPrimitive;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JsonElementConverterTest {

    @Test
    void convert() {
        String value = "pratik";
        assertEquals(new JsonPrimitive(value), new JsonElementConverter().convert(value));
    }

    @Test
    void failConvert() {
        assertThrows(Exception.class, () -> new JsonElementConverter().convert(null));
    }
}