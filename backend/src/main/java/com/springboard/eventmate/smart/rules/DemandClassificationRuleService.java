package com.springboard.eventmate.smart.rules;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DemandClassificationRuleService {

    @Value("${smart.rules.capacity.remaining.seats.low}")
    private int lowRemainingSeatsThreshold;

    /**
     * EV-384
     * Determines DemandState using capacity & demand signals.
     * Pure rule evaluation. No DB. No side effects.
     */
    public DemandState classify(
            DemandMetrics demandMetrics,
            CapacityRuleResult capacityRuleResult
    ) {

        if (demandMetrics == null || capacityRuleResult == null) {
            return DemandState.NONE;
        }

        /* ---------- FILLING_FAST (highest priority) ---------- */
        if (capacityRuleResult.getCapacityRiskLevel() == CapacityRiskLevel.HIGH_RISK ||
            capacityRuleResult.getRemainingSeats() <= lowRemainingSeatsThreshold) {

            return DemandState.FILLING_FAST;
        }

        /* ---------- TRENDING ---------- */
        if (demandMetrics.getBookingVelocity() > 0 &&
            capacityRuleResult.getCapacityRiskLevel() == CapacityRiskLevel.WARNING) {

            return DemandState.TRENDING;
        }

        /* ---------- NONE ---------- */
        return DemandState.NONE;
    }
}
