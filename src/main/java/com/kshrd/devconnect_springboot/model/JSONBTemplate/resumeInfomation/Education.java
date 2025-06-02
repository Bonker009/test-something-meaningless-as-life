package com.kshrd.devconnect_springboot.model.JSONBTemplate.resumeInfomation;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Education {
    @NotBlank(message = "School Name not allow null")
    @Size(max = 50 , message = "School name can't greater than 50")
    @Pattern(
            regexp = "^[a-zA-Z\\s]+$",
            message = "School Name must contain only letters and spaces "
    )
    private String schoolName;

    @NotBlank(message = "School Location not allow null")
    @Size(max = 50 , message = "School Location can't greater than 50")
    @Pattern(
            regexp = "^[a-zA-Z0-9,\\s]+$",
            message = "School Name must contain only letters, number, and spaces "
    )
    private String schoolLocation;
    @NotNull(message = "Start Year not allow null")
    private LocalDate startYear;
    @NotNull(message = "End Year not allow null")
    private LocalDate endYear;
}