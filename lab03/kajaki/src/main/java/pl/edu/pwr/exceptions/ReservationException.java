package pl.edu.pwr.exceptions;

import java.io.Serial;
import java.io.Serializable;

public class ReservationException extends Exception implements Serializable {
    @Serial
    private static final long serialVersionUID = 1;
    public ReservationException(String message) {
        super(message);
    }
}
