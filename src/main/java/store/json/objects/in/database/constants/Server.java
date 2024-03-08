package store.json.objects.in.database.constants;

public enum Server {
    ADDRESS("127.0.0.1"),PORT("34522"), TEST_PORT("34555"),
    TEST1_PORT("34556");

    public final String value;

    Server(String value){
        this.value = value;
    }
}
