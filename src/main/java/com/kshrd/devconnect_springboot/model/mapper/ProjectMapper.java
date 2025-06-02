package com.kshrd.devconnect_springboot.model.mapper;

import com.kshrd.devconnect_springboot.model.dto.response.ProjectResponse;
import com.kshrd.devconnect_springboot.model.entity.Project;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProjectMapper {
    ProjectResponse toResponse(Project project);
    List<ProjectResponse> toDetailResponse(List<Project> projects);
}
