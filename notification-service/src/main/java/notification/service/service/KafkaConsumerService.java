package notification.service.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import notification.service.controller.LineMessageController;

@Service
public class KafkaConsumerService {

    private static final Logger logger = LoggerFactory.getLogger(LineMessageController.class);


    @Autowired
    private LineMessageService lineMessageService;

    @KafkaListener(topics = "${kafka.topic.name}", groupId = "${kafka.group.id}")
    public void consume(String message) {
        logger.info("recived message ... ", message);
        logger.info("sending message ... ");
        lineMessageService.sendMessage(message);
        logger.info("message sent successfully");
    }
}
