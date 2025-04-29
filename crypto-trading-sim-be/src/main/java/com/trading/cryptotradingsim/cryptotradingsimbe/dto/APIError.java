package com.trading.cryptotradingsim.cryptotradingsimbe.dto;

import org.springframework.http.HttpStatus;

public record APIError(String message, HttpStatus status) {
}
