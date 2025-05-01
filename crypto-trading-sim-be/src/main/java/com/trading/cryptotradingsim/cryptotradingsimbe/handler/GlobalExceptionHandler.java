package com.trading.cryptotradingsim.cryptotradingsimbe.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.trading.cryptotradingsim.cryptotradingsimbe.dto.APIError;
import com.trading.cryptotradingsim.cryptotradingsimbe.exception.BadRequestException;
import com.trading.cryptotradingsim.cryptotradingsimbe.exception.InsufficientFundsException;
import com.trading.cryptotradingsim.cryptotradingsimbe.exception.NotFoundException;
import com.trading.cryptotradingsim.cryptotradingsimbe.exception.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<APIError> handleNoResourceFoundException(NoResourceFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new APIError("Resource not found: " + e.getMessage(), HttpStatus.NOT_FOUND));
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<APIError> handleMissingRequestHeaderException(MissingRequestHeaderException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new APIError("Missing request header: " + e.getHeaderName(), HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(JsonProcessingException.class)
    public ResponseEntity<APIError> handleJsonProcessingException(JsonProcessingException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new APIError("Error processing JSON: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<APIError> handleNotFoundException(NotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new APIError(e.getMessage(), HttpStatus.NOT_FOUND));
    }

    @ExceptionHandler(NotImplementedException.class)
    public ResponseEntity<APIError> handleNotImplementedException(NotImplementedException e) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED)
                .body(new APIError(e.getMessage(), HttpStatus.NOT_IMPLEMENTED));
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<APIError> handleBadRequestException(BadRequestException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new APIError(e.getMessage(), HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<APIError> handleInsufficientFundsException(InsufficientFundsException e) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(new APIError(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<APIError> handleGenericException(Exception e) {
        logError(e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new APIError("An unexpected error occurred. ", HttpStatus.INTERNAL_SERVER_ERROR));
    }

    private void logError(Exception e) {
        log.error("Error: {}", e.getMessage());
    }
}
