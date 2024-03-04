package manage.multiple.requests.exceptions;


import manage.multiple.requests.constants.Status;

public class StatusException extends Exception {

    public StatusException(Status status){
        super(status.name());
    }
}
