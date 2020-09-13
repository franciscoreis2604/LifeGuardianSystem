package com.LGS.exception;

/**
 * Class necessary that will be used to throw the custom exception
 */
public class ApiRequestException extends RuntimeException {

    public ApiRequestException(String message) {
        super(message);
    }
    public ApiRequestException(String message, Throwable cause) {
        super(message, cause);
    }

}
