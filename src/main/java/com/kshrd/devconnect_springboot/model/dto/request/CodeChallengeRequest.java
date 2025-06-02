package com.kshrd.devconnect_springboot.model.dto.request;

import java.time.LocalDate;
import java.util.List;

import com.kshrd.devconnect_springboot.model.JSONBTemplate.TestCase;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CodeChallengeRequest {
    @NotBlank(message = "title is required")
    @Size(max = 100, message = "Title must not exceed 100 characters")
    private String title;
    @NotBlank(message = "description is required")
    private String description;
    @NotBlank(message = "instruction is required")
    private String instruction;

    @NotNull(message = "test case is required")
    private List<@Valid TestCase> testCase;

    @NotNull(message = "Score is required")
    @Min(value = 100, message = "Score must be at least 100")
    private Integer score;
    @NotBlank(message = "starter code is required")
    private String starterCode;

    @NotBlank(message = "language is required")
    @Pattern(
            regexp = "^[a-zA-Z+#]+$",
            message = " language must contain only letters"
    )
    @Size(max = 50, message = "Language must not exceed 50 characters")
    @Schema(description = "Programming language used in the challenge", example = "Java", defaultValue = "Java")
    private String language;
}
