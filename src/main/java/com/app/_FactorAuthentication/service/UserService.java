package com.app._FactorAuthentication.service;

import com.app._FactorAuthentication.entity.User;
import com.app._FactorAuthentication.exceptions.securityExceptions.ResourceNotFoundException;
import com.app._FactorAuthentication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user  = userRepository.findByEmail(username).orElseThrow(()->new ResourceNotFoundException("User not found."));
        UserDetails customUserDetails = new CustomUserDetails(user);
        return customUserDetails;
    }
}
