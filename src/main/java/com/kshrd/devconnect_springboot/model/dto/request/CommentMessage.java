package com.kshrd.devconnect_springboot.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentMessage {
    private UUID userId;
    private UUID topicId;
    private UUID commentId;
    private CommentRequest data;
}