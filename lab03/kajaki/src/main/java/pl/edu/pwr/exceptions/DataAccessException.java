package pl.edu.pwr.exceptions;

import java.io.Serial;
import java.io.Serializable;

public class DataAccessException extends Exception implements Serializable {
    @Serial
    private static final long serialVersionUID = 1;
    public DataAccessException(String message) {
        super(message);
    }
    public DataAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
