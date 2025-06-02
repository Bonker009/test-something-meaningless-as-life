package com.kshrd.devconnect_springboot.utils;

import com.kshrd.devconnect_springboot.model.entity.AppUser;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

public class CurrentUser {
    public static AppUser appUser = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    public static UUID appUserId = appUser.getUserId();
}
