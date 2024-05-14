package hotels.search.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.web.util.UriComponentsBuilder;
import hotels.search.model.SearchCondition;
import hotels.search.model.SearchResult;


@Service
public class BookingSearchService extends SearchAbstractService {

    private final DestinationMappingService destinationMappingService;
    private final SearchConditionService searchConditionService;


    public BookingSearchService(DestinationMappingService destinationMappingService, SearchConditionService searchConditionService) {
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
    public String getSearchResult(SearchCondition search){

        // String url = "https://www.booking.com/searchresults.zh-tw.html?ss=%E9%9D%92%E6%A3%AE%E8%BB%8A%E7%AB%99%EF%BC%88Aomori+Station%EF%BC%89%2C+%E9%9D%92%E6%A3%AE%2C+%E9%9D%92%E6%A3%AE%E7%B8%A3%2C+%E6%97%A5%E6%9C%AC&map=1&efdco=1&label=gen173nr-1FCAEoggI46AdIM1gEaHWIAQGYATC4AQfIAQzYAQHoAQH4AQuIAgGoAgO4AuXl-7EGwAIB0gIkZjk2YzU4MWMtZWQ1MC00YWQ3LWJmZTctY2YyNmE0NTEwZGI12AIG4AIB&sid=7d982f20d955da37b5ed6b046464fddd&aid=304142&lang=zh-tw&sb=1&src_elem=sb&src=index&dest_id=900055309&dest_type=landmark&ac_position=1&ac_click_type=b&ac_langcode=xt&ac_suggestion_list_length=5&search_selected=true&search_pageview_id=5af31ef361410115&ac_meta=GhBiN2NkMWYwN2YxMjgwMDA5IAEoATICeHQ6BmFvbW9yaUAASgBQAA%3D%3D&checkin=2024-07-03&checkout=2024-07-04&group_adults=1&no_rooms=1&group_children=0"; // replace with the actual URL
        
        String destinationId = destinationMappingService.getBookingDestinationId(search.getDest());
        
        UriComponentsBuilder builder = UriComponentsBuilder.newInstance()
            .scheme("https")
            .host("www.booking.com")
            .path("/searchresults.zh-tw.html")
            // .queryParam("ss", "%E9%9D%92%E6%A3%AE%E8%BB%8A%E7%AB%99%EF%BC%88Aomori+Station%EF%BC%89%2C+%E9%9D%92%E6%A3%AE%2C+%E9%9D%92%E6%A3%AE%E7%B8%A3%2C+%E6%97%A5%E6%9C%AC")
            // .queryParam("map", "1")
            // .queryParam("efdco", "1")
            // .queryParam("label", "gen173nr-1FCAEoggI46AdIM1gEaHWIAQGYATC4AQfIAQzYAQHoAQH4AQuIAgGoAgO4AuXl-7EGwAIB0gIkZjk2YzU4MWMtZWQ1MC00YWQ3LWJmZTctY2YyNmE0NTEwZGI12AIG4AIB")
            // .queryParam("sid", "7d982f20d955da37b5ed6b046464fddd")
            // .queryParam("aid", "304142")
            .queryParam("lang", "zh-tw")
            .queryParam("sb", "1")
            .queryParam("src_elem", "sb")
            .queryParam("src", "index")
            .queryParam("dest_id", destinationId)
            .queryParam("dest_type", "city")
            .queryParam("ac_position", "1")
            .queryParam("ac_click_type", "b")
            .queryParam("ac_langcode", "xt")
            .queryParam("ac_suggestion_list_length", "5")
            .queryParam("search_selected", "true")
            // .queryParam("search_pageview_id", "5af31ef361410115")
            // .queryParam("ac_meta", "GhBiN2NkMWYwN2YxMjgwMDA5IAEoATICeHQ6BmFvbW9yaUAASgBQAA%3D%3D")
            .queryParam("checkin", search.getCheckin())
            .queryParam("checkout", search.getCheckout())
            .queryParam("group_adults", search.getGroupAdults())
            .queryParam("group_children", search.getGroupChildren())
            .queryParam("no_rooms", search.getNoRooms())
            ;

        String url = builder.toUriString();

        String response = "Booking search result";
        try {
            LocalDateTime startTime = LocalDateTime.now();
            Document doc = Jsoup.connect(url).get();
            LocalDateTime endTime = LocalDateTime.now();

            SearchResult result = extractResponse(doc);

            ZoneId zoneId = ZoneId.systemDefault();
            long startLong = startTime.atZone(zoneId).toInstant().toEpochMilli();
            long endLong = endTime.atZone(zoneId).toInstant().toEpochMilli();
            long responseTime = endLong - startLong;
            
            result.setExecuteTime(startTime);
            result.setResponseTime(responseTime);
            result.setExecuteUrl(url);

            response = result.toString();
        } catch (IOException e) {
            // Handle the exception
            e.printStackTrace();
        }
        
        return response;
    }

    private SearchResult extractResponse(Document doc) {

        // get result count
        Element countElement = doc.selectFirst("h1");
        String numberOfResultsStr = countElement.text().replaceAll("[^0-9]", "");
        int numberOfResults = Integer.parseInt(numberOfResultsStr);

        // get price
        Elements priceElements = doc.select("[data-testid=price-and-discounted-price]");

        int[] prices = new int[priceElements.size()];
        for (int i = 0; i < priceElements.size(); i++) {
            Element priceElement = priceElements.get(i);
            String priceStr = priceElement.text().replaceAll("[^0-9]", "");
            prices[i] = Integer.parseInt(priceStr);
        }

        int minPrice = Arrays.stream(prices).min().orElse(0);
        int maxPrice = Arrays.stream(prices).max().orElse(0);

        String words = "Number of results: " + numberOfResults + "\n";
        words += "Min price: " + minPrice + "\n";
        words += "Max price: " + maxPrice + "\n";
        words += Arrays.toString(prices);

        System.out.println(words);

        SearchResult result = new SearchResult();
        result.setNumberOfResults(numberOfResults);
        result.setMinPrice(minPrice);
        result.setMaxPrice(maxPrice);
        
        return result;
    }
}
