package com.logistics.delivery.strategy;

import com.logistics.delivery.model.DeliveryRequest;
import com.logistics.delivery.model.Route;

public class RouteValidator {
    public static boolean satisfiesConstraints(Route route, DeliveryRequest request) {
        if (!route.isAvailable()) {
            return false;
        }
        if (route.cost() > request.maxBudget()) {
            return false;
        }
        if (route.estimatedMinutes() + route.trafficPenaltyMinutes() > request.maxDeliveryMinutes()) {
            return false;
        }
        if (request.isProhibited()) {
            return false;
        }
        return true;
    }
}