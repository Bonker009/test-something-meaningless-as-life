package com.kshrd.devconnect_springboot.model.dto.request;


import com.kshrd.devconnect_springboot.model.entity.Skill;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class TopicRequest {
    @NotBlank(message = "title is required")
    @Size(max = 255 , message = "title can't greater than 255 characters")
    private String title;
    @NotBlank(message = "description is required")
    private String content;
    @NotNull(message = "tags is required")
    private List<UUID> tags;
}
