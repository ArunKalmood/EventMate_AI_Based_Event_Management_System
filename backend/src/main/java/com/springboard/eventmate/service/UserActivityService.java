package com.springboard.eventmate.service;

import org.springframework.stereotype.Service;

import com.springboard.eventmate.model.UserActivity;
import com.springboard.eventmate.repository.UserActivityRepository;
import com.springboard.eventmate.repository.UserRepository;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class UserActivityService {

    private final UserActivityRepository repository;
    private final UserRepository userRepository; //  ADD

    public void log(String email,
                    Long eventId,
                    String actionType,
                    String actionValue) {

        if (email == null) return; // safety

        Long userId = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("User not found"))
                .getId();

        UserActivity ua = new UserActivity();
        ua.setUserId(userId);
        ua.setEventId(eventId);
        ua.setActionType(actionType);
        ua.setActionValue(actionValue);

        repository.save(ua);
    }
}

