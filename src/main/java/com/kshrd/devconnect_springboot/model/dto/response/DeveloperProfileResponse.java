package com.kshrd.devconnect_springboot.model.dto.response;

import com.kshrd.devconnect_springboot.model.enums.EmployeeStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeveloperProfileResponse {
    private UUID userId;
    private String firstName;
    private String lastName;
    private boolean isFemale;
    private String profilePicture;
    private String bio;
    private String address;
    private String coverPicture;
    private String cv;
    private EmployeeStatus employeeStatus;
    private String jobType;
    private String position;
    private String phoneNumber;
    private List<String> skills;
}
