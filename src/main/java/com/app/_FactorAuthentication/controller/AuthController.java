package com.app._FactorAuthentication.controller;

import com.app._FactorAuthentication.service.AuthService;
import com.app._FactorAuthentication.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @Autowired
    private RegistrationService registrationService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String email) {
        if (!authService.checkUserExists(email)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User does not exist. Please register first.");
        }
        authService.generateAndSendOtp(email);
        return ResponseEntity.ok("OTP sent to email");
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verify(@RequestParam String email, @RequestParam String otp) {
        boolean isVerified = authService.verifyOtp(email, otp);
        if (isVerified) {
            return ResponseEntity.ok("User authenticated successfully");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid OTP or OTP expired");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestParam String email, @RequestParam String name, @RequestParam String password) {
        String response = registrationService.registerUser(email, name, password);
        if (response.equals("User registered successfully.")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}
