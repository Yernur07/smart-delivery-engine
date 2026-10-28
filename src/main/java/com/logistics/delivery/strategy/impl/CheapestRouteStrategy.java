package com.logistics.delivery.strategy.impl;

import com.logistics.delivery.exception.NoSuitableRouteException;
import com.logistics.delivery.model.DeliveryRequest;
import com.logistics.delivery.model.Route;
import com.logistics.delivery.strategy.RouteStrategy;
import com.logistics.delivery.strategy.RouteValidator;

import java.util.Comparator;
import java.util.List;

public class CheapestRouteStrategy implements RouteStrategy {

    @Override
    public Route selectRoute(List<Route> routes, DeliveryRequest request) {
        return routes.stream()
                .filter(r -> RouteValidator.satisfiesConstraints(r, request))
                .min(Comparator.comparingDouble(Route::cost))
                .orElseThrow(() -> new NoSuitableRouteException("No route satisfies constraints for Cheapest Strategy."));
    }

    @Override
    public String getStrategyName() {
        return "Cheapest Strategy";
    }
}