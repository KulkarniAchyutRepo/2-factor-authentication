package com.app._FactorAuthentication.service;

import com.app._FactorAuthentication.entity.Otp;
import com.app._FactorAuthentication.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class AuthService {
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private EmailService emailService;

    private static final int OTP_VALIDITY_MINUTES = 5;

    @Transactional
    public boolean checkUserExists(String email) {
        long userCount = (long)entityManager.createNativeQuery("select count(*) from users where email=:email")
                .setParameter("email", email)
                .getSingleResult();
        return userCount > 0;
    }

    @Transactional
    public void generateAndSendOtp(String email) {
        // Generate OTP
        String otp = String.valueOf(new Random().nextInt(900000) + 100000);

        // Remove existing OTP for the email
        entityManager.createQuery("DELETE FROM Otp o WHERE o.email = :email")
                .setParameter("email", email)
                .executeUpdate();

        // Save new OTP
        Otp otpEntity = new Otp();
        otpEntity.setEmail(email);
        otpEntity.setOtp(otp);
        otpEntity.setExpiryTime(LocalDateTime.now().plusMinutes(OTP_VALIDITY_MINUTES));
        entityManager.persist(otpEntity);

        // Send OTP via email
        emailService.sendOtpEmail(email, otp);
    }

    @Transactional
    public boolean verifyOtp(String email, String otp) {
        Otp storedOtp = entityManager.createQuery("SELECT o FROM Otp o WHERE o.email = :email AND o.otp = :otp", Otp.class)
                .setParameter("email", email)
                .setParameter("otp", otp)
                .getResultStream()
                .findFirst()
                .orElse(null);

        if (storedOtp != null && storedOtp.getExpiryTime().isAfter(LocalDateTime.now())) {
            entityManager.createQuery("DELETE FROM Otp o WHERE o.email = :email")
                    .setParameter("email", email)
                    .executeUpdate();
            return true;
        }
        return false;
    }
}
