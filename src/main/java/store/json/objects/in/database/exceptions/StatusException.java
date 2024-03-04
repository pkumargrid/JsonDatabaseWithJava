package store.json.objects.in.database.exceptions;


import store.json.objects.in.database.constants.Status;

public class StatusException extends Exception {

    public StatusException(Status status){
        super(status.name());
    }
}
