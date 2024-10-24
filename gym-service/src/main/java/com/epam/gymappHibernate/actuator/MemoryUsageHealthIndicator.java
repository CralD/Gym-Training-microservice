package com.epam.gymappHibernate.actuator;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class MemoryUsageHealthIndicator implements HealthIndicator {

    private static final long THRESHOLD = 100000000L;

    @Override
    public Health health() {
        long freeMemory = Runtime.getRuntime().freeMemory();
        long totalMemory = Runtime.getRuntime().totalMemory();

        if (freeMemory >= THRESHOLD) {
            return Health.up()
                    .withDetail("Free Memory", freeMemory)
                    .withDetail("Total Memory", totalMemory)
                    .build();
        } else {
            return Health.down()
                    .withDetail("Free Memory", freeMemory)
                    .withDetail("Total Memory", totalMemory)
                    .build();
        }
    }

}