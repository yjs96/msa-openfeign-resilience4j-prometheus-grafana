package com.example.inventoryservice.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/api/inventory")
@Slf4j
public class InventoryController {
    private final Random random = new Random();
    private AtomicInteger callCount = new AtomicInteger(0);

    @GetMapping("/{productId}")
    public boolean checkStock(@PathVariable String productId) {
        log.info("정보: INVEN: Check stock for productId {}", productId);

        int count = callCount.incrementAndGet();

        if (random.nextInt(10) > 7) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        if (random.nextInt(10) > 8) {
            throw new RuntimeException("런타임: Random inventory check failure");
        }

        log.info("정보: Checking stock for productId {}, call count: {}", productId, count);
        return true;
    }
}
