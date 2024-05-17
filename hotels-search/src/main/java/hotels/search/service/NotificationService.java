package hotels.search.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

        String message = "Search Result: " + searchResult.toString();

        webClient.post().uri("/line/send-notification").bodyValue(message).retrieve()
                .bodyToMono(String.class)
                .doOnNext(response -> System.out.println("Response received: " + response))
                .doOnError(error -> error.printStackTrace()).log().block();

        logger.info("notification sent");
    }
}
