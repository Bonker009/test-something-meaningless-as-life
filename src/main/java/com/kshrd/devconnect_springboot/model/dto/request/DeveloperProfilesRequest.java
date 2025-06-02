package com.kshrd.devconnect_springboot.  model.dto.request;
    
    

import com.kshrd.devconnect_springboot.model.enums.EmployeeStatus;
import io.micrometer.common.lang.Nullable;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class DeveloperProfilesRequest {

    @Size(max = 100, message = "First name must not exceed 100 characters")
    @Pattern(
            regexp = "^[a-zA-Z\\s]*$",
            message = "First name must contain only letters and spaces"
    )
    @Schema(defaultValue = "Sok")
    private String firstName;

    @Size(max = 100, message = "Full name must not exceed 100 characters")
    @Pattern(
            regexp = "^[a-zA-Z\\s]*$",
            message = "Last name must contain only letters and spaces"
    )
    @Schema(defaultValue = "Chanthy")
    private String lastName;

    private boolean isFemale;

    @Pattern(regexp = "^(https?://.*)?$", message = "Cover picture must be a valid URL")
    @Schema(defaultValue = "https://cover-picture.jpg")
    private String coverPicture;

    @Pattern(regexp = "^(https?://.*)?$", message = "Profile picture must be a valid URL")
    @Schema(defaultValue = "https://profile-picture.jpg")
    private String profilePicture;

    @Size(max = 100, message = "Bio must be at most 100 characters")
    private String bio;

    private EmployeeStatus employeeStatus;

    private List<UUID> skills;

    @Pattern(regexp = "^(https?://.*)?$", message = "CV must be a valid URL")
    @Schema(defaultValue = "https://cv.pdf")
    private String cv;

    @Size(max = 200, message = "Address must be at most 200 characters")
    private String address;


    @Pattern(regexp = "^\\+\\d{1,3}-\\d{6,10}$" ,message = "Invalid your phone number format example: +855-12345678")
    @Schema(example = "+855-12345678", defaultValue = "+855-12345678")
    private String phoneNumber;



}
