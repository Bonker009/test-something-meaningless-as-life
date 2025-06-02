package com.kshrd.devconnect_springboot.  model.dto.request;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.kshrd.devconnect_springboot.model.JSONBTemplate.jobBoard.JobBoard;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import jakarta.validation.constraints.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobsRequest {

    @NotNull(message = "Position ID is required")
    @Schema(defaultValue = "f341c41b-5c02-423a-b518-03b8d2544c27")
    private UUID positionId;

    @NotBlank(message = "Salary is required")
    private String salary;

    @NotBlank(message = "Location is required")
    private String location;

    @NotNull(message = "Status must be specified")
    private Boolean status;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Job board must be provided")
    private JobBoard jobBoard;

    @NotNull(message = "Job type ID is required")
    private UUID jobTypeId;

    @NotNull(message = "Pax must be specified")
    @Min(value = 1, message = "Pax must be at least 1")
    private Integer pax;

    @NotEmpty(message = "At least one skill ID must be provided")
    private List<UUID> skillId;
}
