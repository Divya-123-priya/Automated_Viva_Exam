package com.aivivasystem.viva.controller;

import jakarta.validation.Valid;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.aivivasystem.viva.dto.LoginRequest;
import com.aivivasystem.viva.dto.LoginResponse;
import com.aivivasystem.viva.dto.RegisterRequest;
import com.aivivasystem.viva.model.User;
import com.aivivasystem.viva.service.UserService;
import com.aivivasystem.viva.util.JwtUtil;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired private UserService userService;
    @Autowired private JwtUtil     jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest req) {
        try {
            userService.registerUser(req);
            return ResponseEntity.ok(Map.of("message", "Registration successful"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        try {
            User user = userService.login(req.getEmail(), req.getPassword());
            if (user == null) {
                return ResponseEntity.badRequest()
                       .body(Map.of("error", "Invalid email or password"));
            }
            String token = jwtUtil.generateToken(user.getEmail(), user.getRole());
            LoginResponse res = new LoginResponse();
            res.setId(user.getId());
            res.setFullName(user.getFullName());
            res.setRegisterNumber(user.getRegisterNumber());
            res.setEmail(user.getEmail());
            res.setRole(user.getRole());
            res.setToken(token);
            return ResponseEntity.ok(res);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}