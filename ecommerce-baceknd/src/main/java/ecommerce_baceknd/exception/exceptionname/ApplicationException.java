package ecommerce_baceknd.exception.exceptionname;

import org.springframework.http.HttpStatus;

public class ApplicationException extends RuntimeException{

    private final HttpStatus status;
    private final String errorCode;

    public ApplicationException(
            HttpStatus status,
            String message,
            String errorCode) {

        super(message);
        this.status = status;
        this.errorCode = errorCode;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
