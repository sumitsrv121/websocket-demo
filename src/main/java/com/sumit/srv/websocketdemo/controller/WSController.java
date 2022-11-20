package com.sumit.srv.websocketdemo.controller;

import com.sumit.srv.websocketdemo.dto.Message;
import com.sumit.srv.websocketdemo.service.WebSocketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class WSController {
    @Autowired
    private WebSocketService wSService;
    @Autowired
    private SimpUserRegistry userRegistry;

    @PostMapping("/send-message")
    public void sendMessage(@RequestBody final Message message) {
        log.info("Current {} users are connected to the server", userRegistry.getUserCount());

        wSService.notifyFrontEnd(message.getFrom(), message.getMessageContent());
    }

    @PostMapping("/send-private-message")
    public void sendPrivateMessage(@RequestBody final Message message) {
        log.info("Current {} users are connected to the server", userRegistry.getUserCount());

        wSService.notifyUser(message.getFrom(), message.getMessageContent());
    }
}
