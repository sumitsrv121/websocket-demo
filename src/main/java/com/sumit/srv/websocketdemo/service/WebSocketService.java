package com.sumit.srv.websocketdemo.service;

import com.sumit.srv.websocketdemo.dto.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WebSocketService {

    private final SimpMessagingTemplate messagingTemplate;
    private final NotificationService notificationService;

    @Autowired
    public WebSocketService(SimpMessagingTemplate messagingTemplate, NotificationService notificationService) {
        this.messagingTemplate = messagingTemplate;
        this.notificationService = notificationService;
    }

    public void notifyFrontEnd(String from, String message) {
        ResponseMessage responseMessage = new ResponseMessage(from == null ? "Server" : from, message);
        notificationService.sendGlobalNotification();
        messagingTemplate.convertAndSend("/topic/messages", responseMessage);
    }

    public void notifyUser(String from, String message) {
        ResponseMessage responseMessage = new ResponseMessage("server", message);
        notificationService.sendPrivateNotification(from);
        messagingTemplate.convertAndSendToUser(from, "/topic/private-messages", responseMessage);
    }
}
