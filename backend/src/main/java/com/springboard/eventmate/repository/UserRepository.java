package com.springboard.eventmate.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboard.eventmate.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
}
