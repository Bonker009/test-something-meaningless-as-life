package com.kshrd.devconnect_springboot.service.implementation;

import com.kshrd.devconnect_springboot.exception.BadRequestException;
import com.kshrd.devconnect_springboot.exception.NotFoundException;
import com.kshrd.devconnect_springboot.model.dto.request.*;
import com.kshrd.devconnect_springboot.model.dto.response.AppUserResponse;
import com.kshrd.devconnect_springboot.model.dto.response.ProjectResponse;
import com.kshrd.devconnect_springboot.model.entity.*;
import com.kshrd.devconnect_springboot.model.entity.JoinProject;
import com.kshrd.devconnect_springboot.model.entity.Project;
import com.kshrd.devconnect_springboot.model.entity.ProjectPosition;
import com.kshrd.devconnect_springboot.model.enums.DateStatus;
import com.kshrd.devconnect_springboot.model.mapper.ProjectMapper;
import com.kshrd.devconnect_springboot.respository.*;
import com.kshrd.devconnect_springboot.service.ProjectService;
import com.kshrd.devconnect_springboot.utils.CurrentUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectServiceImplement implements ProjectService {
    private final ProjectRepository projectRepository;
    private final ProjectPositionRepository projectPositionRepository;
    private final ProjectSkillRepository projectSkillRepository;
    private final PositionRepository positionRepository;
    private final SkillRepository skillRepository;
    private final JoinProjectRepository joinProjectRepository;
    private final AppUserRepository appUserRepository;
    private final ProjectMapper projectMapper;

    @Override
    public List<ProjectResponse> getAllProject(Integer page, Integer size, String name, List<UUID> skills, DateStatus dateStatus) {
        page = (page - 1) * size;
        List<ProjectResponse> projectResponses = projectMapper.toDetailResponse(projectRepository.getAllProject(page, size, CurrentUser.appUserId, name, skills, dateStatus));
        if (projectResponses.isEmpty()) {
            throw new NotFoundException("Project is empty");
        }
        System.out.println(projectResponses.getFirst());
        return projectResponses;
    }

    @Override
    public Project getProjectById(UUID projectId) {
        Project project = projectRepository.getProjectById(projectId);
        if (project == null) {
            throw new NotFoundException("Project not found");
        }
        return project;
    }

    @Override
    public List<ProjectResponse> getAllProjectByUser(Integer page, Integer size, String name, List<UUID> skills, DateStatus dateStatus) {
        page = (page - 1) * size;
        List<ProjectResponse> projectResponses = projectMapper.toDetailResponse(projectRepository.getAllProjectByUser(CurrentUser.appUserId, page, size, name, skills, dateStatus));
        if (projectResponses.isEmpty()) {
            throw new NotFoundException("Project is empty");
        }
        return projectResponses;
    }

    @Override
    public Project getProjectByIdAndUser(UUID projectId) {
        Project project = projectRepository.getProjectByIdAndUser(CurrentUser.appUserId, projectId);
        if (project == null) {
            throw new NotFoundException("Project not found");
        }
        return project;
    }

    @Override
    public Project createProject(ProjectRequest projectRequest) {
        if (projectRequest.getPositions().isEmpty() || projectRequest.getSkills().isEmpty()) {
            throw new BadRequestException("Position or skills should not be empty");
        }
        if (projectRequest.getStartAt().isAfter(projectRequest.getEndAt()) || projectRequest.getStartAt().getDayOfMonth() == projectRequest.getEndAt().getDayOfMonth()) {
            throw new BadRequestException("Invalid progress date");
        }

        List<UUID> skills = projectRequest.getSkills();

        Set<UUID> uniqueSkills = new HashSet<>(skills);
        if (uniqueSkills.size() != skills.size()) {
            throw new BadRequestException("Cannot have duplicate skills");
        }

        for (UUID skillId : uniqueSkills) {
            if (skillRepository.getSkillById(skillId) == null) {
                throw new NotFoundException("Skill not found: " + skillId);
            }
        }


        List<ProjectPositionRequest> positions = projectRequest.getPositions();
        Set<UUID> uniquePositionIds = new HashSet<>();

        for (ProjectPositionRequest p : positions) {
            UUID positionId = p.getPositionId();

            if (!uniquePositionIds.add(positionId)) {
                throw new BadRequestException("Cannot have duplicate positions");
            }

            if (positionRepository.getPositionById(positionId) == null) {
                throw new NotFoundException("Position not found: " + positionId);
            }

            if (p.getMaxMembers() <= 0) {
                throw new BadRequestException("Max members should be at least 1");
            }
        }
        projectRequest.setTitle(projectRequest.getTitle().trim());
        projectRequest.setDescription(projectRequest.getDescription().trim());
        Project project = projectRepository.createProjectByUser(CurrentUser.appUserId, projectRequest);
        for (UUID s : projectRequest.getSkills()) {
            projectSkillRepository.createProjectSkill(project.getProjectId(), s);
        }
        for (ProjectPositionRequest p : projectRequest.getPositions()) {
            projectPositionRepository.createProjectPosition(p.getMaxMembers(), project.getProjectId(), p.getPositionId());
        }
        return getProjectById(project.getProjectId());
    }

    @Override
    public Project updateProject(UUID projectId, ProjectRequest projectRequest) {
        projectRequest.setTitle(projectRequest.getTitle().trim());
        projectRequest.setDescription(projectRequest.getDescription().trim());
        Project project1 = projectRepository.getProjectById(projectId);
        if (project1 == null) {
            throw new NotFoundException("Project not found");
        }
        if (LocalDateTime.now().isAfter(project1.getStartAt())) {
            throw new BadRequestException("Project already start");
        }
        if (joinProjectRepository.getAllJoinProjectByProjectId(projectId) != null) {
            throw new BadRequestException("Developer already join");
        }

        if (!project1.getOwner().getUserId().equals(CurrentUser.appUserId)) {
            throw new BadRequestException("You are not the owner");
        }
        if (projectRequest.getPositions().isEmpty() || projectRequest.getSkills().isEmpty()) {
            throw new BadRequestException("Position or skills should not be empty");
        }

        if (projectRequest.getStartAt().isAfter(projectRequest.getEndAt()) || projectRequest.getStartAt().equals(projectRequest.getEndAt())) {
            throw new BadRequestException("Invalid progress date");
        }

        List<UUID> skills = projectRequest.getSkills();

        Set<UUID> uniqueSkills = new HashSet<>(skills);
        if (uniqueSkills.size() != skills.size()) {
            throw new BadRequestException("Cannot have duplicate skills");
        }

        for (UUID skillId : uniqueSkills) {
            if (skillRepository.getSkillById(skillId) == null) {
                throw new NotFoundException("Skill not found: " + skillId);
            }
        }


        List<ProjectPositionRequest> positions = projectRequest.getPositions();
        Set<UUID> uniquePositionIds = new HashSet<>();

        for (ProjectPositionRequest p : positions) {
            UUID positionId = p.getPositionId();

            if (!uniquePositionIds.add(positionId)) {
                throw new BadRequestException("Cannot have duplicate positions");
            }

            if (positionRepository.getPositionById(positionId) == null) {
                throw new NotFoundException("Position not found: " + positionId);
            }

            if (p.getMaxMembers() <= 0) {
                throw new BadRequestException("Max members should be at least 1");
            }
        }

        Project project = projectRepository.updateProject(projectId, projectRequest);
        projectSkillRepository.deleteAllProjectSkill(projectId);
        projectPositionRepository.deleteAllProjectPosition(projectId);
        for (UUID s : projectRequest.getSkills()) {
            projectSkillRepository.createProjectSkill(project.getProjectId(), s);
        }
        for (ProjectPositionRequest p : projectRequest.getPositions()) {
            projectPositionRepository.createProjectPosition(p.getMaxMembers(), project.getProjectId(), p.getPositionId());
        }
        return getProjectById(project.getProjectId());
    }

    @Override
    public void deleteProject(UUID projectId) {
        Project project = projectRepository.getProjectByIdAndUser(CurrentUser.appUserId, projectId);
        Project project1 = projectRepository.getProjectById(projectId);
        if (project1 == null) {
            throw new NotFoundException("Project not found");
        }
        if (project == null) {
            throw new NotFoundException("Not the owner of this project");
        }
        projectRepository.deleteProject(CurrentUser.appUserId, projectId);
    }

    @Override
    public JoinProject createJoinProject(JoinProjectRequest joinProject) {
        joinProject.setTitle(joinProject.getTitle().trim());
        joinProject.setDescription(joinProject.getDescription().trim());
        Project project = projectRepository.getProjectById(joinProject.getProjectId());
        if (project == null)
            throw new NotFoundException("Project not found");

        if (project.getOwner().getUserId().equals(joinProject.getDeveloperId()))
            throw new BadRequestException("Owner of the project can't join");

        if (!project.getOwner().getUserId().equals(CurrentUser.appUserId))
            throw new BadRequestException("You are not the owner");

        if (positionRepository.getPositionById(joinProject.getPositionId()) == null)
            throw new NotFoundException("Position not found");

        if (projectPositionRepository.getPositionByProject(project.getProjectId(), joinProject.getPositionId()) == null)
            throw new NotFoundException("Position don't exist in the project");

        if (appUserRepository.getUserById(joinProject.getDeveloperId()) == null)
            throw new NotFoundException("Developer not found");

        if (appUserRepository.getUserById(joinProject.getDeveloperId()).getIsRecruiter())
            throw new BadRequestException("Only developer allow to join");

        if (joinProjectRepository.getJoinProjectByDeveloperIdAndProjectId(project.getProjectId(), joinProject.getDeveloperId()) != null)
            throw new NotFoundException("Developer already join");

        if (LocalDateTime.now().isAfter(project.getStartAt()))
            throw new BadRequestException("Not allow to join since the project is already pass start date");

//        if maxMember < approved join should throw error
        for (ProjectPosition p : project.getPositions()) {
            if (joinProjectRepository.getApprovedCount(project.getProjectId(), p.getPositionId())
                    >= projectPositionRepository.getPositionByProject(project.getProjectId(), p.getPositionId()).getMaxMembers()) {
                throw new BadRequestException("Project position already full");
            }
        }
        return joinProjectRepository.createJoinProject(joinProject, true);
    }

    @Override
    public JoinProject createReqJoinProject(RequestToJoinProject requestToJoinProject, UUID projectId) {
        Project project = projectRepository.getProjectById(projectId);
        if (project == null)
            throw new NotFoundException("Project not found");

        if (project.getOwner().getUserId().equals(CurrentUser.appUserId))
            throw new BadRequestException("Owner of the project can't join");

        if (positionRepository.getPositionById(requestToJoinProject.getPositionId()) == null)
            throw new NotFoundException("Position not found");

        if (projectPositionRepository.getPositionByProject(project.getProjectId(), requestToJoinProject.getPositionId()) == null)
            throw new NotFoundException("Position don't exist in the project");

        if (joinProjectRepository.getJoinProjectByDeveloperIdAndProjectId(project.getProjectId(), CurrentUser.appUserId) != null)
            throw new NotFoundException("Developer already join");

        if (LocalDateTime.now().isAfter(project.getStartAt()))
            throw new BadRequestException("Not allow to join since the project is already pass start date");

//        if maxMember < approved join should throw error
        for (ProjectPosition p : project.getPositions()) {
            if (joinProjectRepository.getApprovedCount(project.getProjectId(), p.getPositionId())
                    >= projectPositionRepository.getPositionByProject(project.getProjectId(), p.getPositionId()).getMaxMembers()) {
                throw new BadRequestException("Project position already full");
            }
        }
        return joinProjectRepository.createReqJoinProject(requestToJoinProject, CurrentUser.appUserId, projectId);
    }

    @Override
    public List<ProjectPosition> getAllPositionByProjectId(UUID projectId) {
        Project project = projectRepository.getProjectById(projectId);
        if (project == null) {
            throw new NotFoundException("Project not found");
        }
        List<ProjectPosition> projectPositions = projectPositionRepository.getAllProjectPositionById(projectId);
        if (projectPositions.isEmpty()) {
            throw new NotFoundException("Project position is empty");
        }
        return projectPositions;
    }

    @Override
    public ProjectResponse updateProjectStatus(Boolean status, UUID projectId) {
        Project project = projectRepository.getProjectById(projectId);
        if (project == null) {
            throw new NotFoundException("Project not found");
        }
        AppUserResponse appUser = appUserRepository.getUserById(CurrentUser.appUserId);
        if (project.getOwner().getUserId().compareTo(appUser.getUserId()) != 0) {
            throw new BadRequestException("You are not the owner of the project");
        }
        return projectMapper.toResponse(projectRepository.updateProjectStatus(status, projectId, CurrentUser.appUserId));
    }

    @Override
    public void updateProjectApproval(UUID projectId, UUID developerId) {
        Project project = projectRepository.getProjectById(projectId);
        if (project == null) throw new NotFoundException("Project not found");
        AppUserResponse appUser = appUserRepository.getUserById(developerId);
        if (appUser == null) throw new NotFoundException("Developer not found");

        if (project.getOwner().getUserId().compareTo(CurrentUser.appUserId) != 0) {
            throw new BadRequestException("You are not the owner of this project");
        }

        if (appUser.getIsRecruiter()) throw new BadRequestException("Unauthorized user");
        JoinProject joinProject = joinProjectRepository.getJoinProjectByDeveloperIdAndProjectId(projectId, developerId);
        if (joinProject == null)
            throw new NotFoundException("Developer didn't apply to this project");

        if (joinProject.getIsInvited())
            throw new NotFoundException("Cannot accept! This is invited");

        for (ProjectPosition p : project.getPositions()) {
            if (joinProjectRepository.getApprovedCount(project.getProjectId(), p.getPositionId())
                    >= projectPositionRepository.getPositionByProject(project.getProjectId(), p.getPositionId()).getMaxMembers()) {
                throw new BadRequestException("Project position already full");
            }
        }

        joinProjectRepository.updateApprovalStatus(projectId, developerId);
    }

    @Override
    public void acceptProjectInvite(UUID projectId) {
        Project project = projectRepository.getProjectById(projectId);
        if (project == null) throw new NotFoundException("Project not found");

        if (project.getOwner().getUserId().compareTo(CurrentUser.appUserId) != 0) {
            throw new BadRequestException("You are not the owner of this project");
        }

        for (ProjectPosition p : project.getPositions()) {
            if (joinProjectRepository.getApprovedCount(project.getProjectId(), p.getPositionId())
                    >= projectPositionRepository.getPositionByProject(project.getProjectId(), p.getPositionId()).getMaxMembers()) {
                throw new BadRequestException("Project position already full");
            }
        }
        JoinProject joinProject = joinProjectRepository.getJoinProjectByDeveloperIdAndProjectId(projectId, CurrentUser.appUserId);
        if (joinProject == null) {
            throw new NotFoundException("You have not join yet");
        }
        if (!joinProject.getIsInvited()) {
            throw new BadRequestException("Your not invited");
        }
        joinProjectRepository.acceptProjectInvite(projectId, CurrentUser.appUserId);
    }

    @Override
    public List<ProjectResponse> getAllProjectHistory(Integer page, Integer size, String name, List<UUID> skills) {
        page = (page - 1) * size;
        List<ProjectResponse> projectResponses = projectMapper.toDetailResponse(projectRepository.getAllProjectHistory(page, size, name, skills, CurrentUser.appUserId));
        if (projectResponses.isEmpty()) {
            throw new NotFoundException("Created project history is empty");
        }
        return projectResponses;
    }

    @Override
    public List<ProjectResponse> getAllJoinedProject(Integer page, Integer size, String name, List<UUID> skills) {
        page = (page - 1) * size;
        List<ProjectResponse> projectResponses = projectMapper.toDetailResponse(projectRepository.getAllJoinedProject(page, size, name, skills, CurrentUser.appUserId));
        if (projectResponses.isEmpty()) {
            throw new NotFoundException("Joined project history is empty");
        }
        return projectResponses;
    }

    @Override
    public Integer getProjectTotalCount(String name, List<UUID> skills, DateStatus dateStatus) {
        return projectRepository.getProjectTotalCount(CurrentUser.appUserId, name, skills, dateStatus);
    }

    @Override
    public Integer getProjectTotalUserCount(String name, List<UUID> skills, DateStatus dateStatus) {
        return projectRepository.getProjectTotalUserCount(CurrentUser.appUserId, name, skills, dateStatus);
    }

    @Override
    public Integer getProjectHistoryTotalUserCount(String name, List<UUID> skills) {
        return projectRepository.getProjectHistoryTotalUserCount(name, skills, CurrentUser.appUserId);
    }

    @Override
    public List<JoinProject> getJoinProjectByOwner(Integer page, Integer size, UUID positionId, Boolean isInvited, String date) {
        page = (page - 1) * size;
        List<JoinProject> joinProjects = joinProjectRepository.getJoinProjectByOwner(page, size, CurrentUser.appUserId, positionId, isInvited, date);
        if (joinProjects.isEmpty()) {
            throw new NotFoundException("Join project is empty");
        }
        return joinProjects;
    }

    @Override
    public List<JoinProject> inviteJoinProject(InviteProjectRequest inviteProjectRequest, UUID projectId) {
        List<JoinProject> joinProjects = new ArrayList<>();
        JoinProjectRequest joinProjectRequest = new JoinProjectRequest();
        Set<UUID> seen = new HashSet<>();
            for (UUID devId : inviteProjectRequest.getDeveloperId()) {
                if(!seen.add(devId)) throw new BadRequestException("Cannot have duplicate developer");;
                joinProjectRequest.setTitle(inviteProjectRequest.getTitle().trim());
                joinProjectRequest.setDescription(inviteProjectRequest.getDescription().trim());
                joinProjectRequest.setProjectId(projectId);
                joinProjectRequest.setDeveloperId(devId);
                joinProjectRequest.setPositionId(inviteProjectRequest.getPositionId());
                joinProjects.add(createJoinProject(joinProjectRequest));
            }

        return joinProjects;
    }

    @Override
    public Integer getCountAllJoinedProject(String name, List<UUID> skills) {
        return projectRepository.getCountAllJoinedProject(name, skills, CurrentUser.appUserId);
    }

    @Override
    public void deleteJoinProject(UUID projectId) {
        JoinProject joinProject = joinProjectRepository.getJoinProjectByDeveloperIdAndProjectId(projectId, CurrentUser.appUserId);
        if (joinProject == null) {
            throw new NotFoundException("Not found");
        }
        if (joinProject.getIsApproved()) {
            throw new BadRequestException("Already approved you can't cancel");
        }

        joinProjectRepository.deleteJoinProject(projectId, CurrentUser.appUserId);
    }

    @Override
    public void deleteJoinProjectOwner(UUID projectId, UUID developerId) {
        Project project = projectRepository.getProjectById(projectId);
        if (project == null) {
            throw new NotFoundException("Project not found");
        }
        if (!project.getOwner().getUserId().equals(CurrentUser.appUserId)) {
            throw new BadRequestException("Not the owner");
        }
        JoinProject joinProject = joinProjectRepository.getJoinProjectByDeveloperIdAndProjectId(projectId, developerId);
        if (joinProject == null) {
            throw new NotFoundException("Not found");
        }
        if (joinProject.getIsApproved()) {
            throw new BadRequestException("Already approved you can't cancel");
        }
        joinProjectRepository.deleteJoinProject(projectId, developerId);
    }

}
