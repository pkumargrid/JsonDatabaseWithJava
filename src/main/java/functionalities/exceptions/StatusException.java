package functionalities.exceptions;


import functionalities.constants.Status;

public class StatusException extends Exception {

    public StatusException(Status status){
        super(status.name());
    }
}
