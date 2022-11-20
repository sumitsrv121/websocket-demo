package com.sumit.srv.websocketdemo.controller;

import com.sumit.srv.websocketdemo.dto.Message;
import com.sumit.srv.websocketdemo.dto.ResponseMessage;
import com.sumit.srv.websocketdemo.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

import java.security.Principal;

@Controller
public class MessageController {
    @Autowired
    private NotificationService service;

    @MessageMapping("/message")
    @SendTo("/topic/messages")
    public ResponseMessage getMessage(@Payload final Message message) {
        service.sendGlobalNotification();
        return new ResponseMessage(HtmlUtils.htmlEscape(message.getFrom())
                , HtmlUtils.htmlEscape(message.getMessageContent()));
    }

    @MessageMapping("/private-message")
    @SendToUser("/topic/private-messages")
    public ResponseMessage getPrivateMessage(@Payload final Message message, Principal principal) {
        service.sendPrivateNotification(principal.getName());
        return new ResponseMessage(HtmlUtils.htmlEscape(
                "Sending private message to user" + ":" + message.getFrom()), HtmlUtils.htmlEscape(
                message.getMessageContent()));
    }
}
