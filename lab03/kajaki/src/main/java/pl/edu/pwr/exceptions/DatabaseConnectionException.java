package pl.edu.pwr.exceptions;

import java.io.Serial;
import java.io.Serializable;

public class DatabaseConnectionException extends Exception implements Serializable {

    @Serial
    private static final long serialVersionUID = 1;
    public DatabaseConnectionException(String message) {
        super(message);
    }
    public DatabaseConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
