package org.route.exception;

public class StockShortageException extends RuntimeException {

    public StockShortageException(String message) {
        super(message);
    }
}
