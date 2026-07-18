package ecommerce_baceknd.exception.Controller;

import ecommerce_baceknd.exception.exceptionname.ApiErrorResponse;
import ecommerce_baceknd.exception.exceptionname.ApplicationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ApiErrorResponse> handleApplicationException(
            ApplicationException ex) {

        ApiErrorResponse response = new ApiErrorResponse(
                ex.getStatus().value(),
                ex.getErrorCode(),
                ex.getMessage()
        );

        return ResponseEntity
                .status(ex.getStatus())
                .body(response);
    }

}
