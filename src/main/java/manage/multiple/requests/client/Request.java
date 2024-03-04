package manage.multiple.requests.client;

public class Request {

    public String type;
    public String key;

    public String value;

    public String fileName;

    public Request(String type, String key, String value, String fileName){
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
