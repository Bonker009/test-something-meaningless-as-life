package com.kshrd.devconnect_springboot.service.implementation;


import com.kshrd.devconnect_springboot.exception.BadRequestException;
import com.kshrd.devconnect_springboot.model.entity.Comment;
import com.kshrd.devconnect_springboot.model.entity.Upvote;
import com.kshrd.devconnect_springboot.respository.CommentRepository;
import com.kshrd.devconnect_springboot.respository.UpvoteRepository;
import com.kshrd.devconnect_springboot.service.UpvoteService;
import com.kshrd.devconnect_springboot.utils.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpvoteServiceImplementation implements UpvoteService {

    private final UpvoteRepository repository;
    private final CommentRepository commentsRepository;
    @Override
    public void createUpvote(UUID commentId) {
        Upvote upvote = Upvote.builder()
                .commentId(commentId)
                .userId(CurrentUser.appUserId)
                .build();
        try{
            Integer total = repository.countUpvote(commentId);
            commentsRepository.addUpvote(commentId , total);
            repository.insertUpvote(upvote);
        }catch (DuplicateKeyException e){
            throw new BadRequestException("You have already upvoted this comment");
        }catch (DataIntegrityViolationException e ){
            throw new BadRequestException("Comment not found");
        }
    }
    @Override
    public void deleteUpvote(UUID commentId) {
        Upvote upvote = Upvote.builder()
                .commentId(commentId)
                .userId(CurrentUser.appUserId)
                .build();
        UUID deleted = repository.deleteUpvote(upvote);
        Comment comment= commentsRepository.selectCommentsById(commentId);
        if (comment == null) throw new BadRequestException("comment not found!");
        if (deleted == null) throw new BadRequestException("this comment is not upvoted yet");
        repository.deleteUpvote(upvote);
        Integer total = repository.countUpvote(commentId);
        commentsRepository.addUpvote( commentId , total);
    }
}
