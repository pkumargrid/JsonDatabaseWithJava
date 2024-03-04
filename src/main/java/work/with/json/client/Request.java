package work.with.json.client;

public class Request {


    public String type;
    public String key;

    public String value;

    public Request(String type, String key, String value){
        this.type = type;
        this.key = key;
        this.value = value;
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
