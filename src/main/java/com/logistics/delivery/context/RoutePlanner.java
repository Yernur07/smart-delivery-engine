package com.logistics.delivery.context;

import com.logistics.delivery.model.DeliveryRequest;
import com.logistics.delivery.model.Route;
import com.logistics.delivery.strategy.RouteStrategy;

import java.util.List;

public class RoutePlanner {
    private RouteStrategy strategy;

    public RoutePlanner(RouteStrategy strategy) {
        this.strategy = strategy;
    }

    public void setStrategy(RouteStrategy strategy) {
        this.strategy = strategy;
    }

    public RouteStrategy getStrategy() {
        return strategy;
    }

    public Route plan(List<Route> routes, DeliveryRequest request) {
        if (strategy == null) {
            throw new IllegalStateException("Strategy is not set!");
        }
        return strategy.selectRoute(routes, request);
    }
}