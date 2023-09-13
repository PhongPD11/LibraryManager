package com.example.librarymanager.Controller;

import com.example.librarymanager.Model.ChatMessage;
import com.example.librarymanager.Model.MessageType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Objects;

@Component
@Slf4j
@RequiredArgsConstructor
public class WebSocketEventListener {

    private final SimpMessageSendingOperations messagingTemplate;

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = (String) Objects.requireNonNull(headerAccessor.getSessionAttributes()).get("username");
        int roomId = (int) Objects.requireNonNull(headerAccessor.getSessionAttributes()).get("roomId");
        if (Objects.equals(roomId, 1234)){
            if (username != null) {
                log.info("user disconnected: {}", username);
                var chatMessage = ChatMessage.builder()
                        .type(MessageType.LEAVE)
                        .sender(username)
                        .build();
                messagingTemplate.convertAndSend("/topic/public", chatMessage);
            }
        }
    }
}