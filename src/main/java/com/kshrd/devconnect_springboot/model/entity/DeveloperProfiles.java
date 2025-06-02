package com.kshrd.devconnect_springboot.  model.entity;

import com.kshrd.devconnect_springboot.model.enums.EmployeeStatus;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeveloperProfiles {
       private UUID developerId;
       private String firstName;
       private String lastName;
       private boolean isFemale;
       private String profilePicture;
       private String bio;
       private String address;
       private String coverPicture;
       private String cv;
       private EmployeeStatus employeeStatus;
       private String githubUsername;
       private String position;
       private String jobType;
       private String phoneNumber;
       private List<String> skills;
       private UUID userId;

}
