package create.database.server;

import create.database.Database.DataProvider;
import create.database.constants.UserQuery;
import create.database.constants.Status;


public class Main {

    public static void main(String[] args) {
        JavaServer server = JavaServer.getInstance();
        DataProvider dataProvider = server.connect();
        create.database.client.Main client = new create.database.client.Main();
        boolean exit = false;
        while(!exit){
            try{
                UserQuery userQuery = client.getUserInput();
                switch (userQuery){
                    case GET :
                        System.out.println(dataProvider.getData(userQuery.getIndex()));
                        break;
                    case SET :
                        dataProvider.setData(userQuery.getIndex(),userQuery.getData());
                        break;
                    case DELETE:
                        dataProvider.deleteData(userQuery.getIndex());
                        break;
                    case EXIT:
                        exit = true;
                        break;
                }
                System.out.println(Status.OK);
            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
    }
}
