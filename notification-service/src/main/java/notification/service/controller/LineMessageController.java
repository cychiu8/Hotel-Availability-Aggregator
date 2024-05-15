package notification.service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import notification.service.service.LineMessageService;

@RestController
public class LineMessageController {

    @Autowired
    private LineMessageService lineMessageService;

    @RequestMapping("/line/test")
    public ResponseEntity<String> sendNotification() {

        try {
            lineMessageService.sendMessage("testțtttt");
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
