package com.kshrd.devconnect_springboot.controller;

import com.kshrd.devconnect_springboot.model.dto.request.CommentMessage;
import com.kshrd.devconnect_springboot.model.entity.Comment;
import com.kshrd.devconnect_springboot.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class CommentWebSocketController {

    private final CommentService commentService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/comments/create")
    public void handleCreateOrReply(CommentMessage message) {
        Comment result = (message.getCommentId() == null)
                ? commentService.createComments(message)   // root comment
                : commentService.insertReplyComment(message); // reply

        messagingTemplate.convertAndSend("/topic/comments", result);
    }
}