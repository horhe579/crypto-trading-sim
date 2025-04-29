package com.trading.cryptotradingsim.cryptotradingsimbe.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.trading.cryptotradingsim.cryptotradingsimbe.dto.APIError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(JsonProcessingException.class)
    public ResponseEntity<APIError> handleJsonProcessingException(JsonProcessingException e) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        return ResponseEntity.status(status).body(new APIError(e.getMessage(), status));
    }
}
