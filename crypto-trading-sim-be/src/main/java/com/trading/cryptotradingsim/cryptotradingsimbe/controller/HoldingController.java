package com.trading.cryptotradingsim.cryptotradingsimbe.controller;

import com.trading.cryptotradingsim.cryptotradingsimbe.dto.response.HoldingResponse;
import com.trading.cryptotradingsim.cryptotradingsimbe.service.holding.HoldingService;
import com.trading.cryptotradingsim.cryptotradingsimbe.util.HoldingUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/holdings")
public class HoldingController {

    private final HoldingService holdingService;

    public HoldingController(HoldingService holdingService) {
        this.holdingService = holdingService;
    }

    @GetMapping
    public ResponseEntity<List<HoldingResponse>> getHoldings(@RequestHeader("X-User-Id") String userId) {
        List<HoldingResponse> holdings = holdingService.getHoldings(UUID.fromString(userId))
                .stream()
                .map(HoldingUtil::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(holdings);
    }
}
