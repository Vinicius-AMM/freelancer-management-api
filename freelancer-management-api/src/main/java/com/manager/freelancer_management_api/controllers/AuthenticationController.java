package com.manager.freelancer_management_api.controllers;

import com.manager.freelancer_management_api.domain.user.dtos.LoginDTO;
import com.manager.freelancer_management_api.domain.user.dtos.LoginResponseDTO;
import com.manager.freelancer_management_api.domain.user.dtos.RegisterUserDTO;
import com.manager.freelancer_management_api.services.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid LoginDTO loginData){
        LoginResponseDTO response = authenticationService.login(loginData);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody @Valid RegisterUserDTO registrationData){
        authenticationService.register(registrationData);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
