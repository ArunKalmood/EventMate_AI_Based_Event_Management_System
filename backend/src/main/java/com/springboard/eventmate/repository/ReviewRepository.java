package com.springboard.eventmate.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.springboard.eventmate.model.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Optional<Review> findByEventIdAndUserIdAndIsDeletedFalse(Long eventId, Long userId);

    Page<Review> findByEventIdAndIsDeletedFalseOrderByCreatedAtDesc(
        Long eventId,
        Pageable pageable
    );

    Page<Review> findByIsDeletedFalseOrderByCreatedAtDesc(Pageable pageable);
    
    @Query("""
    	    SELECT
    	        AVG(r.rating),
    	        COUNT(r),
    	        AVG(CASE WHEN r.sentimentScore > 0 THEN 1 ELSE 0 END)
    	    FROM Review r
    	    WHERE r.event.id = :eventId
    	      AND r.isDeleted = false
    	""")
    	Object[] calculateStats(@Param("eventId") Long eventId);
    
    @Query("""
    	    SELECT r FROM Review r
    	    JOIN FETCH r.event
    	    JOIN FETCH r.user
    	    WHERE r.isDeleted = false
    	    ORDER BY r.createdAt DESC
    	""")
    	Page<Review> findLatestWithEventAndUser(Pageable pageable);


}
