package com.kshrd.devconnect_springboot.  model.entity;
    
    

import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.kshrd.devconnect_springboot.model.dto.response.UserResponse;
import com.kshrd.devconnect_springboot.model.JSONBTemplate.jobBoard.JobBoard;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Jobs {
       private UUID jobId;
       private String position;
       private String salary;
       private String location;
       private Boolean status;
       private Boolean isBookmark;
       private String description;
       private Date postedDate;
       private UserResponse creator;
       private JobBoard jobBoard;
       private List<String> skills;
       private String jobType;
       private Integer pax;
}
