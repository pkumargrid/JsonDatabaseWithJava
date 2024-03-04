package store.json.objects.in.database.server;


import com.google.gson.JsonElement;
import store.json.objects.in.database.client.Request;
import store.json.objects.in.database.constants.Status;
import store.json.objects.in.database.constants.UserQuery;

public class Response {

    public String response;

    public JsonElement value;

    public String reason;

    Response(String response, JsonElement value, String reason){
        this.response = response;
        this.value = value;
        this.reason = reason;
    }


    @Override
    public String toString() {
        return "Response{" +
                "response='" + response + '\'' +
                ", value='" + value + '\'' +
                ", reason='" + reason + '\'' +
                '}';
    }

    public static Response generator(Request request, JsonElement valueToReturn) {
        UserQuery userQuery = UserQuery.valueOf(request.type.toUpperCase());
        return switch (userQuery) {
            case GET -> new Response(Status.OK.name(), valueToReturn, null);
            case SET, DELETE, EXIT -> new Response(Status.OK.name(), null, null);
        };
    }
}
