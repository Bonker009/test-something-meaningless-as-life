package com.kshrd.devconnect_springboot.model.entity;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.kshrd.devconnect_springboot.model.dto.response.AppUserResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment {
       private UUID commentId;
       private String content;
       private LocalDate postedAt;
       private Integer totalUpvote;
       private UUID topicId;
       private AppUserResponse creator;
       @JsonInclude(JsonInclude.Include.NON_NULL)
       private UUID parentId;
       private List<Comment> replies;
}
