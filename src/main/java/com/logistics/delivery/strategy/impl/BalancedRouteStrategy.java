package com.logistics.delivery.strategy.impl;

import com.logistics.delivery.exception.NoSuitableRouteException;
import com.logistics.delivery.model.DeliveryRequest;
import com.logistics.delivery.model.Route;
import com.logistics.delivery.strategy.RouteStrategy;
import com.logistics.delivery.strategy.RouteValidator;

import java.util.List;

public class BalancedRouteStrategy implements RouteStrategy {
    private final double timeWeight;
    private final double costWeight;
    private final double distanceWeight;
    private final double emissionWeight;

    public BalancedRouteStrategy(double timeWeight, double costWeight, double distanceWeight, double emissionWeight) {
        if (Math.abs(timeWeight + costWeight + distanceWeight + emissionWeight - 1.0) > 1e-6) {
            throw new IllegalArgumentException("Sum of balanced strategy weights must be exactly 1.0");
        }
        this.timeWeight = timeWeight;
        this.costWeight = costWeight;
        this.distanceWeight = distanceWeight;
        this.emissionWeight = emissionWeight;
    }

    @Override
    public Route selectRoute(List<Route> routes, DeliveryRequest request) {
        List<Route> validRoutes = routes.stream()
                .filter(r -> RouteValidator.satisfiesConstraints(r, request))
                .toList();

        if (validRoutes.isEmpty()) {
            throw new NoSuitableRouteException("No route satisfies constraints for Balanced Strategy.");
        }

        double maxTime = validRoutes.stream().mapToDouble(r -> r.estimatedMinutes() + r.trafficPenaltyMinutes()).max().orElse(1.0);
        double maxCost = validRoutes.stream().mapToDouble(Route::cost).max().orElse(1.0);
        double maxDistance = validRoutes.stream().mapToDouble(Route::distanceKm).max().orElse(1.0);
        double maxEmission = validRoutes.stream().mapToDouble(Route::carbonEmission).max().orElse(1.0);

        Route bestRoute = null;
        double minScore = Double.MAX_VALUE;

        for (Route r : validRoutes) {
            double normTime = (r.estimatedMinutes() + r.trafficPenaltyMinutes()) / maxTime;
            double normCost = r.cost() / maxCost;
            double normDistance = r.distanceKm() / maxDistance;
            double normEmission = r.carbonEmission() / maxEmission;

            double score = normTime * timeWeight
                    + normCost * costWeight
                    + normDistance * distanceWeight
                    + normEmission * emissionWeight;

            if (score < minScore) {
                minScore = score;
                bestRoute = r;
            }
        }

        return bestRoute;
    }

    @Override
    public String getStrategyName() {
        return "Balanced Strategy";
    }
}