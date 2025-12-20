package pl.edu.pwr.exceptions;

import java.io.Serial;
import java.io.Serializable;

public class OfferCreationException extends Exception implements Serializable {
    @Serial
    private static final long serialVersionUID = 1;
    public OfferCreationException(String message) {
        super(message);
    }
}