package com.kshrd.devconnect_springboot.model.dto.request;

import com.kshrd.devconnect_springboot.model.entity.Position;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProjectRequest {

    @NotBlank(message = "Project title cannot be blank")
    @Pattern(regexp = "^(?!\\s*$).{1,100}", message = "Project title must contain more than one word and not over 100 characters")
    private String title;

    @NotBlank(message = "Project description cannot be blank")
    @Pattern(regexp = "^(?!\\s*$).{1,255}", message = "Project description must contain more than one word and not over 255 characters")
    private String description;

    @NotNull
    private Boolean isOpen;

    @NotNull
    @FutureOrPresent
    private LocalDateTime startAt;

    @NotNull
    @FutureOrPresent
    private LocalDateTime endAt;

    @NotNull
    private List<ProjectPositionRequest> positions;

    @NotNull
    private List<UUID> skills;
}
