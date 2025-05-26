package com.trading.cryptotradingsim.cryptotradingsimbe.dto;

import org.springframework.http.HttpStatus;

public record APIError(
        String message,
        HttpStatus status
) {
    public static final class Builder {

        String message;
        HttpStatus status;

        public Builder() {
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder status(HttpStatus status) {
            this.status = status;
            return this;
        }

        public Builder status(int status) throws IllegalArgumentException {
            this.status = HttpStatus.valueOf(status);
            return this;
        }

        public APIError build() {
            return new APIError(message, status);
        }
    }
}
