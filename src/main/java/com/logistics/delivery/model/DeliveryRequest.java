package com.logistics.delivery.model;

public record DeliveryRequest(
        DeliveryPriority priority,
        double maxBudget,
        int maxDeliveryMinutes,
        boolean ecoPreferred,
        boolean isProhibited
) {}