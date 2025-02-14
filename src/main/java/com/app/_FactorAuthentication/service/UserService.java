package com.app._FactorAuthentication.service;

import com.app._FactorAuthentication.entity.User;
import com.app._FactorAuthentication.exceptions.securityExceptions.ResourceNotFoundException;
import com.app._FactorAuthentication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public User update(User user){
        Optional<User> existingUser = userRepository.findById(user.getId());
        if(!existingUser.isPresent()){
            throw new UsernameNotFoundException("User does not exist.");
        }
        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(hashedPassword);
        return userRepository.save(user);
    }

    public List<User> findAll(){
        return  userRepository.findAll();
    }

    public boolean deleteById(long id){
        Optional<User> existingUser = userRepository.findById(id);
        if(!existingUser.isPresent()){
            throw new UsernameNotFoundException("User does not exist.");
        }
        userRepository.deleteById(id);
        return true;
    }
}
