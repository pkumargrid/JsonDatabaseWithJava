package create.database.exceptions;

import create.database.constants.Status;

public class StatusException extends Exception {

    public StatusException(Status status) {
        super(status.name());
    }

}
