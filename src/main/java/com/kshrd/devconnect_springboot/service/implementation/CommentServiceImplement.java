package com.kshrd.devconnect_springboot.service.implementation;

import com.kshrd.devconnect_springboot.exception.BadRequestException;
import com.kshrd.devconnect_springboot.exception.NotFoundException;
import com.kshrd.devconnect_springboot.model.dto.request.CommentMessage;
import com.kshrd.devconnect_springboot.model.dto.request.CommentRequest;
import com.kshrd.devconnect_springboot.model.entity.Comment;
import com.kshrd.devconnect_springboot.respository.CommentRepository;
import com.kshrd.devconnect_springboot.respository.TopicRepository;
import com.kshrd.devconnect_springboot.service.CommentService;
import com.kshrd.devconnect_springboot.utils.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentServiceImplement implements CommentService {

    private final CommentRepository repository;
    private final TopicRepository topicRepository;
    @Override
    public Comment createComments(CommentMessage message) {
        if(topicRepository.selectTopicsById(message.getTopicId()) == null) {
            throw new NotFoundException("Topic not found");
        }
        return repository.insertComments(message.getData(), message.getUserId(), message.getTopicId());
    }

    @Override
    public Comment updateComments(UUID id, CommentRequest entity) {
        if (repository.selectCommentsById(id) == null) {
            throw new NotFoundException("Comment not found");
        }
        Comment comment = repository.selectCommentsById(id);
        if (!comment.getCreator().getUserId().equals(CurrentUser.appUserId)) {
            throw new BadRequestException("You are not the owner of this comment");
        }
        return repository.updateComments(entity, id, CurrentUser.appUserId);
    }

    @Override
    public void deleteComments(UUID id) {
        if (repository.selectCommentsById(id) == null) {
            throw new NotFoundException("Comment not found");
        }
        Comment comment = repository.selectCommentsById(id);
        if (!comment.getCreator().getUserId().equals(CurrentUser.appUserId)) {
            throw new BadRequestException("You are not the owner of this comment");
        }
        repository.deleteComments(id);
    }

    @Override
    public Comment insertReplyComment(CommentMessage message) {
        Comment comment = repository.selectCommentsById(message.getCommentId());
        if (repository.selectCommentsById(message.getCommentId()) == null) {
            throw new NotFoundException("Comment not found");
        }
        if (message.getUserId() == comment.getCreator().getUserId()) {
            throw new BadRequestException("You can't reply to your own comment");
        }
        return repository.insertReplyComment(message.getData(), message.getUserId(), message.getCommentId());
    }
}
