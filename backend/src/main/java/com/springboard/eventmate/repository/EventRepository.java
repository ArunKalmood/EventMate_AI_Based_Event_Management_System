package com.springboard.eventmate.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.springboard.eventmate.model.Event;
import com.springboard.eventmate.model.User;

import jakarta.persistence.LockModeType;

public interface EventRepository extends JpaRepository<Event, Long> {

    // Existing (do NOT remove)
    List<Event> findByCreatedBy(User createdBy);

    //🔹 Search by title OR location
    @Query("""
        SELECT e FROM Event e
        WHERE e.status = 'ACTIVE'
          AND (
              LOWER(e.title) LIKE LOWER(CONCAT('%', :q, '%'))
              OR LOWER(e.location) LIKE LOWER(CONCAT('%', :q, '%'))
          )
    """)
    Page<Event> searchByTitleOrLocation(
            @Param("q") String q,
            Pageable pageable
    );

    //  FIXED: Category-safe search
    @Query("""
        SELECT e FROM Event e
        WHERE e.status = 'ACTIVE'
          AND (
              LOWER(e.title) LIKE LOWER(CONCAT('%', :q, '%'))
              OR LOWER(e.location) LIKE LOWER(CONCAT('%', :q, '%'))
          )
          AND e.category IS NOT NULL
          AND UPPER(e.category) = :category
    """)
    Page<Event> searchByTitleOrLocationAndCategory(
            @Param("q") String q,
            @Param("category") String category,
            Pageable pageable
    );

    // EV-262 — LOCK event row for atomic booking
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT e FROM Event e WHERE e.id = :id")
    Optional<Event> findByIdForUpdate(@Param("id") Long id);

    Page<Event> findByCreatedBy(User createdBy, Pageable pageable);

    Page<Event> findByCreatedById(Long organizerId, Pageable pageable);
    
    @Query("""
    	    SELECT e FROM Event e
    	    WHERE e.status = 'ACTIVE'
    	      AND e.category IN :categories
    	      AND e.date >= CURRENT_DATE
    	""")
    	List<Event> findUpcomingByCategories(
    	        @Param("categories") List<String> categories
    	);

}
