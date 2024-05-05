package kettlebell.weather.util;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScheduledExecutorServiceUtil {
    private static ScheduledExecutorService service;

    private ScheduledExecutorServiceUtil() {
    }

    public static ScheduledExecutorService getInstance() {
        if (service == null) {
            service = Executors.newSingleThreadScheduledExecutor();
        }
        return service;
    }

    public static void closeScheduled() {

        service.shutdown();
        try {
            if (!service.awaitTermination(1, TimeUnit.SECONDS)) {
                service.shutdownNow();
            }
        } catch (InterruptedException e) {
            service.shutdownNow();
        }
    }
}
