package com.logistics.delivery;

import com.logistics.delivery.context.RoutePlanner;
import com.logistics.delivery.model.DeliveryPriority;
import com.logistics.delivery.model.DeliveryRequest;
import com.logistics.delivery.model.Location;
import com.logistics.delivery.model.Route;
import com.logistics.delivery.selector.StrategySelector;
import com.logistics.delivery.strategy.RouteStrategy;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Route> routes = List.of(
                new Route("Route A", new Location("A", 55.75, 37.61), new Location("B", 55.76, 37.62), 15.0, 30, 500.0, 2.5, true, 5.0),
                new Route("Route B", new Location("A", 55.75, 37.61), new Location("B", 55.76, 37.62), 12.5, 25, 650.0, 2.0, true, 2.0),
                new Route("Route C", new Location("A", 55.75, 37.61), new Location("B", 55.76, 37.62), 18.0, 40, 400.0, 1.5, true, 8.0),
                new Route("Route D", new Location("A", 55.75, 37.61), new Location("B", 55.76, 37.62), 10.0, 35, 300.0, 3.0, true, 10.0),
                new Route("Route E", new Location("A", 55.75, 37.61), new Location("B", 55.76, 37.62), 22.0, 50, 800.0, 4.0, false, 5.0),
                new Route("Route F", new Location("A", 55.75, 37.61), new Location("B", 55.76, 37.62), 14.0, 28, 550.0, 2.2, true, 3.0),
                new Route("Route G", new Location("A", 55.75, 37.61), new Location("B", 55.76, 37.62), 16.0, 32, 450.0, 1.8, true, 4.0)
        );

        DeliveryRequest request = new DeliveryRequest(DeliveryPriority.URGENT, 1000.0, 60, false, false);

        RouteStrategy selectedStrategy = StrategySelector.selectStrategy(request.priority());
        RoutePlanner planner = new RoutePlanner(selectedStrategy);

        Route bestRoute = planner.plan(routes, request);

        System.out.println("Strategy: " + planner.getStrategy().getStrategyName());
        System.out.println("Selected Route: " + bestRoute.id());
    }
}