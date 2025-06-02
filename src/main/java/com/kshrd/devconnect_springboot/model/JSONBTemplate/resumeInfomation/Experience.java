package com.kshrd.devconnect_springboot.model.JSONBTemplate.resumeInfomation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Experience {

    private String companyName;
    private String companyLocation;
    private String position;
    private LocalDate startYear;
    private LocalDate endYear;

}