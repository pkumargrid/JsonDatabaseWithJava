package create.database.client;

import create.database.constants.UserQuery;

import java.util.Scanner;

import static java.lang.Integer.*;

public class Main {

    private final Scanner scanner;

    public Main(){
        scanner = new Scanner(System.in);
    }

    public UserQuery getUserInput() {
        String userInput = scanner.nextLine();
        String[] input = userInput.split(" ");
        UserQuery userQuery = UserQuery.valueOf(input[0].toUpperCase());
        switch (userQuery){
            case GET, DELETE: userQuery.setIndex(parseInt(input[1]) - 1);
                break;
            case SET :
                userQuery.setIndex(parseInt(input[1]) -1);
                int idx = userInput.indexOf(" ",userInput.indexOf(" "));
                userQuery.setData(userInput.substring(idx + 1));
                break;
        }
        return userQuery;
    }

}
