package com.kshrd.devconnect_springboot.controller;

import com.kshrd.devconnect_springboot.base.ApiResponse;
import com.kshrd.devconnect_springboot.base.BaseController;
import com.kshrd.devconnect_springboot.model.dto.request.InviteProjectRequest;
import com.kshrd.devconnect_springboot.model.dto.request.JoinProjectRequest;
import com.kshrd.devconnect_springboot.model.dto.request.ProjectRequest;
import com.kshrd.devconnect_springboot.model.dto.request.RequestToJoinProject;
import com.kshrd.devconnect_springboot.model.dto.response.ProjectResponse;
import com.kshrd.devconnect_springboot.model.entity.JoinProject;
import com.kshrd.devconnect_springboot.model.entity.Project;
import com.kshrd.devconnect_springboot.model.entity.ProjectPosition;
import com.kshrd.devconnect_springboot.model.enums.DateStatus;
import com.kshrd.devconnect_springboot.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/projects")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class ProjectController extends BaseController {

    private final ProjectService projectService;

    @GetMapping
    @Operation(summary = "Get all projects")
    public ResponseEntity<ApiResponse<List<ProjectResponse>>> getAllProjects(
             @RequestParam(defaultValue = "1") @Positive Integer page,
             @RequestParam(defaultValue = "10") @Positive Integer size,
             @RequestParam(required = false) String projectName,
             @RequestParam(required = false) List<UUID> skills,
             @RequestParam(required = false) @Schema(description = "Filter project by status") DateStatus dateStatus
                                                                             ) {
        return response("Fetched all project successfully",
                projectService.getAllProject(page, size, projectName, skills, dateStatus), page, size,
                projectService.getProjectTotalCount(projectName, skills, dateStatus));
    }

    @GetMapping("/{project-id}")
    @Operation(summary = "Get project by id")
    public ResponseEntity<ApiResponse<Project>> getProjectById(@PathVariable("project-id") UUID projectId) {
        return response("Get project by id successfully", projectService.getProjectById(projectId));
    }

    @GetMapping("/users")
    @Operation(summary = "Get all project by user")
    public ResponseEntity<ApiResponse<List<ProjectResponse>>> getAllProjectByUser(
            @RequestParam(defaultValue = "1") @Positive Integer page,
            @RequestParam(defaultValue = "10") @Positive Integer size,
            @RequestParam(required = false) String projectName,
            @RequestParam(required = false) List<UUID> skills,
            @RequestParam(required = false) @Schema(description = "Filter project by status") DateStatus dateStatus) {
        return response("Get project by user successfully",
                projectService.getAllProjectByUser(page, size, projectName, skills, dateStatus),
                page, size, projectService.getProjectTotalUserCount(projectName, skills, dateStatus)
        );
    }

    @GetMapping("/users/{project-id}")
    @Operation(summary = "Get project by id and user")
    public ResponseEntity<ApiResponse<Project>> getProjectByIdAndUser(@PathVariable("project-id") UUID projectId) {
        return response("Get project by id successfully", projectService.getProjectByIdAndUser(projectId));
    }

    @PostMapping("/create")
    @Operation(summary = "Create project")
    public ResponseEntity<ApiResponse<Project>> createProject(@Valid @RequestBody ProjectRequest projectRequest) {
        return response("Create project successfully", HttpStatus.CREATED ,projectService.createProject(projectRequest));
    }

    @PutMapping("/{project-id}")
    @Operation(summary = "Update project by id")
    public ResponseEntity<ApiResponse<Project>> updateProjectById(@PathVariable("project-id") UUID projectId,
                                                         @Valid @RequestBody ProjectRequest projectRequest) {
        return response("Update project successfully", projectService.updateProject(projectId, projectRequest));
    }

    @DeleteMapping("/{project-id}")
    @Operation(summary = "Delete project by id and user")
    public ResponseEntity<ApiResponse<Object>> deleteProjectByIdAndUser(@PathVariable("project-id") UUID projectId) {
        projectService.deleteProject(projectId);
        return response("Deleted project successfully");
    }

    @PatchMapping("/update-status/{project-id}")
    @Operation(summary = "Update project status to close")
    public ResponseEntity<ApiResponse<ProjectResponse>> updateProjectStatus(@RequestParam(defaultValue = "true") Boolean status, @PathVariable("project-id") UUID projectId) {

        return response("Update project status successfully", projectService.updateProjectStatus(status, projectId));
    }

    @PatchMapping("/update-join-status/{project-id}/{developer-id}")
    @Operation(summary = "Update project status approval")
    public ResponseEntity<ApiResponse<Object>> updateApprovalTrue(@PathVariable("project-id") UUID projectId, @PathVariable("developer-id") UUID developerId) {
        projectService.updateProjectApproval(projectId, developerId);
        return response("Update developer join project approval successfully");
    }

    @PostMapping("/invite/join/{project-id}")
    @Operation(summary = "Invite to join project")
    public ResponseEntity<ApiResponse<List<JoinProject>>> createInviteJoinProject(
            @PathVariable("project-id") UUID projectId,
            @RequestBody InviteProjectRequest inviteProjectRequest) {
        return response("Invite to join project successfully", HttpStatus.CREATED, projectService.inviteJoinProject(inviteProjectRequest, projectId));
    }

    @PostMapping("/request/join/{project-id}")
    @Operation(summary = "Request to join project")
    public ResponseEntity<ApiResponse<JoinProject>> createRequestJoinProject( @PathVariable("project-id") UUID projectId,@RequestBody RequestToJoinProject requestToJoinProject) {
        return response("Request to join project successfully", HttpStatus.CREATED, projectService.createReqJoinProject(requestToJoinProject, projectId));
    }

    @GetMapping("/position/{project-id}")
    @Operation(summary = "Get all position in a project by id")
    public ResponseEntity<ApiResponse<List<ProjectPosition>>> getAllPositionByProjectId(@PathVariable("project-id") UUID projectId) {
        return response("Get position by project successfully", projectService.getAllPositionByProjectId(projectId));
    }

    @GetMapping("/project-history")
    @Operation(summary= "History of created project")
    public ResponseEntity<ApiResponse<List<ProjectResponse>>> getAllProjectHistory(  @RequestParam(defaultValue = "1") @Positive Integer page,
                                                                                     @RequestParam(defaultValue = "10") @Positive Integer size,
                                                                                   @RequestParam(required = false) String projectName,
                                                                                   @RequestParam(required = false) List<UUID> skills) {
        return response("Get all joined project successfully", projectService.getAllProjectHistory(page, size, projectName, skills), page, size, projectService.getProjectHistoryTotalUserCount(projectName, skills));
    }

    @GetMapping("/join-project-history")
    @Operation(summary= "History of joined project")
    public ResponseEntity<ApiResponse<List<ProjectResponse>>> getAllJoinedProject(  @RequestParam(defaultValue = "1") @Positive Integer page,
                                                                                     @RequestParam(defaultValue = "10") @Positive Integer size,
                                                                                     @RequestParam(required = false) String projectName,
                                                                                     @RequestParam(required = false) List<UUID> skills) {
        return response("Get all joined project successfully", projectService.getAllJoinedProject(page, size, projectName, skills), page, size, projectService.getCountAllJoinedProject(projectName, skills));
    }

    @GetMapping("/join-project")
    @Operation(summary = "Get all developer that join by request or invite in the project")
    public ResponseEntity<ApiResponse<List<JoinProject>>> getJoinProjectByOwner(  @RequestParam(defaultValue = "1") @Positive Integer page,
                                                                                  @RequestParam(defaultValue = "10") @Positive Integer size,
                                                                                @RequestParam(defaultValue = "false") Boolean isInvited,
                                                                                @RequestParam(required = false) UUID positionId,
                                                                                  @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String date) {
        return response("Get all developer join the project", projectService.getJoinProjectByOwner(page, size, positionId, isInvited, date));
    }

// for owner of the project
    @DeleteMapping("/join-project/{project-id}")
    @Operation(summary = "Reject or Cancel invitation or request for owner of the project")
    public ResponseEntity<ApiResponse<Object>> deleteJoinProject(@PathVariable("project-id") UUID projectId,
                                                                 @RequestParam UUID developerId) {
        projectService.deleteJoinProjectOwner(projectId,developerId);
        return response("Delete success");

    }

//    not yet approve so allow the participant to remove request
    @DeleteMapping("/join-project/cancel/{project-id}")
    @Operation(summary = "Cancel request")
    public ResponseEntity<ApiResponse<Object>> cancelRequest(@PathVariable("project-id") UUID projectId) {
        projectService.deleteJoinProject(projectId);
        return response("Delete success");
    }

    @PatchMapping("/accept-invite/{project-id}")
    @Operation(summary = "Accept invite")
    public ResponseEntity<ApiResponse<Object>> acceptProjectInvite(@PathVariable("project-id") UUID projectId) {
        projectService.acceptProjectInvite(projectId);
        return response("Invitation accepted");
    }

}
