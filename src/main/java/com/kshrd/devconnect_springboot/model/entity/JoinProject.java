package com.kshrd.devconnect_springboot.model.entity;

import com.kshrd.devconnect_springboot.model.dto.response.AppUserResponse;
import com.kshrd.devconnect_springboot.model.dto.response.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JoinProject {
    private String title;
    private String description;
    private Boolean isApproved;
    private Boolean isInvited;
    private UUID projectId;
    private Position position;
    private UserResponse developer;
}
