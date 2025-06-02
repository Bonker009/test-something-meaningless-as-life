package com.kshrd.devconnect_springboot.service;

import com.kshrd.devconnect_springboot.model.dto.request.CommentMessage;
import com.kshrd.devconnect_springboot.model.dto.request.CommentRequest;
import com.kshrd.devconnect_springboot.model.entity.Comment;

import java.util.List;
import java.util.UUID;

public interface CommentService {
    Comment createComments(CommentMessage message);
    Comment updateComments (UUID id, CommentRequest entity);
    void deleteComments(UUID id);
    Comment insertReplyComment(CommentMessage message);

}
