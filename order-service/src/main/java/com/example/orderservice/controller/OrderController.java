package com.example.orderservice.controller;

import com.example.orderservice.client.InventoryClient;
import com.example.orderservice.dto.OrderDto;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@Slf4j
public class OrderController {
    private final InventoryClient inventoryClient;

    public OrderController(InventoryClient inventoryClient) {
        this.inventoryClient = inventoryClient;
    }


    @PostMapping
    @CircuitBreaker(name = "inventoryService", fallbackMethod = "fallbackCreateOrder")
    @Retry(name = "inventoryService", fallbackMethod = "fallbackCreateOrder")
    @RateLimiter(name = "inventoryService")
    @Bulkhead(name = "inventoryService")
    public String createOrder(@RequestBody OrderDto orderDto) {
        log.info("정보: ORDER: Received, Checking productId: {}", orderDto.getProductId());
        boolean hasStock = inventoryClient.checkStock(orderDto.getProductId());
        if (hasStock) {
            log.info("정보: ORDER: Stock Available, Creating...");
            return "Order Created Successfully\n";
        }
        return "No Stock Available\n";
    }

    public String fallbackCreateOrder(OrderDto orderDto, Throwable ex) {
        log.error("에러: Error Creating order for product: {}", orderDto.getProductId(), ex);
        return "Unable to create order\n";
    }
}
