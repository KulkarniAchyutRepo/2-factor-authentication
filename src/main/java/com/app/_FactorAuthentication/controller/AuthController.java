package com.app._FactorAuthentication.controller;
import com.app._FactorAuthentication.dto.LoginViaPasswordRequest;
import com.app._FactorAuthentication.dto.AuthJwtResponse;
import com.app._FactorAuthentication.dto.RefreshTokenRequest;
import com.app._FactorAuthentication.entity.User;
import com.app._FactorAuthentication.repository.UserRepository;
import com.app._FactorAuthentication.service.AuthService;
import com.app._FactorAuthentication.service.JwtService;
import com.app._FactorAuthentication.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import com.app._FactorAuthentication.exceptions.authExceptions.UserNotFoundException;


@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private AuthService authService;
    private RegistrationService registrationService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    @Autowired
    public AuthController(AuthService authService, RegistrationService registrationService, AuthenticationManager authenticationManager, UserDetailsService userDetailsService, JwtService jwtService, UserRepository userRepository) {
        this.authService = authService;
        this.registrationService = registrationService;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @PostMapping("/loginViaPassword")
    public ResponseEntity<AuthJwtResponse> loginViaPassword(@RequestBody LoginViaPasswordRequest loginViaPasswordRequest) {
        String email = loginViaPasswordRequest.getEmail();
        String password = loginViaPasswordRequest.getPassword();

        // Authenticate using Spring Security
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));

        // Generate JWT tokens
        String accessToken = jwtService.generateToken(email, true);
        String refreshToken = jwtService.generateToken(email, false);

        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        AuthJwtResponse authJwtResponse = new AuthJwtResponse(accessToken, refreshToken, user);

        return ResponseEntity.ok(authJwtResponse);
    }


    @PostMapping("/refreshToken")
    public ResponseEntity<?> getRefreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest){
        String refreshToken = refreshTokenRequest.refreshToken();
        if(jwtService.validateToken(refreshToken)){
            String usernameFromToken = jwtService.getUsernameFromToken(refreshToken);
            String accessToken = jwtService.generateToken(usernameFromToken, true);
            refreshToken = jwtService.generateToken(usernameFromToken, false);
            User user = userRepository.findByEmail(usernameFromToken).get();
            AuthJwtResponse authJwtResponse = new AuthJwtResponse(accessToken, refreshToken, user);
            return ResponseEntity.ok(authJwtResponse);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
    }

    @PostMapping("/loginViaEmail")
    public ResponseEntity<String> loginViaEmail(@RequestParam String email) {
        if (!authService.checkUserExists(email)) {
            throw new UserNotFoundException("User does not exist. Please register first.");
        }

        String responseMessage = authService.generateAndSendOtp(email);
        return ResponseEntity.ok(responseMessage);
    }

    @PostMapping("/verifyEmailLogin")
    public ResponseEntity<String> verifyEmailLogin(@RequestParam String email, @RequestParam String otp) {
        boolean isVerified = authService.verifyOtp(email, otp);
        if (isVerified) {
            return ResponseEntity.ok("User authenticated successfully via email.");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed. Invalid otp or otp expired");
    }

}