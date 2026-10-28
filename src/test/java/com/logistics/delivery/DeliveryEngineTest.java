package com.logistics.delivery;

import com.logistics.delivery.context.RoutePlanner;
import com.logistics.delivery.exception.NoSuitableRouteException;
import com.logistics.delivery.model.*;
import com.logistics.delivery.selector.StrategySelector;
import com.logistics.delivery.strategy.impl.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DeliveryEngineTest {

    private List<Route> routes;

    @BeforeEach
    void setUp() {
        routes = List.of(
                new Route("Route A", new Location("A", 55.75, 37.61), new Location("B", 55.76, 37.62), 15.0, 30, 500.0, 2.5, true, 5.0),
                new Route("Route B", new Location("A", 55.75, 37.61), new Location("B", 55.76, 37.62), 12.5, 25, 650.0, 2.0, true, 2.0),
                new Route("Route C", new Location("A", 55.75, 37.61), new Location("B", 55.76, 37.62), 18.0, 40, 400.0, 1.5, true, 8.0)
        );
    }

    @Test
    void testFastestStrategy() {
        RoutePlanner planner = new RoutePlanner(new FastestRouteStrategy());
        DeliveryRequest request = new DeliveryRequest(DeliveryPriority.URGENT, 1000.0, 60, false, false);
        Route result = planner.plan(routes, request);
        assertEquals("Route B", result.id());
    }

    @Test
    void testCheapestStrategy() {
        RoutePlanner planner = new RoutePlanner(new CheapestRouteStrategy());
        DeliveryRequest request = new DeliveryRequest(DeliveryPriority.ECONOMY, 1000.0, 60, false, false);
        Route result = planner.plan(routes, request);
        assertEquals("Route C", result.id());
    }

    @Test
    void testEcoStrategy() {
        RoutePlanner planner = new RoutePlanner(new EcoFriendlyRouteStrategy());
        DeliveryRequest request = new DeliveryRequest(DeliveryPriority.ECO, 1000.0, 60, true, false);
        Route result = planner.plan(routes, request);
        assertEquals("Route C", result.id());
    }

    @Test
    void testBalancedStrategyWeights() {
        assertDoesNotThrow(() -> new BalancedRouteStrategy(0.4, 0.3, 0.1, 0.2));
        assertThrows(IllegalArgumentException.class, () -> new BalancedRouteStrategy(0.5, 0.5, 0.5, 0.5));
    }

    @Test
    void testRuntimeStrategySwitching() {
        RoutePlanner planner = new RoutePlanner(new FastestRouteStrategy());
        assertEquals("Fastest Strategy", planner.getStrategy().getStrategyName());

        planner.setStrategy(new CheapestRouteStrategy());
        assertEquals("Cheapest Strategy", planner.getStrategy().getStrategyName());
    }

    @Test
    void testAutomaticStrategySelector() {
        var strategy = StrategySelector.selectStrategy(DeliveryPriority.URGENT);
        assertInstanceOf(FastestRouteStrategy.class, strategy);
    }

    @Test
    void testHardConstraintsBudgetExceeded() {
        RoutePlanner planner = new RoutePlanner(new CheapestRouteStrategy());
        // Бюджет слишком мал для всех маршрутов
        DeliveryRequest request = new DeliveryRequest(DeliveryPriority.ECONOMY, 100.0, 60, false, false);
        assertThrows(NoSuitableRouteException.class, () -> planner.plan(routes, request));
    }

    @Test
    void testNoSuitableRouteException() {
        RoutePlanner planner = new RoutePlanner(new FastestRouteStrategy());
        // Дедлайн слишком жесткий
        DeliveryRequest request = new DeliveryRequest(DeliveryPriority.URGENT, 1000.0, 5, false, false);
        assertThrows(NoSuitableRouteException.class, () -> planner.plan(routes, request));
    }
}