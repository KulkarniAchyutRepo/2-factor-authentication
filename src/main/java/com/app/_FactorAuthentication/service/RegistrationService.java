package com.app._FactorAuthentication.service;

import com.app._FactorAuthentication.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {
    @Autowired
    private EntityManager entityManager;

    @Transactional
    public String registerUser(String email, String name, String password) {
        // Check if the user already exists
        long userCount = entityManager.createQuery("SELECT COUNT(u) FROM User u WHERE u.email = :email", Long.class)
                .setParameter("email", email)
                .getSingleResult();

        if (userCount > 0) {
            return "User with this email already exists.";
        }

        // Save the new user
        User newUser = new User();
        newUser.setEmail(email);
        newUser.setName(name);
        newUser.setPassword(password); // Note: Ideally, the password should be hashed
        entityManager.persist(newUser);

        return "User registered successfully.";
    }
}
