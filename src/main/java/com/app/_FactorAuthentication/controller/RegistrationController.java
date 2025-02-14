package com.app._FactorAuthentication.controller;

import com.app._FactorAuthentication.dto.RegistrationRequest;
import com.app._FactorAuthentication.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegistrationController {
    RegistrationService registrationService;
    @Autowired
    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping("/api/v1/register")
    public ResponseEntity<String> register(@RequestBody RegistrationRequest registrationRequest) {
        String email = registrationRequest.email();
        String name = registrationRequest.name();
        String password = registrationRequest.password();
        String response = registrationService.registerUser(email, name, password);
        return ResponseEntity.ok(response);
    }
}
