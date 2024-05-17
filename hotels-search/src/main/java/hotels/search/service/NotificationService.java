package hotels.search.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.format.DateTimeFormatter;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import hotels.search.model.SearchResult;

@Service
public class NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    private WebClient webClient;


    @Value("${notification.url}")
    private String NotificationUrl;

    public NotificationService() {}

    @PostConstruct
    public void init() {
        this.webClient = WebClient.create(NotificationUrl);
    }

    public void sendNotification(SearchResult searchResult) {

        logger.info("sending notification ..." + searchResult.toString());

        String dest = searchResult.getSearchCondition().getDest();
        String checkin = searchResult.getSearchCondition().getCheckin()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String checkout = searchResult.getSearchCondition().getCheckout()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        int results = searchResult.getNumberOfResults();
        int minPrice = searchResult.getMinPrice();
        int maxPrice = searchResult.getMaxPrice();
        String executeTime = searchResult.getExecuteTime()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String url = searchResult.getExecuteUrl();

        String message = String.format(
                "Destination: %s\nCheckin: %s\nCheckout: %s\nResults: %d\nPrice range: %d - %d\nURL: %s\nExecute time: %s\n",
                dest, checkin, checkout, results, minPrice, maxPrice, url, executeTime);

        webClient.post().uri("/line/send-notification").bodyValue(message).retrieve()
                .bodyToMono(String.class)
                .doOnNext(response -> System.out.println("Response received: " + response))
                .doOnError(error -> error.printStackTrace()).log().block();

        logger.info("notification sent");
    }
}
