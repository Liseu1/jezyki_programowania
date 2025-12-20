package pl.edu.pwr.exceptions;

import java.io.Serial;
import java.io.Serializable;

public class OrderCreationException extends Exception implements Serializable {
    @Serial
    private static final long serialVersionUID = 1;
    public OrderCreationException(String message) {
        super(message);
    }
}