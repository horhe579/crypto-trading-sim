package com.trading.cryptotradingsim.cryptotradingsimbe.dto.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {
    private UUID id;
    private Double balance;
    private OffsetDateTime createdAt;
} 