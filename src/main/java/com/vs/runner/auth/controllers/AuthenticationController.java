package com.vs.runner.auth.controllers;

import com.vs.runner.auth.models.AuthenticationRequest;
import com.vs.runner.auth.models.AuthenticationResponse;
import com.vs.runner.auth.models.ConfirmRegisterRequest;
import com.vs.runner.auth.models.RegisterRequest;
import com.vs.runner.auth.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    @PostMapping("/register")
    public ResponseEntity<?> register (@RequestBody RegisterRequest request) {
        authenticationService.register(request);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PutMapping("/confirm_register")
    public ResponseEntity<?> confirmRegister (@RequestBody ConfirmRegisterRequest request) {
        authenticationService.confirmRegistration(request);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate (@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }
}
