package com.kshrd.devconnect_springboot.  controller;

import com.kshrd.devconnect_springboot.base.ApiResponse;
import com.kshrd.devconnect_springboot.base.BaseController;
import com.kshrd.devconnect_springboot.model.dto.request.DeveloperProfilesRequest;
import com.kshrd.devconnect_springboot.model.dto.response.DeveloperProfileResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import org.springframework.web.bind.annotation.*;
import com.kshrd.devconnect_springboot.service.DeveloperProfilesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.UUID;


@RestController
@RequestMapping("/api/developer-profile")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class DeveloperProfilesController extends BaseController {

    private final DeveloperProfilesService developerProfilesService;

    @GetMapping()
    public ResponseEntity<ApiResponse<DeveloperProfileResponse>> getCurrentProfileDev() {
        DeveloperProfileResponse entity = developerProfilesService.getDeveloperProfilesByCurrentUser();
        return response("DeveloperProfiles retrieved successfully", entity);
    }

    @PutMapping("/complete-profile")
    public ResponseEntity<ApiResponse<DeveloperProfileResponse>> completeProfiDeveloperProfiles(@RequestBody @Valid  DeveloperProfilesRequest entity ,
                                                                                          @RequestParam(required = false)
                                                                                          UUID positionId,
                                                                                         @RequestParam(required = false)
                                                                                             UUID jobTypeId
    ) {
        DeveloperProfileResponse service = developerProfilesService.createDeveloperProfiles(entity , jobTypeId, positionId);
        return response("Developer Profiles have been created successfully", service);
    }


    @GetMapping("/get-github-username")
    public ResponseEntity<ApiResponse<String>> getGithubUsername() {
        String githubUsername = developerProfilesService.getGithubUsername();
        return response("Github username retrieved successfully", githubUsername);
    }

    @GetMapping("/all-developer")
    @Operation(summary = "Get all developer")
    public ResponseEntity<ApiResponse<List<DeveloperProfileResponse>>> getAllDeveloper( @RequestParam(defaultValue = "1") @Positive Integer page,
                                                                                        @RequestParam(defaultValue = "10") @Positive Integer size,
                                                                                        @RequestParam(required = false) String name){
        return response("Get all developer successful", developerProfilesService.getAllDeveloper(page, size, name));
    }

    @PostMapping("/add-github-username/{github-username}")
    public ResponseEntity<ApiResponse<String>> addGithubUsername(@PathVariable("github-username")
                                                                     @Pattern(regexp = "^[a-zA-Z0-9_-]{1,39}$", message = "Invalid GitHub username format")
                                                                     String githubUsername) {
        Map<String ,String> response = developerProfilesService.updateGithubUsername(githubUsername);
        return response(response.get("message"), response.get("github"));
    }

}
