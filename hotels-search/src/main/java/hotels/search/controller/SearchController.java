package hotels.search.controller;
import hotels.search.model.SearchResult;
import hotels.search.service.BookingSearchService;
import hotels.search.service.JalanSearchService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


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
    public ResponseEntity<List<SearchResult>> fetchBooking() {

        try {
            List<SearchResult> responses = bookingSearchService.getAllSearchResult();
            return new ResponseEntity<>(responses, HttpStatus.OK);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping("/fetch/jalan")
    public ResponseEntity<List<SearchResult>> fetchJalan() {
        try {
            List<SearchResult> responses = jalanSearchService.getAllSearchResult();
            return new ResponseEntity<>(responses, HttpStatus.OK);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}




