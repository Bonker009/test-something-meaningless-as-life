package com.kshrd.devconnect_springboot.model.mapper;

import com.kshrd.devconnect_springboot.model.dto.response.JobResponse;
import com.kshrd.devconnect_springboot.model.dto.response.JoinJobResponse;
import com.kshrd.devconnect_springboot.model.entity.Jobs;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface JobMapper {
    List<JobResponse> toResponse(List<Jobs> jobs);
    JobResponse toResponse(Jobs jobs);
}
