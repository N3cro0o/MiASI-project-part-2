package pl.krasmap.common.schedules;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SyncSchedule {
    private static final int TIME_OFFSET_SEC = 1000;
    private static final int TIME_OFFSET_MIN = 60 * TIME_OFFSET_SEC;
    private static boolean IGNORE = false;

    @Scheduled(fixedRate = 5 * TIME_OFFSET_MIN)
    public void dbSync() {
        if (!IGNORE) {
            IGNORE = true;
            return;
        }
        System.out.println("TODO: Add db sync");
    }
}
