package com.springboard.eventmate.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboard.eventmate.model.UserActivity;

public interface UserActivityRepository extends JpaRepository<UserActivity, Long> {
	
	List<UserActivity> findByUserId(Long userId);

}



