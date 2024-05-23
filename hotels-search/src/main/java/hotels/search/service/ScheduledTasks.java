package hotels.search.service;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import hotels.search.model.SearchResult;

@EnableScheduling
@Component
public class ScheduledTasks {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);

    private final BookingSearchService bookingSearchService;
    private final JalanSearchService jalanSearchService;
    private final NotificationService notificationService;

    @Value("${booking.cron}")
    private String bookingCron;

    @Value("${jalan.cron}")
    private String jalanCron;

    public ScheduledTasks(BookingSearchService bookingSearchService,
            JalanSearchService jalanSearchService, NotificationService notificationService) {
        this.bookingSearchService = bookingSearchService;
        this.jalanSearchService = jalanSearchService;
        this.notificationService = notificationService;
    }

    @Scheduled(cron = "${booking.cron}")
    public void fetchBooking() {

        logger.info("Fetching Booking");
        List<SearchResult> responses = bookingSearchService.getAllSearchResult();
        logger.info("Finished fetching Booking");

        for (SearchResult response : responses) {
            if (response.getNumberOfResults() > 0) {
                notificationService.sendNotification(response);
            }
        }
    }

    @Scheduled(cron = "${jalan.cron}")
    public void fetchJalan() {

        logger.info("Fetching Jalan");
        List<SearchResult> responses = jalanSearchService.getAllSearchResult();
        logger.info("Finished fetching Jalan");

        for (SearchResult response : responses) {
            if (response.getNumberOfResults() > 0) {
                notificationService.sendNotification(response);
            }
        }
    }
}
