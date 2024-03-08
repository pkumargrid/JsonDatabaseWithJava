package store.json.objects.in.database.converter;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class JsonObjectMapperTest {

    @Test
    void mapToObj() {
        String value = "{name:pratik, age: 22}";
        Map<String, Object> map = Map.of("name","pratik", "age", 22.0);
        assertEquals(map, (Map<String, Object>)JsonObjectMapper.mapToObj(value, Map.class));
    }
}