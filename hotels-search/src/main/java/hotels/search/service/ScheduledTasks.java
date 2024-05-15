package hotels.search.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@EnableScheduling
@Component
public class ScheduledTasks {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);

    private final BookingSearchService bookingSearchService;
    private final JalanSearchService jalanSearchService;

    @Value("${booking.cron}")
    private String bookingCron;

    @Value("${jalan.cron}")
    private String jalanCron;

    public ScheduledTasks(BookingSearchService bookingSearchService,
            JalanSearchService jalanSearchService) {
        this.bookingSearchService = bookingSearchService;
        this.jalanSearchService = jalanSearchService;
    }

    @Scheduled(cron = "${booking.cron}")
    public void fetchBooking() {
        logger.info("Fetching Booking");
        bookingSearchService.getAllSearchResult();
        logger.info("Finished fetching Booking");
    }

    @Scheduled(cron = "${jalan.cron}")
    public void fetchJalan() {
        logger.info("Fetching Jalan");
        jalanSearchService.getAllSearchResult();
        logger.info("Finished fetching Jalan");
    }
}
