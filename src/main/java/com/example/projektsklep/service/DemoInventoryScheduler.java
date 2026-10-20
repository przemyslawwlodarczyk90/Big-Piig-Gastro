package com.example.projektsklep.service;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "app.inventory.demo-enabled", havingValue = "true")
public class DemoInventoryScheduler {
    private final DemoInventoryService inventory;

    public DemoInventoryScheduler(DemoInventoryService inventory) { this.inventory = inventory; }

    @Scheduled(fixedDelayString = "${app.inventory.interval-ms:60000}", initialDelayString = "${app.inventory.initial-delay-ms:10000}")
    public void tick() { inventory.replenish(); }
}
