package com.kshrd.devconnect_springboot.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecruiterRequest {
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

    @Pattern(regexp = "^(https?://.*)?$", message = "Profile picture must be a valid URL")
    @Schema(defaultValue = "https://profile-picture.jpg")
    private String profilePicture;


    @Pattern(regexp = "^(?!\\s*$).{1,100}", message = "Company name must contain more than one word and not over 100 characters")
    @Schema(defaultValue = "company name")
    private String companyName;

    @Pattern(regexp = "^\\+\\d{1,3}-\\d{6,10}$" ,message = "Invalid your phone number format example: +855-12345678")
    @Schema(example = "+855-12345678", defaultValue = "+855-12345678")
    private String phoneNumber;

    @Pattern(regexp = "^(?!\\s*$).{1,100}", message = "Industry must contain more than one word and not over 100 characters")
    @Schema(defaultValue = "industry")
    private String industry;

    @Pattern(regexp = "^(?!\\s*$).{1,200}", message = "Company location must contain more than one word and not over 200 characters")
    @Schema(defaultValue = "Phnom Penh, Cambodia")
    private String companyLocation;

    @Pattern(regexp = "^(?!\\s*$).{1,100}", message = "Bio must contain more than one word and not over 100 characters")
    @Schema(defaultValue = "Bio about the recruiter")
    private String bio;

    @Pattern(regexp = "^(?!\\s*$).{1,255}", message = "Company site must contain more than one word and not over 255 characters")
    @Schema(defaultValue = "https://www.companysite.com")
    private String companySite;

    @Past (message = "Founded date must be in the past")
    private LocalDate foundedAt;
    @Pattern(regexp = "^(https?://.*)?$", message = "CV must be a valid URL")
    @Schema(defaultValue = "https://cv.pdf")
    private String coverPicture;
}
