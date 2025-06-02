package com.kshrd.devconnect_springboot.model.entity;


import com.kshrd.devconnect_springboot.model.dto.response.AppUserResponse;
import com.kshrd.devconnect_springboot.model.dto.response.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Topic {
       private UUID topicId;
       private String title;
       private String content;
       private LocalDateTime postedAt;
       private UserResponse creator;
       private List<String> tags;
       private List<Comment> comments;
}
