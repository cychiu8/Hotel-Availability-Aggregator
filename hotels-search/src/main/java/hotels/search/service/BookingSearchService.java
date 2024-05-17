package hotels.search.service;

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
    private final SearchResultService searchResultService;


    public BookingSearchService(DestinationMappingService destinationMappingService,
            SearchConditionService searchConditionService,
            SearchResultService searchResultService) {
        this.destinationMappingService = destinationMappingService;
        this.searchConditionService = searchConditionService;
        this.searchResultService = searchResultService;
    }


    @Override
    public List<SearchResult> getAllSearchResult() {
        List<SearchCondition> conditions = searchConditionService.getAllSearchConditions();
        List<SearchResult> results = new ArrayList<>();
        for (SearchCondition condition : conditions) {
            SearchResult result = getSearchResultAndSave(condition);
            results.add(result);
        }

        return results;
    }


    @Override
    public SearchResult getSearchResultAndSave(SearchCondition search) {

        String destinationId = destinationMappingService.getBookingDestinationId(search.getDest());

        UriComponentsBuilder builder = UriComponentsBuilder.newInstance().scheme("https")
                .host("www.booking.com").path("/searchresults.zh-tw.html")
                // .queryParam("ss",
                // "%E9%9D%92%E6%A3%AE%E8%BB%8A%E7%AB%99%EF%BC%88Aomori+Station%EF%BC%89%2C+%E9%9D%92%E6%A3%AE%2C+%E9%9D%92%E6%A3%AE%E7%B8%A3%2C+%E6%97%A5%E6%9C%AC")
                // .queryParam("map", "1")
                // .queryParam("efdco", "1")
                // .queryParam("label",
                // "gen173nr-1FCAEoggI46AdIM1gEaHWIAQGYATC4AQfIAQzYAQHoAQH4AQuIAgGoAgO4AuXl-7EGwAIB0gIkZjk2YzU4MWMtZWQ1MC00YWQ3LWJmZTctY2YyNmE0NTEwZGI12AIG4AIB")
                // .queryParam("sid", "7d982f20d955da37b5ed6b046464fddd")
                // .queryParam("aid", "304142")
                .queryParam("lang", "zh-tw").queryParam("sb", "1").queryParam("src_elem", "sb")
                .queryParam("src", "index").queryParam("dest_id", destinationId)
                .queryParam("dest_type", "city").queryParam("ac_position", "1")
                .queryParam("ac_click_type", "b").queryParam("ac_langcode", "xt")
                .queryParam("ac_suggestion_list_length", "5").queryParam("search_selected", "true")
                // .queryParam("search_pageview_id", "5af31ef361410115")
                // .queryParam("ac_meta",
                // "GhBiN2NkMWYwN2YxMjgwMDA5IAEoATICeHQ6BmFvbW9yaUAASgBQAA%3D%3D")
                .queryParam("checkin", search.getCheckin())
                .queryParam("checkout", search.getCheckout())
                .queryParam("group_adults", search.getGroupAdults())
                .queryParam("group_children", search.getGroupChildren())
                .queryParam("no_rooms", search.getNoRooms());

        String url = builder.toUriString();
        SearchResult result = new SearchResult();


        try {
            LocalDateTime startTime = LocalDateTime.now();
            Document doc = Jsoup.connect(url).get();
            LocalDateTime endTime = LocalDateTime.now();

            result = extractResponse(doc);
            result.setSearchCondition(search);

            ZoneId zoneId = ZoneId.systemDefault();
            long startLong = startTime.atZone(zoneId).toInstant().toEpochMilli();
            long endLong = endTime.atZone(zoneId).toInstant().toEpochMilli();
            long responseTime = endLong - startLong;

            result.setExecuteTime(startTime);
            result.setResponseTime(responseTime);
            result.setExecuteUrl(url);


            searchResultService.saveSearchResult(result);

        } catch (IOException e) {
            // Handle the exception
            e.printStackTrace();

            throw new RuntimeException("An error occurred while extracting the response", e);
        }

        return result;
    }

    private SearchResult extractResponse(Document doc) {
        SearchResult result = new SearchResult();
        int numberOfResults = 0;
        int minPrice = 0;
        int maxPrice = 0;
        try {
            // get result count
            Element countElement = doc.selectFirst("h1");
            if (countElement == null) {
                return createResultWithDefaults();
            }

            String numberOfResultsStr = countElement.text().replaceAll("[^0-9]", "");
            if (numberOfResultsStr.isEmpty() || numberOfResultsStr == "0"
                    || numberOfResultsStr.equals("")) {
                return createResultWithDefaults();
            }

            numberOfResults = Integer.parseInt(numberOfResultsStr);

            // get price
            Elements priceElements = doc.select("[data-testid=price-and-discounted-price]");
            int[] prices = new int[priceElements.size()];
            for (int i = 0; i < priceElements.size(); i++) {
                Element priceElement = priceElements.get(i);
                String priceStr = priceElement.text().replaceAll("[^0-9]", "");
                prices[i] = Integer.parseInt(priceStr);
            }

            minPrice = Arrays.stream(prices).min().orElse(0);
            maxPrice = Arrays.stream(prices).max().orElse(0);

        } catch (NumberFormatException e) {
            e.printStackTrace();
            throw new RuntimeException("An error occurred while parsing the HTML", e);
        }
        result.setNumberOfResults(numberOfResults);
        result.setMinPrice(minPrice);
        result.setMaxPrice(maxPrice);
        return result;
    }

    private SearchResult createResultWithDefaults() {
        SearchResult result = new SearchResult();
        int numberOfResults = 0;
        int minPrice = 0;
        int maxPrice = 0;
        result.setNumberOfResults(numberOfResults);
        result.setMinPrice(minPrice);
        result.setMaxPrice(maxPrice);
        return result;
    }
}
