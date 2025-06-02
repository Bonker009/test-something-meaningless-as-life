package com.kshrd.devconnect_springboot.service;

import com.kshrd.devconnect_springboot.model.dto.request.InviteProjectRequest;
import com.kshrd.devconnect_springboot.model.dto.request.JoinProjectRequest;
import com.kshrd.devconnect_springboot.model.dto.request.ProjectRequest;
import com.kshrd.devconnect_springboot.model.dto.request.RequestToJoinProject;
import com.kshrd.devconnect_springboot.model.dto.response.ProjectResponse;
import com.kshrd.devconnect_springboot.model.entity.JoinProject;
import com.kshrd.devconnect_springboot.model.entity.Project;
import com.kshrd.devconnect_springboot.model.entity.ProjectPosition;
import com.kshrd.devconnect_springboot.model.enums.DateStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ProjectService {
    List<ProjectResponse> getAllProject(Integer page, Integer size, String name, List<UUID> skills, DateStatus dateStatus);
    Project getProjectById(UUID projectId);
    List<ProjectResponse> getAllProjectByUser(Integer page, Integer size, String name, List<UUID> skills, DateStatus dateStatus);
    Project getProjectByIdAndUser(UUID projectId);
    Project createProject(ProjectRequest projectRequest);
    Project updateProject(UUID projectId, ProjectRequest projectRequest);
    void deleteProject(UUID projectId);
    JoinProject createJoinProject(JoinProjectRequest joinProject);
    JoinProject createReqJoinProject(RequestToJoinProject requestToJoinProject, UUID projectId);
    List<ProjectPosition> getAllPositionByProjectId(UUID projectId);
    ProjectResponse updateProjectStatus(Boolean status, UUID projectId);
    void updateProjectApproval(UUID projectId, UUID developerId);
    void acceptProjectInvite(UUID projectId);
    List<ProjectResponse> getAllProjectHistory(Integer page, Integer size, String name, List<UUID> skills);
    List<ProjectResponse> getAllJoinedProject(Integer page, Integer size, String name, List<UUID> skills);
    Integer getProjectTotalCount(String name, List<UUID> skills, DateStatus dateStatus);
    Integer getProjectTotalUserCount(String name, List<UUID> skills, DateStatus dateStatus);
    Integer getProjectHistoryTotalUserCount(String name, List<UUID> skills);
    List<JoinProject> getJoinProjectByOwner(Integer page, Integer size, UUID positionId, Boolean isInvited, String date);
    List<JoinProject> inviteJoinProject(InviteProjectRequest inviteProjectRequest, UUID projectId);
    Integer getCountAllJoinedProject(String name, List<UUID> skills);
    void deleteJoinProject(UUID projectId);
    void deleteJoinProjectOwner(UUID projectId, UUID developerId);
}
