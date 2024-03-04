package store.json.objects.in.database.client;

import com.google.gson.JsonElement;

public class Request {

    public String type;

    public JsonElement key;

    public JsonElement value;

    public String fileName;

    public Request(String type, JsonElement key, JsonElement value, String fileName){
        this.type = type;
        this.key = key;
        this.value = value;
        this.fileName = fileName;
    }

    @Override
    public String toString() {
        return "Request{" +
                "type='" + type + '\'' +
                ", key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
