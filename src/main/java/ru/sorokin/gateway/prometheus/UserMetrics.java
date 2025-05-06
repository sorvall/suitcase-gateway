package ru.sorokin.gateway.prometheus;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class UserMetrics {
    private final AtomicInteger dailyUsers = new AtomicInteger(0);
    private final MeterRegistry meterRegistry;

    public UserMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        meterRegistry.gauge("daily_active_users", dailyUsers);
    }

    public void incrementUserCount() {
        dailyUsers.incrementAndGet();
    }

    public void resetDailyCounter() {
        dailyUsers.set(0);
    }
}