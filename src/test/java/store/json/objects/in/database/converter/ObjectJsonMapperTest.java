package store.json.objects.in.database.converter;

import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;

class ObjectJsonMapperTest {

    @Test
    void mapToJson() {
        String value = "{\"name\":\"pratik\",\"age\":22.0}";
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("name", "pratik");
        map.put("age", 22.0);
        assertEquals(value,ObjectJsonMapper.mapToJson(map));
    }
}