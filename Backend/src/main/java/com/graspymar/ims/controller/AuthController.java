package com.graspymar.ims.controller;

import com.graspymar.ims.dto.LoginRequest;
import com.graspymar.ims.dto.LoginResponse;
import com.graspymar.ims.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {

    System.out.println("ENTRO AL LOGIN");


            return ResponseEntity.ok(authService.login(request));

        }

}
