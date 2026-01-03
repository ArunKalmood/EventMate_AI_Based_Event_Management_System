package com.springboard.eventmate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.springboard.eventmate.model.TestEntity;

public interface TestRepository extends JpaRepository<TestEntity, Long> {
}

