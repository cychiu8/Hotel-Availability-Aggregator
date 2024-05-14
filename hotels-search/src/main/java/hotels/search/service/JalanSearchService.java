package hotels.search.service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import hotels.search.model.SearchCondition;

@Service
public class JalanSearchService extends SearchAbstractService{

    private final DestinationMappingService destinationMappingService;
    private final SearchConditionService searchConditionService;

    public JalanSearchService(DestinationMappingService destinationMappingService, SearchConditionService searchConditionService) {
        this.destinationMappingService = destinationMappingService;
        this.searchConditionService = searchConditionService;
    }

    @Override
    public String getAllSearchResult() {
        List<SearchCondition> conditions = searchConditionService.getAllSearchConditions();
        List<String> results = new ArrayList<>();
        for(SearchCondition condition : conditions){
            String result = getSearchResult(condition);
            results.add(result);
        }
        
        return results.toString();
    }

    @Override
    public String getSearchResult(SearchCondition search) {

        // String url = "https://www.jalan.net/020000/LRG_020200/?stayYear=2024&stayMonth=5&stayDay=12&stayCount=1&roomCount=1&adultNum=1&minPrice=0&maxPrice=999999&mealType=&kenCd=020000&lrgCd=020200&rootCd=04&distCd=01&roomCrack=100000&reShFlg=1&mvTabFlg=0&listId=0&screenId=UWW1402";
        
        String[] destinationCodes = destinationMappingService.getJalanDestinationCodes(search.getDest());
        long stayCount = ChronoUnit.DAYS.between(search.getCheckin(), search.getCheckout());


        UriComponentsBuilder builder = UriComponentsBuilder.newInstance()
            .scheme("https")
            .host("www.jalan.net")
            .pathSegment(destinationCodes[0], "LRG_" + destinationCodes[1])
            .path("/")
            .queryParam("stayYear", search.getCheckin().getYear())
            .queryParam("stayMonth", search.getCheckin().getMonthValue())
            .queryParam("stayDay", search.getCheckin().getDayOfMonth())
            .queryParam("stayCount", String.valueOf(stayCount)) // Assuming a stay count of 1
            .queryParam("roomCount", search.getNoRooms())
            .queryParam("adultNum", search.getGroupAdults())
            // Add other query parameters as needed
            .queryParam("minPrice", "0")
            .queryParam("maxPrice", "999999")
            .queryParam("mealType", "")
            .queryParam("kenCd", "020000")
            .queryParam("lrgCd", "020200")
            .queryParam("rootCd", "04")
            .queryParam("distCd", "01")
            .queryParam("roomCrack", "100000")
            .queryParam("reShFlg", "1")
            .queryParam("mvTabFlg", "0")
            .queryParam("listId", "0")
            .queryParam("screenId", "UWW1402");
        String url = builder.toUriString();
        String response = "Jalan search result";
        try {
            Document doc = Jsoup.connect(url).get();
            response = doc.text(); // get the text of the page
        } catch (IOException e) {
            // Handle the exception
            e.printStackTrace();
        }
        
        return response;
    }
}
