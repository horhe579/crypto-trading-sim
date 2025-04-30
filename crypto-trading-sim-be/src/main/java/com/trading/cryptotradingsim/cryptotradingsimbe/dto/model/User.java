package com.trading.cryptotradingsim.cryptotradingsimbe.dto.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private UUID id;
    private Double balance;
    private OffsetDateTime createdAt;
}
