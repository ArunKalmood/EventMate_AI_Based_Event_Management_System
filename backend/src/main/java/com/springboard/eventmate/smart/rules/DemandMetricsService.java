package com.springboard.eventmate.smart.rules;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.springboard.eventmate.model.Event;
import com.springboard.eventmate.model.enums.BookingStatus;
import com.springboard.eventmate.repository.BookingRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DemandMetricsService {

    private final BookingRepository bookingRepository;
    
    @Value("${smart.rules.booking.velocity.window.hours}")
    private int bookingVelocityWindowHours;
    
    @Value("${smart.rules.capacity.threshold.normal.max}")
    private int normalMaxThreshold;

    @Value("${smart.rules.capacity.threshold.warning.min}")
    private int warningMinThreshold;

    @Value("${smart.rules.capacity.threshold.high.min}")
    private int highRiskMinThreshold;
    
    @Value("${smart.rules.capacity.remaining.seats.low}")
    private int lowRemainingSeatsThreshold;




    /**
     * EV-371
     * Computes confirmed booking count for a given event.
     * Read-only, no side effects.
     */
    public long getConfirmedBookingCount(Event event) {
        if (event == null) {
            return 0;
        }

        return bookingRepository.countByEventAndStatus(
                event,
                BookingStatus.CONFIRMED
        );
    }
    
    /**
     * EV-372
     * Computes capacity utilization percentage for an event.
     * Formula: (confirmedBookings / capacity) * 100
     * Safe for edge cases.
     */
    public int getCapacityUtilizationPercentage(Event event) {
        if (event == null || event.getCapacity() == null || event.getCapacity() <= 0) {
            return 0;
        }

        long confirmedBookings = getConfirmedBookingCount(event);

        double utilization =
                ((double) confirmedBookings / event.getCapacity()) * 100;

        return (int) Math.round(utilization);
    }
    
  
    /**
     * EV-373
     * Computes booking velocity as the number of confirmed bookings
     * in the last configurable time window.
     */
    public long getBookingVelocity(Event event) {
        if (event == null) {
            return 0;
        }

        LocalDateTime cutoffTime =
                LocalDateTime.now().minusHours(bookingVelocityWindowHours);

        return bookingRepository.countByEventAndStatusAndBookedAtAfter(
                event,
                BookingStatus.CONFIRMED,
                cutoffTime
        );
    }
    
    /**
     * EV-374
     * Aggregates all demand metrics into a single internal structure.
     */
    public DemandMetrics getDemandMetrics(Event event) {
        if (event == null) {
            return new DemandMetrics(0, 0, 0);
        }

        long totalBookings = getConfirmedBookingCount(event);
        int capacityUsage = getCapacityUtilizationPercentage(event);
        long velocity = getBookingVelocity(event);

        return new DemandMetrics(
                totalBookings,
                capacityUsage,
                velocity
        );
    }
    
    /**
     * EV-378
     * Evaluates capacity usage percentage against configured thresholds
     * and determines capacity risk level.
     */
    public CapacityRiskLevel evaluateCapacityRisk(Event event) {
        if (event == null) {
            return CapacityRiskLevel.NORMAL;
        }

        int capacityUsage = getCapacityUtilizationPercentage(event);

        if (capacityUsage >= highRiskMinThreshold) {
            return CapacityRiskLevel.HIGH_RISK;
        }

        if (capacityUsage >= warningMinThreshold) {
            return CapacityRiskLevel.WARNING;
        }

        return CapacityRiskLevel.NORMAL;
    }
    
    /**
     * EV-379
     * Detects overcrowding risk based on capacity risk level
     * or very low remaining seats.
     */
    public boolean isOvercrowdingRisk(Event event) {
        if (event == null || event.getCapacity() == null) {
            return false;
        }

        // Condition 1: Capacity already in HIGH_RISK
        CapacityRiskLevel riskLevel = evaluateCapacityRisk(event);
        if (riskLevel == CapacityRiskLevel.HIGH_RISK) {
            return true;
        }

        // Condition 2: Very low remaining seats
        long confirmedBookings = getConfirmedBookingCount(event);
        int remainingSeats = event.getCapacity() - (int) confirmedBookings;

        return remainingSeats <= lowRemainingSeatsThreshold;
    }
    
    /**
     * EV-380
     * Aggregates capacity rule evaluation results into
     * a single internal structure.
     */
    public CapacityRuleResult evaluateCapacityRules(Event event) {
        if (event == null || event.getCapacity() == null) {
            return new CapacityRuleResult(
                    CapacityRiskLevel.NORMAL,
                    0,
                    false
            );
        }

        CapacityRiskLevel riskLevel = evaluateCapacityRisk(event);

        long confirmedBookings = getConfirmedBookingCount(event);
        int remainingSeats = event.getCapacity() - (int) confirmedBookings;

        boolean overcrowdingRisk = isOvercrowdingRisk(event);

        return new CapacityRuleResult(
                riskLevel,
                remainingSeats,
                overcrowdingRisk
        );
    }






}
