package store.json.objects.in.database.constants;

public enum Server {
    ADDRESS("127.0.0.1"),PORT("34522");

    public final String value;

    Server(String value){
        this.value = value;
    }
}
