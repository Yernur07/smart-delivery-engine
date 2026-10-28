package com.logistics.delivery.selector;

import com.logistics.delivery.model.DeliveryPriority;
import com.logistics.delivery.strategy.RouteStrategy;
import com.logistics.delivery.strategy.impl.BalancedRouteStrategy;
import com.logistics.delivery.strategy.impl.CheapestRouteStrategy;
import com.logistics.delivery.strategy.impl.EcoFriendlyRouteStrategy;
import com.logistics.delivery.strategy.impl.FastestRouteStrategy;

public class StrategySelector {
    public static RouteStrategy selectStrategy(DeliveryPriority priority) {
        return switch (priority) {
            case URGENT -> new FastestRouteStrategy();
            case ECONOMY -> new CheapestRouteStrategy();
            case ECO -> new EcoFriendlyRouteStrategy();
            case STANDARD -> new BalancedRouteStrategy(0.4, 0.3, 0.1, 0.2);
        };
    }
}