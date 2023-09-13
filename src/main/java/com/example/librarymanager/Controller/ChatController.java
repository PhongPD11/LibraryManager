package com.example.librarymanager.Controller;

import com.example.librarymanager.Model.ChatMessage;
import com.example.librarymanager.Model.MessageType;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.util.Objects;

@Controller
public class ChatController {
    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(
            @Payload ChatMessage chatMessage
    ) {
        if (Objects.equals(chatMessage.getRoomId(), 1234)) {
            return chatMessage;
        } else return null;
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(
            @Payload ChatMessage chatMessage,
            SimpMessageHeaderAccessor headerAccessor
    ) {
        if (Objects.equals(chatMessage.getRoomId(), 1234)) {
            headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
            return chatMessage;
        } else {
            return ChatMessage.builder().type(MessageType.REJECT).build();
        }
    }
}