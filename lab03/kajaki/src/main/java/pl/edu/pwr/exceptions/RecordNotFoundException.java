package pl.edu.pwr.exceptions;

import java.io.Serial;
import java.io.Serializable;

public class RecordNotFoundException extends DataAccessException implements Serializable {
    @Serial
    private static final long serialVersionUID = 1;
    public RecordNotFoundException(String message) {
        super(message);
    }
    public RecordNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
