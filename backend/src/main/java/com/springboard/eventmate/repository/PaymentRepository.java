package com.springboard.eventmate.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboard.eventmate.model.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByPaypalOrderId(String paypalOrderId);
}
