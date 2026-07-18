package ecommerce_baceknd.exception.exceptionname;

import java.time.LocalDateTime;

public class ApiErrorResponse {

    private int status;
    private String errorCode;
    private String message;
    private LocalDateTime timestamp;

    public ApiErrorResponse(
            int status,
            String errorCode,
            String message) {

        this.status = status;
        this.errorCode = errorCode;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    public int getStatus() {
        return status;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

}
