package com.sumit.srv.websocketdemo.service;

import com.github.javafaker.Faker;
import com.sumit.srv.websocketdemo.dto.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    private final SimpMessagingTemplate messagingTemplate;
    private final Faker faker;

    @Autowired
    public NotificationService(SimpMessagingTemplate messagingTemplate, Faker faker) {
        this.messagingTemplate = messagingTemplate;
        this.faker = faker;
    }

    public void sendGlobalNotification() {
        ResponseMessage responseMessage = new ResponseMessage("server", faker.howIMetYourMother().catchPhrase());
        messagingTemplate.convertAndSend("/topic/global-notifications", responseMessage);
    }

    public void sendPrivateNotification(final String id) {
        ResponseMessage responseMessage = new ResponseMessage("server", faker.company().catchPhrase());
        messagingTemplate.convertAndSendToUser(id, "/topic/private-notifications", responseMessage);
    }
}
