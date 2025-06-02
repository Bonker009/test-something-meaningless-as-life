package com.kshrd.devconnect_springboot.service.implementation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

import com.kshrd.devconnect_springboot.exception.BadRequestException;
import com.kshrd.devconnect_springboot.exception.NotFoundException;
import com.kshrd.devconnect_springboot.model.dto.request.DeveloperProfilesRequest;
import com.kshrd.devconnect_springboot.model.dto.response.DeveloperProfileResponse;
import com.kshrd.devconnect_springboot.model.dto.response.UserResponse;
import com.kshrd.devconnect_springboot.model.entity.AppUser;
import com.kshrd.devconnect_springboot.model.entity.DeveloperProfiles;
import com.kshrd.devconnect_springboot.model.enums.EmployeeStatus;
import com.kshrd.devconnect_springboot.model.mapper.DeveloperProfileMapper;

import com.kshrd.devconnect_springboot.respository.*;
import com.kshrd.devconnect_springboot.service.DeveloperProfilesService;
import com.kshrd.devconnect_springboot.utils.CurrentUser;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class DeveloperProfilesServiceImplement implements DeveloperProfilesService {

    private final DeveloperProfilesRepository repository;
    private final AppUserRepository appUserRepository;
    private final DeveloperProfileMapper developerProfileMapper;
    private final SkillRepository skillRepository;
    private final JobsRepository jobsRepository;
    private final PositionRepository positionRepository;
    private final DeveloperProfilesRepository developerProfilesRepository;

    @Override
    public DeveloperProfileResponse getDeveloperProfilesByCurrentUser() {
        DeveloperProfiles developerProfiles = repository.selectDeveloperProfilesByUserId(CurrentUser.appUserId);
        if (developerProfiles == null) {
            throw new NotFoundException("Developer profile not found");
        }
        UserResponse userResponse = appUserRepository.getUserResponseById(CurrentUser.appUserId);
        developerProfiles.setFirstName(userResponse.getFirstName());
        developerProfiles.setLastName(userResponse.getLastName());
        developerProfiles.setProfilePicture(userResponse.getProfileImageUrl());

        return developerProfileMapper.toResponse(developerProfiles);
    }

    @Override
    public DeveloperProfileResponse createDeveloperProfiles(DeveloperProfilesRequest request ,UUID jobTypeId, UUID positionId) {

            normalizeRequest(request);

            //! validate for update user information
            AppUser appUser = new AppUser();
            UserResponse userResponses = appUserRepository.getUserResponseById(CurrentUser.appUserId);

            String firstName =request.getFirstName() == null || request.getFirstName().trim().isEmpty() ? userResponses.getFirstName() : request.getFirstName();
            String lastName = request.getLastName() == null || request.getLastName().trim().isEmpty() ?  userResponses.getLastName() : request.getLastName();
            String profileImageUrl = request.getProfilePicture() == null || request.getProfilePicture().trim().isEmpty() ? userResponses.getProfileImageUrl() : request.getProfilePicture();

            appUser.setFirstName(firstName);
            appUser.setLastName(lastName);
            appUser.setProfileImageUrl(profileImageUrl);

            appUserRepository.updateAppUser(appUser, CurrentUser.appUserId);

            if(request.getEmployeeStatus() != EmployeeStatus.UNEMPLOYED) {
                if (jobTypeId == null || positionId == null) {
                    throw new NotFoundException("Job type and position not allow null when EMPLOYED or FREELANCE");
                }
            }else {
                if( jobTypeId != null || positionId != null)
                    throw new BadRequestException("Job type and position not required when UNEMPLOYED");
            }

            if (jobsRepository.selectJobTypeById(jobTypeId) == null && request.getEmployeeStatus() != EmployeeStatus.UNEMPLOYED)
                throw new NotFoundException("Job type not found");

            if(positionRepository.getPositionById(positionId) == null && request.getEmployeeStatus() != EmployeeStatus.UNEMPLOYED)
                throw new NotFoundException("Position not found");

            for (UUID skillId : request.getSkills()) {
                if (skillRepository.getSkillById(skillId) == null) {
                    throw new NotFoundException("Skill not found");
                }
                for (UUID checkId : repository.getSkillsIdByUserId(CurrentUser.appUserId)) {
                    if (checkId.equals(skillId)) {
                        repository.deleteDeveloperSkills(skillId);
                    }
                }
            }
            for (UUID skillId : request.getSkills()) {
                repository.insertDeveloperSkills(skillId, CurrentUser.appUserId);
            }
            DeveloperProfiles profile =  repository.updateDeveloperProfiles(request,jobTypeId ,positionId , CurrentUser.appUserId);
            UserResponse userResponse = appUserRepository.getUserResponseById(CurrentUser.appUserId);
            profile.setFirstName(userResponse.getFirstName());
            profile.setLastName(userResponse.getLastName());
            profile.setProfilePicture(userResponse.getProfileImageUrl());

            return developerProfileMapper.toResponse(profile);

    }


    @Override
    public String getGithubUsername() {
        String github = repository.getGithubUsername(CurrentUser.appUserId);
        if (github == null) {
            throw new NotFoundException("Github username not found");
        }
        return github;
    }

    @Override
    public Map<String ,String> updateGithubUsername(String githubSyncRequest) {
        String github = repository.getGithubUsername(CurrentUser.appUserId);
        HashMap<String, String> response = new HashMap<>();
        String message;
        if (github != null) {
            message = "Github username updated successfully";
        }else {
            message = "Github username created successfully";
        }
        repository.gitHubSync(githubSyncRequest , CurrentUser.appUserId);
        response.put("githubSyncRequest", github);
        response.put("message", message);

        return response;
    }

    @Override
    public List<DeveloperProfileResponse> getAllDeveloper(Integer page, Integer size, String name) {
        page = (page - 1) * size;
        List<DeveloperProfileResponse> developerProfileResponses = developerProfileMapper.toDetailResponse(developerProfilesRepository.getAllDeveloper(page, size, name));
        if (developerProfileResponses.isEmpty()) {
            throw new NotFoundException("No developers");
        }
        return developerProfileResponses;
    }

    // This method normalizes the request by setting null values for empty strings
    private void normalizeRequest(DeveloperProfilesRequest request) {
        if (!StringUtils.hasText(request.getFirstName())) {
            request.setFirstName(null);
        }
        if (!StringUtils.hasText(request.getLastName())) {
            request.setLastName(null);
        }
        if (!StringUtils.hasText(request.getProfilePicture())) {
            request.setProfilePicture(null);
        }
    }
}