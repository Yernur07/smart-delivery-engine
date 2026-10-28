# Smart Delivery & Route Optimization Engine

## Project Overview
This project implements a **Smart Delivery & Route Optimization Engine** in Java 21, designed for logistics platforms delivering thousands of orders daily. It employs the **Strategy Pattern** to decouple route selection algorithms (optimizing for speed, cost, emissions, distance, or a balanced multi-criteria score) from the core routing logic (`RoutePlanner`).

---

## Core Questions & Answers

### 1. What problem does Strategy solve in this project?
Without Strategy, switching route optimization algorithms would require complex conditionals (`if-else` or `switch-case`) spread throughout the domain logic. Strategy encapsulates each optimization algorithm into its own class, making algorithms interchangeable and independent of the client (`RoutePlanner`).

### 2. Where is the Context?
`com.logistics.delivery.context.RoutePlanner` acts as the Context. It holds a reference to a `RouteStrategy` abstraction and delegates route selection to it.

### 3. Where is the Strategy interface?
`com.logistics.delivery.strategy.RouteStrategy` defines the contract for all algorithms (`selectRoute` and `getStrategyName`).

### 4. What are the Concrete Strategies?
- **FastestRouteStrategy**: Optimizes for total estimated delivery time including traffic delays.
- **CheapestRouteStrategy**: Optimizes for minimum total monetary cost.
- **EcoFriendlyRouteStrategy**: Optimizes for minimum carbon footprint ($CO_2$) and distance.
- **BalancedRouteStrategy**: Multi-criteria weighted optimization normalized across time, cost, distance, and emissions (weights sum to 1.0).
- **DistanceFirstStrategy**: Optimizes purely for minimal physical travel distance.

### 5. Where is runtime strategy switching demonstrated?
In `RoutePlanner.java` via `setStrategy(RouteStrategy newStrategy)` and tested in `DeliveryEngineTest.java`:
```java
RoutePlanner planner = new RoutePlanner(new FastestRouteStrategy());
planner.plan(routes, request);

// Switching strategy at runtime
planner.setStrategy(new EcoFriendlyRouteStrategy());
planner.plan(routes, request);
6. Why is this Strategy rather than simple if/else?
if/else violates the Open/Closed Principle (OCP) and Single Responsibility Principle (SRP). Every time a new algorithm is added or an existing one is modified, the monolith class must be modified, increasing risk and coupling. Strategy allows adding algorithms simply by implementing a new class.

7. How can a new Strategy be added?
Create a class implementing RouteStrategy in strategy.impl.

Implement selectRoute(...) and getStrategyName().

Pass the new strategy instance to RoutePlanner without modifying any existing classes.

8. What is the difference between Strategy and Bridge?
Strategy focuses on varying behavior/algorithms dynamically at runtime.

Bridge focuses on decoupling an abstraction from its implementation hierarchy at compile-time so both can vary independently.

9. Which SOLID principles are demonstrated?
SRP (Single Responsibility Principle): RoutePlanner handles context routing, RouteValidator handles restriction checks, and concrete strategy classes handle specific mathematical optimizations.

OCP (Open/Closed Principle): New strategies (e.g., DistanceFirstStrategy) can be added without modifying RoutePlanner or other strategies.

DIP (Dependency Inversion Principle): High-level RoutePlanner depends on the RouteStrategy abstraction, not on concrete strategy classes.

10. What limitations does your current architecture have?
Hard constraints validation (RouteValidator) is currently static and bound to satisfiesConstraints. For complex dynamic validation pipelines, a Specification or Chain of Responsibility pattern could be integrated.