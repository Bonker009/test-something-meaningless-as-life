package com.kshrd.devconnect_springboot.service;

import com.kshrd.devconnect_springboot.model.entity.Upvote;

import java.util.UUID;

public interface UpvoteService {
    void createUpvote(UUID commentId);
    void deleteUpvote(UUID commentId);
}
