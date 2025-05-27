package com.trading.cryptotradingsim.cryptotradingsimbe.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.trading.cryptotradingsim.cryptotradingsimbe.dto.APIError;
import com.trading.cryptotradingsim.cryptotradingsimbe.exception.*;
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

    @ExceptionHandler(InvalidPriceException.class)
    public ResponseEntity<APIError> handleInvalidPriceException(InvalidPriceException e) {
        return handleExceptionInternal(e, e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<APIError> handleNoResourceFoundException(NoResourceFoundException e) {
        return handleExceptionInternal(e, "Resource not found: " + e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<APIError> handleMissingRequestHeaderException(MissingRequestHeaderException e) {
        return handleExceptionInternal(e, "Missing request header: " + e.getHeaderName(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(JsonProcessingException.class)
    public ResponseEntity<APIError> handleJsonProcessingException(JsonProcessingException e) {
        return handleExceptionInternal(e, "Error processing JSON: " + e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<APIError> handleNotFoundException(NotFoundException e) {
        return handleExceptionInternal(e, e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NotImplementedException.class)
    public ResponseEntity<APIError> handleNotImplementedException(NotImplementedException e) {
        return handleExceptionInternal(e, e.getMessage(), HttpStatus.NOT_IMPLEMENTED);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<APIError> handleBadRequestException(BadRequestException e) {
        return handleExceptionInternal(e, e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<APIError> handleInsufficientFundsException(InsufficientFundsException e) {
        return handleExceptionInternal(e, e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<APIError> handleException(Exception e) {
        return handleExceptionInternal(e, "An unexpected error occurred: " + e.getMessage()
                , HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<APIError> handleExceptionInternal(Exception e, String message, HttpStatus status) {
        logError(e);
        return buildErrorResponse(message, status);
    }

    private ResponseEntity<APIError> buildErrorResponse(String message, HttpStatus status) {
        return ResponseEntity.status(status)
                .body(new APIError.Builder().message(message).status(status).build());
    }

    private void logError(Exception e) {
        log.error("Error: {}", e.getMessage());
    }
}
