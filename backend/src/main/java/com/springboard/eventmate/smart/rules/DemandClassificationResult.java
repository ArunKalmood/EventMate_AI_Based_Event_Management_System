package com.springboard.eventmate.smart.rules;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DemandClassificationResult {

    private final DemandState demandState;
    private final CapacityRiskLevel capacityRiskLevel;
    private final boolean overcrowdingRisk;

    // Optional / read-only metrics
    private final DemandMetrics demandMetrics;
    private final CapacityRuleResult capacityRuleResult;
}
