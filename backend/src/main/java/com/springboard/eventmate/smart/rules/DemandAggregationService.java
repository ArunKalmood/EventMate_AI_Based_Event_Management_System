package com.springboard.eventmate.smart.rules;

import org.springframework.stereotype.Service;

import com.springboard.eventmate.model.Event;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DemandAggregationService {

    private final DemandClassificationRuleService demandClassificationRuleService;
    private final DemandMetricsService demandMetricsService;

    /**
     * EV-386
     * Aggregates demand classification output into a reusable structure.
     * Read-only. No DB writes.
     */
    public DemandClassificationResult aggregate(
            DemandMetrics demandMetrics,
            CapacityRuleResult capacityRuleResult
    ) {

        DemandState demandState =
                demandClassificationRuleService.classify(
                        demandMetrics,
                        capacityRuleResult
                );

        CapacityRiskLevel riskLevel =
                capacityRuleResult != null
                        ? capacityRuleResult.getCapacityRiskLevel()
                        : CapacityRiskLevel.NORMAL;

        boolean overcrowdingRisk =
                capacityRuleResult != null && capacityRuleResult.isOvercrowdingRisk();

        return new DemandClassificationResult(
                demandState,
                riskLevel,
                overcrowdingRisk,
                demandMetrics,
                capacityRuleResult
        );
    }
    
    /**
     * EV-389
     * Orchestrates full demand evaluation for an event.
     * Read-only. No DB writes.
     */
    public DemandClassificationResult evaluate(Event event) {

        if (event == null) {
            return null;
        }

        // 1. Compute demand metrics
        DemandMetrics demandMetrics =
                demandMetricsService.getDemandMetrics(event);

        // 2. Evaluate capacity rules
        CapacityRuleResult capacityRuleResult =
                demandMetricsService.evaluateCapacityRules(event);

        // 3. Aggregate final result (EV-386)
        return aggregate(demandMetrics, capacityRuleResult);
    }

}
