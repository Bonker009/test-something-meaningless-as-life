package com.kshrd.devconnect_springboot.service;

import com.kshrd.devconnect_springboot.model.entity.Badges;
import com.kshrd.devconnect_springboot.model.entity.DeveloperBadge;

public interface BadgeService {
    DeveloperBadge getBadgeCurrentUser();
}
