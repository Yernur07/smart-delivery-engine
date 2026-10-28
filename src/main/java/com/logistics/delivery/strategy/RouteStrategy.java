package com.logistics.delivery.strategy;

import com.logistics.delivery.model.DeliveryRequest;
import com.logistics.delivery.model.Route;

import java.util.List;

public interface RouteStrategy {
    Route selectRoute(List<Route> routes, DeliveryRequest request);
    String getStrategyName();
}