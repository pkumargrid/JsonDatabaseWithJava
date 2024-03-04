package work.with.json.server;

import work.with.json.client.Request;
import work.with.json.constants.UserQuery;
import work.with.json.database.DataHandler;
import work.with.json.exceptions.StatusException;

public class ClientRequestHandler {

    private static DataHandler dataHandler;

    public static void setUp(){
        dataHandler = new DataHandler();
    }

    public static String queryResolver(Request request) throws StatusException {
        String type = request.type.toUpperCase();
        UserQuery query = UserQuery.valueOf(type);
        return switch (query) {
            case GET -> typeGet(request.key);
            case SET -> {
                typeSet(request.key, request.value);
                yield "OK";
            }
            case DELETE -> {
                typeDelete(request.key);
                yield "OK";
            }
            case EXIT -> throw new IllegalStateException("Exit cannot be trapped here....");
        };
    }

    private static void typeDelete(String key) throws StatusException {
        dataHandler.deleteData(key);
    }

    private static void typeSet(String key, String value) throws StatusException {
        dataHandler.setData(key, value);
    }

    private static String typeGet(String key) throws StatusException {
        return dataHandler.getData(key);
    }

}