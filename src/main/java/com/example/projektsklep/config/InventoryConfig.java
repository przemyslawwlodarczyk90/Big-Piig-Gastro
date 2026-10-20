package com.example.projektsklep.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import java.time.Clock;
import java.util.Random;
import java.util.random.RandomGenerator;

@Configuration
@EnableScheduling
public class InventoryConfig {
    @Bean
    public Clock clock() { return Clock.systemUTC(); }

    @Bean
    public RandomGenerator inventoryRandom() { return new Random(); }
}
