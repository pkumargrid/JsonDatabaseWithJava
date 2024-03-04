package work.with.json.server;


import work.with.json.client.Request;
import work.with.json.constants.Status;
import work.with.json.constants.UserQuery;


public class Response {

    public String response;

    public String value;

    public String reason;

    Response(String response, String value, String reason){
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

    public static Response generator(Request request, String valueToReturn) {
        UserQuery userQuery = UserQuery.valueOf(request.type.toUpperCase());
        return switch (userQuery) {
            case GET -> new Response(Status.OK.name(), valueToReturn, null);
            case SET, DELETE, EXIT -> new Response(Status.OK.name(), null, null);
        };
    }
}
