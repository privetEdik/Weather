package kettlebell.weather.util;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

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
		service.shutdownNow();
	}
}
