package com.kshrd.devconnect_springboot.model.JSONBTemplate.resumeInfomation.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExperienceRequest {

    @NotBlank(message = "Company Name not allow null")
    @Size(max = 50 , message = "Company name can't greater than 50")
    @Pattern(
            regexp = "^[a-zA-Z\\s]+$",
            message = "Company name must contain only letters and spaces"
    )
    @Valid
    private String companyName;

    @NotBlank(message = "Company Location not allow null")
    @Size(max = 50 , message = "Company Location can't greater than 50")
    @Pattern(
            regexp = "^[a-zA-Z0-9\\s]+$",
            message = "Company Location must contain only letters, number and spaces "
    )
    @Valid
    private String companyLocation;
    @NotNull(message = "position not allow null")
    @Valid
    private UUID position;

    private LocalDate startYear;
    private LocalDate endYear;
}
