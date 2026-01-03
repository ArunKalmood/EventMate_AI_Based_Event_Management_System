package com.springboard.eventmate.smart.rules;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DemandMetrics {

    private final long totalBookings;
    private final int capacityUtilizationPercentage;
    private final long bookingVelocity;
}
