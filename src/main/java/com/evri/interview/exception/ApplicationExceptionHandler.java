package com.evri.interview.exception;

import com.evri.interview.model.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ValidationException;

@RestControllerAdvice
@Slf4j
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {

    public static final String COURIER_NOT_FOUND_MESSAGE = "Courier not found";

    @ExceptionHandler(CourierNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCourierNotFoundException(
            CourierNotFoundException exception) {
        log.error(COURIER_NOT_FOUND_MESSAGE, exception);

        return new ResponseEntity<>(
                new ErrorResponse(COURIER_NOT_FOUND_MESSAGE), new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            ValidationException exception) {
        log.error(exception.getMessage(), exception);

        return new ResponseEntity<>(
                new ErrorResponse(exception.getMessage()), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

}
