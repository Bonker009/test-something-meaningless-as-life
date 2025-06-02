package com.kshrd.devconnect_springboot.model.dto.response;

import com.kshrd.devconnect_springboot.model.entity.Position;
import com.kshrd.devconnect_springboot.model.entity.Skill;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobResponse {
    private UUID jobId;
    private String salary;
    private String location;
    private Boolean status;
    private Boolean isBookmark;
    private Integer pax;
    private String jobType;
    private String position;
    private List<String> skills;
    private String postedDate;
    private UserResponse creator;
}
