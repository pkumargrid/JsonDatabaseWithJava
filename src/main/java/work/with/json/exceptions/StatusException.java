package work.with.json.exceptions;


import work.with.json.constants.Status;

public class StatusException extends Exception {

    public StatusException(Status status){
        super(status.name());
    }
}
