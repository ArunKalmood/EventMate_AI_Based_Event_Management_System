package com.springboard.eventmate.service;

import com.springboard.eventmate.model.dto.SignupRequest;
import com.springboard.eventmate.model.dto.LoginRequest;

public interface AuthService {

    void signup(SignupRequest request);

    String login(LoginRequest request);   // for EV-21 (Login)
    
    String upgradeToOrganizer(String email);

}
