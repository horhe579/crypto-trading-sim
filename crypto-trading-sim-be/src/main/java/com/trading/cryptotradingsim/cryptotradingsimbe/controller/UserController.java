package com.trading.cryptotradingsim.cryptotradingsimbe.controller;

import com.trading.cryptotradingsim.cryptotradingsimbe.dto.response.UserResponse;
import com.trading.cryptotradingsim.cryptotradingsimbe.service.user.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.trading.cryptotradingsim.cryptotradingsimbe.util.UserUtil.toResponse;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<UserResponse> getUser(@RequestHeader("X-User-Id") String userId) {
        return ResponseEntity.ok(toResponse(userService.getUser(UUID.fromString(userId))));
    }

    @PostMapping("/reset")
    public ResponseEntity<UserResponse> resetUser(@RequestHeader("X-User-Id") String userId) {
        return ResponseEntity.ok(toResponse(userService.resetUser(UUID.fromString(userId))));
    }
}
