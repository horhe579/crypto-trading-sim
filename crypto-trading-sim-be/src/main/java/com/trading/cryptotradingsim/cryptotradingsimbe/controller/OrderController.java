package com.trading.cryptotradingsim.cryptotradingsimbe.controller;

import com.trading.cryptotradingsim.cryptotradingsimbe.dto.request.OrderRequest;
import com.trading.cryptotradingsim.cryptotradingsimbe.dto.response.OrderResponse;
import com.trading.cryptotradingsim.cryptotradingsimbe.service.trade.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.trading.cryptotradingsim.cryptotradingsimbe.util.OrderUtil.toModel;
import static com.trading.cryptotradingsim.cryptotradingsimbe.util.OrderUtil.toResponse;

@Validated
@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping()
    @RequestMapping("/buy")
    // Todo validate id uuid
    public ResponseEntity<OrderResponse> createBuyOrder(@RequestHeader("X-User-Id") String userId,
                                                        @RequestBody @Valid OrderRequest buyOrderRequest) {
        return ResponseEntity.ok(
                toResponse(orderService.executeBuy(toModel(buyOrderRequest, UUID.fromString(userId))))
        );
    }

    @PostMapping
    @RequestMapping("/sell")
    // Todo validate id uuid
    public ResponseEntity<OrderResponse> createSellOrder(@RequestHeader("X-User-Id") String userId,
                                                         @RequestBody @Valid OrderRequest sellOrderRequest) {
        return ResponseEntity.ok(
                toResponse(orderService.executeSell(toModel(sellOrderRequest, UUID.fromString(userId))))
        );
    }
}
