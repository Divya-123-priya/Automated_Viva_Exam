package com.aivivasystem.viva.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.aivivasystem.viva.dto.RegisterRequest;
import com.aivivasystem.viva.model.User;
import com.aivivasystem.viva.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;  

    
    public User registerUser(RegisterRequest req) {
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new RuntimeException("Email already registered: " + req.getEmail());
        }
        User user = new User();
        user.setFullName(req.getFullName());                          
        user.setRegisterNumber(req.getRegisterNumber());              
        user.setEmail(req.getEmail());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setRole(req.getRole() != null ? req.getRole() : "STUDENT");
        return userRepository.save(user);
    }

    
    public User login(String email, String rawPassword) {
        User user = userRepository.findByEmail(email);
        if (user == null) return null;
        if (passwordEncoder.matches(rawPassword, user.getPassword())) {
            return user;
        }
        return null;
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
