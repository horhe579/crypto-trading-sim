package com.trading.cryptotradingsim.cryptotradingsimbe.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.trading.cryptotradingsim.cryptotradingsimbe.dto.APIError;
import com.trading.cryptotradingsim.cryptotradingsimbe.exception.BadRequestException;
import com.trading.cryptotradingsim.cryptotradingsimbe.exception.InsufficientFundsException;
import com.trading.cryptotradingsim.cryptotradingsimbe.exception.NotFoundException;
import com.trading.cryptotradingsim.cryptotradingsimbe.exception.NotImplementedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

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
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new APIError("An unexpected error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR));
    }
}
