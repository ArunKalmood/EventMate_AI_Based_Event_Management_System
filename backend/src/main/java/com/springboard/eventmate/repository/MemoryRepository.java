package com.springboard.eventmate.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboard.eventmate.model.Memory;

public interface MemoryRepository extends JpaRepository<Memory, Long> {

    List<Memory> findByEventIdOrderByCreatedAtDesc(Long eventId);

    List<Memory> findByUserIdOrderByCreatedAtDesc(Long userId);
}
