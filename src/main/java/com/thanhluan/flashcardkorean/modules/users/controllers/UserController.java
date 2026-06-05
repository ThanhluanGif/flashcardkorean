package com.thanhluan.flashcardkorean.modules.users.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.thanhluan.flashcardkorean.modules.users.dtos.AuthenticationResponse;
import com.thanhluan.flashcardkorean.modules.users.dtos.UserLoginRequest;
import com.thanhluan.flashcardkorean.modules.users.dtos.UserRegisterRequest;
import com.thanhluan.flashcardkorean.modules.users.dtos.UserResponse;
import com.thanhluan.flashcardkorean.modules.users.services.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // API Đăng ký tài khoản
    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody UserRegisterRequest request) {
        UserResponse response = userService.registerUser(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // API Đăng nhập
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@Valid @RequestBody UserLoginRequest request) {
        AuthenticationResponse response = userService.authenticateUser(request);
        return ResponseEntity.ok(response);
    }

    // API Lấy danh sách user (Thường chỉ dành cho Admin)
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
}
