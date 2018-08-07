package com.leonds.core.orm;

/**
 * @author Leon
 */
public class PersistentException extends RuntimeException {

    public PersistentException(Throwable cause) {
        super(cause);
    }

    public PersistentException(String message) {
        super(message);
    }

    public PersistentException(String message, Throwable cause) {
        super(message, cause);
    }
}
