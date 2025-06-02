package com.kshrd.devconnect_springboot.model.entity;

import com.kshrd.devconnect_springboot.model.dto.response.AppUserResponse;
import com.kshrd.devconnect_springboot.model.dto.response.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Recruiter {
    private UUID recruiterId;
    private String companyName;
    private String phoneNumber;
    private String industry;
    private String companyLocation;
    private String bio;
    private LocalDateTime foundedAt;
    private String coverPicture;
    private UserResponse userInformation;
}
