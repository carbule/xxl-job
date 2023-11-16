package com.korant.youya.workplace.exception;

import java.io.Serial;
import java.io.Serializable;

public class YouyaException extends RuntimeException implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private int statusCode = 500;

    public YouyaException(String message) {
        super(message);
    }

    public YouyaException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

    public int getStatusCode() {
        return statusCode;
    }
}
