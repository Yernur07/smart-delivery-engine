package com.logistics.delivery.model;

public record Location(
        String name,
        double latitude,
        double longitude
) {}