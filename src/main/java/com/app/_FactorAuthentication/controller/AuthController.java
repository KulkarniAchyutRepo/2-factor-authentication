package com.app._FactorAuthentication.controller;
import com.app._FactorAuthentication.service.AuthService;
import com.app._FactorAuthentication.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.app._FactorAuthentication.exceptions.UserNotFoundException;
import com.app._FactorAuthentication.exceptions.InvalidOtpException;


@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @Autowired
    private RegistrationService registrationService;

    @PostMapping("/2fa")
    public ResponseEntity<String> login(@RequestParam String email) {
        if (!authService.checkUserExists(email)) {
            throw new UserNotFoundException("User does not exist. Please register first.");
        }
        authService.generateAndSendOtp(email);
        return ResponseEntity.ok("OTP sent to email");
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verify(@RequestParam String email, @RequestParam String otp) {
        boolean isVerified = authService.verifyOtp(email, otp);
        if (!isVerified) {
            throw new InvalidOtpException("Invalid OTP or OTP expired");
        }
        return ResponseEntity.ok("User authenticated successfully");
    }

}