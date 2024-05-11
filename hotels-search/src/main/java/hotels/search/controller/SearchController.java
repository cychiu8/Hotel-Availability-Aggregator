package hotels.search.controller;
import hotels.search.service.BookingSearchService;
import hotels.search.service.JalanSearchService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;


@RestController
public class SearchController {
    
    @Autowired
    private BookingSearchService bookingSearchService;

    @Autowired
    private JalanSearchService jalanSearchService;

    @RequestMapping("/")
    public String home() {
        return "Hello World!";
    }

    @RequestMapping("/fetch/booking")
    public String fetchBooking() {
        String data = bookingSearchService.getSearchResult();

        return data;
    }

    @RequestMapping("/fetch/jalan")
    public String fetchJalan() {
        String data = jalanSearchService.getSearchResult();

        return data;
    }
}




