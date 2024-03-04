package create.database.server;


import create.database.Database.DataProvider;

public class JavaServer {

    private static JavaServer instance;
    private static DataProvider dataProvider;

    private JavaServer() {
        dataProvider = new DataProvider(100);
        dataProvider.init();
    }

    public static synchronized JavaServer getInstance() {
        if (instance == null) {
            instance = new JavaServer();
        }
        return instance;
    }

    public DataProvider connect() {
        return dataProvider;
    }
}

