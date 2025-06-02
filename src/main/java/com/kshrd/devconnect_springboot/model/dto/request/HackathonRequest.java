package com.kshrd.devconnect_springboot.model.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HackathonRequest {

    @NotBlank(message = "Hackathon title cannot be blank")
    @Pattern(regexp = "^(?!\\s*$).{1,100}", message = "Hackathon title must contain more than one word and not over 100 characters")
    @Schema(defaultValue = "title")
    private String title;

    @NotBlank(message = "Hackathon description cannot be blank")
    @Pattern(regexp = "^(?!\\s*$).{1,255}", message = "Hackathon description must contain more than one word and not over 255 characters")
    @Schema(defaultValue = "description")
    private String description;


    @NotNull(message = "Start date cannot be null")
    @FutureOrPresent
    @Schema(defaultValue = "start date")
    private LocalDateTime startDate;

    @NotNull(message = "End date cannot be null")
    @Future
    @Schema(defaultValue = "end date")
    private LocalDateTime endDate;


    @NotNull(message = "Availability status cannot be null")
    private Boolean isAvailable;

    @Min(value = 50, message = "Full score must be at least 50")
    @Max(value = 100, message = "Full score cannot be more than 100")
    @Pattern(regexp = "^[0-9]+$", message = "Full score must be an integer")
    @Schema(defaultValue = "full score")
    private String fullScore;

}