package hotels.search.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import hotels.search.model.SearchCondition;
import hotels.search.model.SearchResult;

@Service
public class JalanSearchService extends SearchAbstractService {

    private final DestinationMapService destinationMapService;
    private final SearchConditionService searchConditionService;
    private final SearchResultService searchResultService;

    public JalanSearchService(DestinationMapService destinationMapService,
            SearchConditionService searchConditionService,
            SearchResultService searchResultService) {
        this.destinationMapService = destinationMapService;
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

        long stayCount = ChronoUnit.DAYS.between(search.getCheckin(), search.getCheckout());
        String kenId =
                destinationMapService.getDestinationMapById(search.getDest()).getJalanKenId();
        String lrgId =
                destinationMapService.getDestinationMapById(search.getDest()).getJalanLrgId();

        UriComponentsBuilder builder = UriComponentsBuilder.newInstance().scheme("https")
                .host("www.jalan.net").pathSegment(kenId, "LRG_" + lrgId).path("/")
                .queryParam("stayYear", search.getCheckin().getYear())
                .queryParam("stayMonth", search.getCheckin().getMonthValue())
                .queryParam("stayDay", search.getCheckin().getDayOfMonth())
                .queryParam("stayCount", String.valueOf(stayCount)) // Assuming a stay count
                                                                    // of 1
                .queryParam("roomCount", search.getNoRooms())
                .queryParam("adultNum", search.getGroupAdults())
                // Add other query parameters as needed
                .queryParam("minPrice", "0").queryParam("maxPrice", "999999")
                .queryParam("mealType", "").queryParam("kenCd", kenId).queryParam("lrgCd", lrgId)
                .queryParam("rootCd", "04").queryParam("distCd", "01")
                .queryParam("roomCrack", "100000").queryParam("reShFlg", "1")
                .queryParam("mvTabFlg", "0").queryParam("listId", "0")
                .queryParam("screenId", "UWW1402");
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

            Elements countElements = doc.select(".jlnpc-listInformation--count");
            if (countElements.isEmpty()) {
                result.setNumberOfResults(numberOfResults);
                result.setMinPrice(minPrice);
                result.setMaxPrice(maxPrice);
                return result;
            }
            numberOfResults = Integer.parseInt(countElements.first().text());

            Elements priceElements = doc.select(".p-searchResultItem__lowestPriceValue");
            int[] prices = new int[numberOfResults];
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
}
