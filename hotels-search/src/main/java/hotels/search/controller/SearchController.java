package hotels.search.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import hotels.search.model.SearchResult;
import hotels.search.service.BookingSearchService;
import hotels.search.service.JalanSearchService;
import hotels.search.service.NotificationService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


@RestController
public class SearchController {

    private static final Logger logger = LoggerFactory.getLogger(SearchController.class);


    @Autowired
    private BookingSearchService bookingSearchService;

    @Autowired
    private JalanSearchService jalanSearchService;

    @Autowired
    private NotificationService notificationService;

    @RequestMapping("/")
    public String home() {
        return "Hello World!";
    }

    @RequestMapping("/fetch/booking")
    public ResponseEntity<List<SearchResult>> fetchBooking() {

        try {

            logger.info("fetchBooking called");
            List<SearchResult> responses = bookingSearchService.getAllSearchResult();
            logger.info("fetchBooking finished");

            for (SearchResult response : responses) {
                if (response.getNumberOfResults() > 0) {
                    notificationService.sendNotification(response);
                }
            }

            return new ResponseEntity<>(responses, HttpStatus.OK);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping("/fetch/jalan")
    public ResponseEntity<List<SearchResult>> fetchJalan() {
        try {
            logger.info("fetchJalan called");
            List<SearchResult> responses = jalanSearchService.getAllSearchResult();
            logger.info("fetchJalan finished");

            for (SearchResult response : responses) {
                if (response.getNumberOfResults() > 0) {
                    notificationService.sendNotification(response);
                }
            }

            return new ResponseEntity<>(responses, HttpStatus.OK);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}


