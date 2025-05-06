package ru.sorokin.gateway.prometheus;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Component
public class UniqueUserMetrics {
    private final Set<String> uniqueUserIds = ConcurrentHashMap.newKeySet();
    private final MeterRegistry meterRegistry;

    public UniqueUserMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        // Регистрируем метрику
        Gauge.builder("daily_unique_users", uniqueUserIds, Set::size)
                .register(meterRegistry);

        // Очистка старых данных каждые 24 часа
        Executors.newSingleThreadScheduledExecutor()
                .scheduleAtFixedRate(this::clearOldData, 24, 24, TimeUnit.HOURS);
    }

    public void trackUser(String userId) {
        uniqueUserIds.add(userId);
    }

    private void clearOldData() {
        uniqueUserIds.clear();
    }

    public int getCurrentCount() {
        return uniqueUserIds.size();
    }
}
