package com.logistics.delivery.model;

public record Route(
        String id,
        Location origin,
        Location destination,
        double distanceKm,
        int estimatedMinutes,
        double cost,
        double carbonEmission,
        boolean isAvailable,
        double trafficPenaltyMinutes
) {}