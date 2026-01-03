package com.springboard.eventmate.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.springboard.eventmate.model.Role;
import com.springboard.eventmate.model.User;
import com.springboard.eventmate.model.dto.LoginRequest;
import com.springboard.eventmate.model.dto.SignupRequest;
import com.springboard.eventmate.repository.UserRepository;
import com.springboard.eventmate.service.AuthService;
import com.springboard.eventmate.util.JwtUtil;

@Service
public class AuthServiceImpl implements AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public AuthServiceImpl(PasswordEncoder passwordEncoder,
                           UserRepository userRepository,
                           JwtUtil jwtUtil) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void signup(SignupRequest request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        String hashedPassword = passwordEncoder.encode(request.getPassword());

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(hashedPassword);

        Role assignedRole = Role.USER; // default
        
        if (request.getRole() != null &&
            request.getRole().equalsIgnoreCase("ORGANIZER")) {
            assignedRole = Role.ORGANIZER;
        }

        user.setRole(assignedRole);

        userRepository.save(user);
    }


    @Override
    public String login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        //  IMPORTANT: TOKEN MUST INCLUDE ROLE 
        return jwtUtil.generateToken(
                user.getEmail(),           // subject
                user.getRole().name()      // add role claim
        );
    }
    
    @Override
    public String upgradeToOrganizer(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Idempotency safeguard
        if (user.getRole() == Role.ORGANIZER) {
            return jwtUtil.generateToken(
                    user.getEmail(),
                    user.getRole().name()
            );
        }

        user.setRole(Role.ORGANIZER);
        userRepository.save(user);

        // Issue NEW JWT with updated role
        return jwtUtil.generateToken(
                user.getEmail(),
                user.getRole().name()
        );
    }

}
