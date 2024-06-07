package com.itesm.panoptimize.util.middleware;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import java.net.ConnectException;
import java.util.Arrays;
import java.util.logging.Logger;

//@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = Logger.getLogger(GlobalExceptionHandler.class.getName());

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponse> handleResponseStatusException(ResponseStatusException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getReason(),
                ex.getMessage()
        );
        LOGGER.severe(ex.getMessage());
        return new ResponseEntity<>(errorResponse, ex.getStatusCode());
    }

    @ExceptionHandler(ConnectException.class)
    public ResponseEntity<ConnectException> handleConnectException(ConnectException ex) {
        LOGGER.severe(ex.getMessage());
        return new ResponseEntity<>(ex, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getClass().getSimpleName(),
                ex.getMessage()
        );
        LOGGER.severe(ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getClass().getSimpleName(),
                ex.getMessage()
        );
        LOGGER.severe(Arrays.toString(ex.getStackTrace()));
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
