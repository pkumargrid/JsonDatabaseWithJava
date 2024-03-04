package functionalities.server;


import functionalities.constants.UserQuery;
import functionalities.database.DataHandler;
import functionalities.exceptions.StatusException;


import static functionalities.constants.Capacity.CAPACITY;
import static java.lang.Integer.parseInt;

public class ClientRequestHandler {

    private static DataHandler dataHandler;

    public static void setUp(){
        dataHandler = new DataHandler(CAPACITY.value);
        dataHandler.init();
    }

    public static String getType(String messageFomClient){
        return messageFomClient.substring(0,messageFomClient.indexOf(" "));
    }

    public static String extract(String messageFomClient){
        return messageFomClient.substring(messageFomClient.indexOf(":") + 2);
    }

    public static String queryResolver(String messageFromClient) throws StatusException {
        messageFromClient = extract(messageFromClient);
        String type = getType(messageFromClient);
        UserQuery query = UserQuery.valueOf(type.toUpperCase());
        return switch (query) {
            case GET -> typeGet(messageFromClient);
            case SET -> {
                typeSet(messageFromClient);
                yield "OK";
            }
            case DELETE -> {
                typeDelete(messageFromClient);
                yield "OK";
            }
            case EXIT -> throw new IllegalStateException("Exit cannot be trapped here....");
        };
    }

    private static void typeDelete(String messageFromClient) throws StatusException {
        dataHandler.deleteData(parseInt(messageFromClient.split(" ")[1])-1);
    }

    private static void typeSet(String messageFromClient) throws StatusException {
        int index = parseInt(messageFromClient.split(" ")[1]);
        int fromIndex = messageFromClient.indexOf(" ",messageFromClient.indexOf(" "));
        String toStoreMessage = messageFromClient.substring(fromIndex + 1);
        dataHandler.setData(index-1,toStoreMessage);
    }

    private static String typeGet(String messageFromClient) throws StatusException {
        int index = parseInt(messageFromClient.split(" ")[1]);
        return dataHandler.getData(index-1);
    }

}