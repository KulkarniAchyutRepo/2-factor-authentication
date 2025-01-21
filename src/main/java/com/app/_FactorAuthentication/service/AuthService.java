package com.app._FactorAuthentication.service;
import com.app._FactorAuthentication.entity.Otp;
import com.app._FactorAuthentication.repository.OtpRepository;
import com.app._FactorAuthentication.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OtpRepository otpRepository;

    @Autowired
    private EmailService emailService;

    private static final int OTP_VALIDITY_MINUTES = 5;

    public boolean checkUserExists(String email) {
        return userRepository.existsByEmail(email);
    }

    @Transactional
    public void generateAndSendOtp(String email) {
        String otp = String.valueOf(new Random().nextInt(900000) + 100000);

        otpRepository.deleteByEmail(email);

        Otp otpEntity = new Otp();
        otpEntity.setEmail(email);
        otpEntity.setOtp(otp);
        otpEntity.setExpiryTime(LocalDateTime.now().plusMinutes(OTP_VALIDITY_MINUTES));
        otpRepository.save(otpEntity);

        emailService.sendOtpEmail(email, otp);
    }
    @Transactional
    public boolean verifyOtp(String email, String otp) {
        Optional<Otp> storedOtp = otpRepository.findByEmailAndOtp(email, otp);

        if (storedOtp.isPresent() && storedOtp.get().getExpiryTime().isAfter(LocalDateTime.now())) {
            otpRepository.deleteByEmail(email);
            return true;
        }
        return false;
    }

    @Scheduled(fixedRate = 60000) // Runs every 1 minute
    @Transactional
    public void deleteStaleOtps() {
        otpRepository.deleteByExpiryTimeBefore(LocalDateTime.now());
    }
}