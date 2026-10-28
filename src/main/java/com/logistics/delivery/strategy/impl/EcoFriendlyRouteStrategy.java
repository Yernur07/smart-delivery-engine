package com.logistics.delivery.strategy.impl;

import com.logistics.delivery.exception.NoSuitableRouteException;
import com.logistics.delivery.model.DeliveryRequest;
import com.logistics.delivery.model.Route;
import com.logistics.delivery.strategy.RouteStrategy;
import com.logistics.delivery.strategy.RouteValidator;

import java.util.List;

public class EcoFriendlyRouteStrategy implements RouteStrategy {
    private static final double EMISSION_WEIGHT = 0.7;
    private static final double DISTANCE_WEIGHT = 0.3;

    @Override
    public Route selectRoute(List<Route> routes, DeliveryRequest request) {
        return routes.stream()
                .filter(r -> RouteValidator.satisfiesConstraints(r, request))
                .min((r1, r2) -> {
                    double score1 = r1.carbonEmission() * EMISSION_WEIGHT + r1.distanceKm() * DISTANCE_WEIGHT;
                    double score2 = r2.carbonEmission() * EMISSION_WEIGHT + r2.distanceKm() * DISTANCE_WEIGHT;
                    return Double.compare(score1, score2);
                })
                .orElseThrow(() -> new NoSuitableRouteException("No route satisfies constraints for Eco Strategy."));
    }

    @Override
    public String getStrategyName() {
        return "Eco-Friendly Strategy";
    }
}