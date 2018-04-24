package net.greenpoppy.tahiti.resource;

import org.springframework.http.HttpStatus;


public class ResourceException
    extends RuntimeException {

    private final HttpStatus httpStatus;
    private final String type;

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getType() {
        return type;
    }

    public ResourceException(HttpStatus httpStatus, String type, String message) {
        super(message);
        this.httpStatus = httpStatus;
        this.type = type;
    }

    public ResourceException(HttpStatus httpStatus, String type, String message, Throwable cause) {
        super(message, cause);
        this.httpStatus = httpStatus;
        this.type = type;
    }
}
