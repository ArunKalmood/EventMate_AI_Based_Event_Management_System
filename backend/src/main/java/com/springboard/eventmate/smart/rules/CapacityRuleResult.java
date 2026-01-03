package com.springboard.eventmate.smart.rules;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CapacityRuleResult {

    private final CapacityRiskLevel capacityRiskLevel;
    private final int remainingSeats;
    private final boolean overcrowdingRisk;
}
