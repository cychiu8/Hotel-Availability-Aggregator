package notification.service.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import notification.service.service.LineMessageService;
import org.springframework.web.bind.annotation.PostMapping;


@RestController
public class LineMessageController {

    private static final Logger logger = LoggerFactory.getLogger(LineMessageController.class);

    @Autowired
    private LineMessageService lineMessageService;

    @PostMapping("/line/send-notification")
    public ResponseEntity<String> sendNotification(@RequestBody String message) {

        try {
            logger.info("recived message ... ", message);
            logger.info("sending message ... ");
            lineMessageService.sendMessageByBiz(message);
            logger.info("message sent successfully");
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
