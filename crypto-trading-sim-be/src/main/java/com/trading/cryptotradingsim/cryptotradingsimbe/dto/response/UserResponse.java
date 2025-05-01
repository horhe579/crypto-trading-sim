package com.trading.cryptotradingsim.cryptotradingsimbe.dto.response;

import java.time.OffsetDateTime;
import java.util.UUID;

public record UserResponse(UUID id, Double balance, OffsetDateTime createdAt) {
}