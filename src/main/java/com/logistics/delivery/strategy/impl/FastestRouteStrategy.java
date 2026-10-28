package com.logistics.delivery.strategy.impl;

import com.logistics.delivery.exception.NoSuitableRouteException;
import com.logistics.delivery.model.DeliveryRequest;
import com.logistics.delivery.model.Route;
import com.logistics.delivery.strategy.RouteStrategy;
import com.logistics.delivery.strategy.RouteValidator;

import java.util.List;

public class FastestRouteStrategy implements RouteStrategy {

    @Override
    public Route selectRoute(List<Route> routes, DeliveryRequest request) {
        return routes.stream()
                .filter(r -> RouteValidator.satisfiesConstraints(r, request))
                .min((r1, r2) -> {
                    double time1 = r1.estimatedMinutes() + r1.trafficPenaltyMinutes();
                    double time2 = r2.estimatedMinutes() + r2.trafficPenaltyMinutes();
                    return Double.compare(time1, time2);
                })
                .orElseThrow(() -> new NoSuitableRouteException("No route satisfies constraints for Fastest Strategy."));
    }

    @Override
    public String getStrategyName() {
        return "Fastest Strategy";
    }
}