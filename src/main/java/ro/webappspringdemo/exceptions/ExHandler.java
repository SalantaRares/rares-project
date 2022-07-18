package ro.webappspringdemo.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExHandler extends ResponseEntityExceptionHandler {

    /**
     * Handles custom exceptions
     * @param ex - CustomException thrown in application
     * @return custom response for exceptions
     */
    @ExceptionHandler(value = {CustomException.class})
    protected ResponseEntity<ExceptionMessage> handleBnrHrException(CustomException ex) {
        return ResponseEntity.status(ex.getStatusCode()).body(new ExceptionMessage(ex.getExceptionMsg()));
    }
}
