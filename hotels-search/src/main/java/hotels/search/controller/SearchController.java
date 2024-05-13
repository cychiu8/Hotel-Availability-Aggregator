package hotels.search.controller;
import hotels.search.model.SearchCondition;
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
        SearchCondition search = new SearchCondition();

        search.setDest("Aomori");
        search.setCheckin("2024-07-03");
        search.setCheckout("2024-07-04");
        search.setGroupAdults(1);
        search.setGroupChildren(0);
        search.setNoRooms(1);
        String data = bookingSearchService.getSearchResult(search);

        return data;
    }

    @RequestMapping("/fetch/jalan")
    public String fetchJalan() {
        SearchCondition search = new SearchCondition();

        search.setDest("Aomori");
        search.setCheckin("2024-07-03");
        search.setCheckout("2024-07-04");
        search.setGroupAdults(1);
        search.setGroupChildren(0);
        search.setNoRooms(1);
        String data = jalanSearchService.getSearchResult(search);

        return data;
    }
}




