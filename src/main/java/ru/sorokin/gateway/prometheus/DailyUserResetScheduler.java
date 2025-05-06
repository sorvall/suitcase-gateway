package ru.sorokin.gateway.prometheus;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DailyUserResetScheduler {
    private final UserMetrics userMetrics;

    public DailyUserResetScheduler(UserMetrics userMetrics) {
        this.userMetrics = userMetrics;
    }

    @Scheduled(cron = "0 0 0 * * ?") // Каждый день в 00:00
    public void resetDailyUserCounter() {
        userMetrics.resetDailyCounter();
    }
}