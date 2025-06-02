package com.kshrd.devconnect_springboot.model.entity;

import com.kshrd.devconnect_springboot.model.dto.response.AppUserResponse;
import com.kshrd.devconnect_springboot.model.dto.response.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Project {
    private UUID projectId;
    private String title;
    private String description;
    private Boolean isOpen;
    private Boolean isBookmark;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private LocalDate createdAt;
    private UserResponse owner;
    private List<Skill> skills;
    private List<ProjectPosition> positions;
    private List<JoinProject> joinProjects;
}
