package com.kshrd.devconnect_springboot.model.mapper;

import com.kshrd.devconnect_springboot.model.dto.response.JoinJobDetailResponse;
import com.kshrd.devconnect_springboot.model.dto.response.JoinJobResponse;
import com.kshrd.devconnect_springboot.model.entity.JoinJob;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface JoinJobDetailMapper {
    List<JoinJobDetailResponse> toResponse(List<JoinJob> joinJobs);
    JoinJobDetailResponse toResponse(JoinJob joinJob);
}
