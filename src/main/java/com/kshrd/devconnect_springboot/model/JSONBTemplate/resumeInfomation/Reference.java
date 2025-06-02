package com.kshrd.devconnect_springboot.model.JSONBTemplate.resumeInfomation;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.common.aliasing.qual.Unique;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reference {

    @NotBlank(message = "Name is required")
    @Size(max = 50, message = "Name must not exceed 50 characters")
    @Pattern(
            regexp = "^[a-zA-Z\\s]+$",
            message = "Name must contain only letters and spaces "
    )
    private String name;
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\+\\d{1,3}-\\d{6,10}$" ,message = "Invalid reference phone number format example: +855-12345678")
    @Schema(defaultValue = "+855-86451138")
    private String phoneNumber;

    @NotBlank(message = "Job title is required")
    @Size(max = 50 , message = "Job Title not exceed 50 characters")
    @Pattern(
            regexp = "^[a-zA-Z\\s]+$",
            message = "Job Title must contain only letters and spaces "
    )
    private String jobTitle;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Pattern(
            regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$",
            message = "Email must be valid (e.g., user@example.com)"
    )
    private String email;

}