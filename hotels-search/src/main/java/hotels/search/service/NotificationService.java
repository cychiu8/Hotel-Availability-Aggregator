package hotels.search.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import hotels.search.model.SearchResult;

@Service
public class NotificationService {

        private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

        private WebClient webClient;

        @Autowired
        private KafkaTemplate<String, String> kafkaTemplate;

        @Value("${notification.url}")
        private String NotificationUrl;

        @Value("${kafka.topic.name}")
        private String topicName;

        @Value("${notification.type}")
        private String notificationType;

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
                                dest, checkin, checkout, results, minPrice, maxPrice, url,
                                executeTime);

                if (notificationType.equals("api")) {
                        sendByApi(message + "\n" + "by API");
                } else if (notificationType.equals("kafka")) {
                        sendByKafka(message + "\n" + "by Kafka");
                } else {
                        sendByApi(message + "\n" + "by API");
                        sendByKafka(message + "\n" + "by Kafka");
                }
                logger.info("notification sent");
        }

        public void sendByKafka(String message) {
                kafkaTemplate.send("notification", message);
                logger.info("notification sent by kafka");

                // Send the message to the topic
                ListenableFuture<SendResult<String, String>> future =
                                kafkaTemplate.send(topicName, message);

                // Add a callback
                future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
                        @Override
                        public void onSuccess(SendResult<String, String> result) {
                                logger.info("Sent message=[" + message + "] with offset=["
                                                + result.getRecordMetadata().offset() + "]");
                        }

                        @Override
                        public void onFailure(Throwable ex) {
                                logger.warn("Unable to send message=[" + message + "] due to : "
                                                + ex.getMessage());
                        }
                });
        }

        public void sendByApi(String message) {
                webClient.post().uri("/line/send-notification").bodyValue(message).retrieve()
                                .onStatus(HttpStatus::isError, ClientResponse::createException)
                                .bodyToMono(String.class)
                                .doOnNext(response -> System.out
                                                .println("Response received: " + response))
                                .doOnError(error -> error.printStackTrace()).log().block();
                logger.info("notification sent by api");
        }
}
