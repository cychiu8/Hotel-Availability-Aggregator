package notification.service.service;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import notification.service.model.LineMessage;
import notification.service.model.LineMessageBody;

@Service
public class LineMessageService {

    private WebClient webClient;

    private WebClient notifyWebClient;

    @Value("${line.api.url}")
    private String lineApiUrl;

    @Value("${line.notify.url}")
    private String notifyUrl;

    @Value("${line.access.token}")
    private String accessToken;

    @Value("${line.notify.token}")
    private String notifyToken;

    @PostConstruct
    public void init() {
        this.webClient = WebClient.create(this.lineApiUrl);
        this.notifyWebClient = WebClient.create(this.notifyUrl);
    }

    public void sendMessageByNotify(String message) {
        String uri = "/api/notify";
        this.notifyWebClient.post().uri(uri).header("Authorization", "Bearer " + this.notifyToken)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .body(BodyInserters.fromFormData("message", message)).retrieve()
                .bodyToMono(Void.class)
                .doOnNext(response -> System.out.println("Response received: " + response))
                .doOnError(error -> error.printStackTrace()).log().block();
    }

    public void sendMessageByBiz(String message) {
        String uri = "/v2/bot/message/broadcast";
        ObjectMapper objectMapper = new ObjectMapper();

        List<LineMessage> messageList = new ArrayList<>();
        LineMessage messageObj = new LineMessage();
        messageObj.setType("text");
        messageObj.setText(message);
        messageList.add(messageObj);

        LineMessageBody lineMessageBody = new LineMessageBody();
        lineMessageBody.setMessages(messageList);

        String body = new String("");

        try {
            body = objectMapper.writeValueAsString(lineMessageBody);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException("Error while converting message body to JSON");
        }

        this.webClient.post().uri(uri).header("Authorization", "Bearer " + this.accessToken)
                .header("Content-Type", "application/json").bodyValue(body).retrieve()
                .bodyToMono(Void.class)
                .doOnNext(response -> System.out.println("Response received: " + response))
                .doOnError(error -> error.printStackTrace()).log().block();
    }
}
