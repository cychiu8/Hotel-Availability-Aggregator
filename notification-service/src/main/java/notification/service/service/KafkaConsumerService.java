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

    @Value("${line.biz.enable}")
    private boolean isBizEnable;

    @Value("${line.notify.enable}")
    private boolean isNotifyEnable;

    @Autowired
    private LineMessageService lineMessageService;

    @KafkaListener(topics = "${kafka.topic.name}", groupId = "${kafka.group.id}")
    public void consume(String message) {
        logger.info("recived message ... ", message);
        logger.info("sending message ... ");

        if (isNotifyEnable) {
            lineMessageService.sendMessageByNotify(message);
        }

        if (isBizEnable) {
            lineMessageService.sendMessageByBiz(message);
        }

        logger.info("message sent successfully");
    }
}
