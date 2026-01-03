package com.springboard.eventmate.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboard.eventmate.model.Event;
import com.springboard.eventmate.model.LostItem;
import com.springboard.eventmate.model.User;

public interface LostItemRepository extends JpaRepository<LostItem, Long> {

    List<LostItem> findByEvent(Event event);

    List<LostItem> findByReportedBy(User reportedBy);
    
    List<LostItem> findByEventIdOrderByCreatedAtDesc(Long eventId);
    
    List<LostItem> findByEventCreatedByIdOrderByCreatedAtDesc(Long organizerId);


}
