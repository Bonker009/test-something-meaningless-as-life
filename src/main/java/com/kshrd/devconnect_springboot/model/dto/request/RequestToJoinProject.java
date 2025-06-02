package com.kshrd.devconnect_springboot.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestToJoinProject {

    @NotBlank(message = "Title cannot be blank")
    @Pattern(regexp = "^(?!\\s*$).{1,100}", message = "Title must contain more than one word and not over 100 characters")
    private String title;

    @NotBlank(message = "Description cannot be blank")
    @Pattern(regexp = "^(?!\\s*$).{1,255}", message = "Description must contain more than one word and not over 255 characters")
    private String description;

    @NotNull
    @NotBlank
    private UUID positionId;
}
