package com.kshrd.devconnect_springboot.  service;

import com.kshrd.devconnect_springboot.model.dto.response.DeveloperProfileResponse;
import com.kshrd.devconnect_springboot.model.dto.request.DeveloperProfilesRequest;
import jakarta.validation.constraints.Positive;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface DeveloperProfilesService {
    DeveloperProfileResponse getDeveloperProfilesByCurrentUser();
    DeveloperProfileResponse createDeveloperProfiles(DeveloperProfilesRequest entity , UUID jobTypeId , UUID positionId);
    String getGithubUsername();
    Map<String ,String> updateGithubUsername(String githubSyncRequest);
    List<DeveloperProfileResponse> getAllDeveloper(Integer page, Integer size, String name);
}
