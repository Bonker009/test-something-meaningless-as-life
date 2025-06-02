package com.kshrd.devconnect_springboot.service.implementation;

import com.kshrd.devconnect_springboot.exception.NotFoundException;
import com.kshrd.devconnect_springboot.model.entity.Badges;
import com.kshrd.devconnect_springboot.model.entity.DeveloperBadge;
import com.kshrd.devconnect_springboot.model.entity.DeveloperProfiles;
import com.kshrd.devconnect_springboot.model.entity.Topic;
import com.kshrd.devconnect_springboot.respository.BadgeRepository;
import com.kshrd.devconnect_springboot.respository.CommentRepository;
import com.kshrd.devconnect_springboot.respository.DeveloperProfilesRepository;
import com.kshrd.devconnect_springboot.respository.TopicRepository;
import com.kshrd.devconnect_springboot.service.BadgeService;
import com.kshrd.devconnect_springboot.utils.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BadgeServiceImplement implements BadgeService {

    private final BadgeRepository badgeRepository;
    private final CommentRepository commentRepository;
    private final DeveloperProfilesRepository developerProfilesRepository;
    private final TopicRepository topicRepository;

    @Override
    public DeveloperBadge getBadgeCurrentUser() {
        DeveloperBadge developerProfiles = badgeRepository.getBadgesByCurrentUser(CurrentUser.appUserId);
        if (developerProfiles == null) {
            throw new NotFoundException("Developer badges not found for current user");
        }
        return developerProfiles;
    }


}
