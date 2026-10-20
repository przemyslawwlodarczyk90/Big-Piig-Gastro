package com.example.projektsklep.service;

import com.example.projektsklep.model.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Clock;
import java.time.Duration;
import java.util.random.RandomGenerator;

@Service
public class DemoInventoryService {
    private final ProductRepository products;
    private final Clock clock;
    private final RandomGenerator random;

    public DemoInventoryService(ProductRepository products, Clock clock, RandomGenerator random) {
        this.products = products;
        this.clock = clock;
        this.random = random;
    }

    @Transactional
    public void replenish() {
        var now = clock.instant();
        var catalog = products.findAllForUpdate();
        for (var product : catalog) {
            if (product.getStockQuantity() < 5) {
                product.setStockQuantity(product.getStockQuantity() + random.nextInt(10, 31));
            }
        }
        // A supplier delay hides stock temporarily without destroying existing units.
        if (catalog.size() > 1 && catalog.stream().noneMatch(p -> p.availableStock(now) == 0)) {
            var candidates = catalog.stream().filter(p -> p.getUnavailableUntil() == null).toList();
            if (candidates.isEmpty()) {
                catalog.forEach(p -> p.setUnavailableUntil(null));
                candidates = catalog;
            }
            candidates.get(random.nextInt(candidates.size()))
                    .setUnavailableUntil(now.plus(Duration.ofMinutes(random.nextInt(2, 6))));
        }
    }
}
