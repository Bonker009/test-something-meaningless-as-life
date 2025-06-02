package com.kshrd.devconnect_springboot.service.implementation;

import com.kshrd.devconnect_springboot.exception.NotFoundException;
import com.kshrd.devconnect_springboot.model.dto.request.RecruiterRequest;
import com.kshrd.devconnect_springboot.model.dto.response.UserResponse;
import com.kshrd.devconnect_springboot.model.entity.AppUser;
import com.kshrd.devconnect_springboot.model.entity.Recruiter;
import com.kshrd.devconnect_springboot.respository.AppUserRepository;
import com.kshrd.devconnect_springboot.respository.RecruiterRepository;
import com.kshrd.devconnect_springboot.service.RecruiterService;
import com.kshrd.devconnect_springboot.utils.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecruiterServiceImpl implements RecruiterService {
    private final RecruiterRepository recruiterRepository;
    private final AppUserRepository appUserRepository;
    @Override
    public Recruiter getRecruiterProfile() {
        Recruiter recruiter = recruiterRepository.getRecruiterProfile(CurrentUser.appUserId);
        if (recruiter == null) {
            throw new NotFoundException("No profile found");
        }
        return recruiter;
    }

    @Override
    public Recruiter updateRecruiterProfile(RecruiterRequest request) {
        Recruiter recruiter = recruiterRepository.getRecruiterProfile(CurrentUser.appUserId);
        if (recruiter == null) {
            throw new NotFoundException("No profile found");
        }
        AppUser appUser = new AppUser();

        UserResponse userResponses = appUserRepository.getUserResponseById(CurrentUser.appUserId);
        String firstName =request.getFirstName() == null || request.getFirstName().trim().isEmpty() ? userResponses.getFirstName() : request.getFirstName();
        String lastName = request.getLastName() == null || request.getLastName().trim().isEmpty() ?  userResponses.getLastName() : request.getLastName();
        String profileImageUrl = request.getProfilePicture() == null || request.getProfilePicture().trim().isEmpty() ? userResponses.getProfileImageUrl() : request.getProfilePicture();

        appUser.setFirstName(firstName);
        appUser.setLastName(lastName);
        appUser.setProfileImageUrl(profileImageUrl);

        appUserRepository.updateAppUser(appUser, CurrentUser.appUserId);
        Recruiter recruiterProfile = recruiterRepository.updateRecruiterProfile(CurrentUser.appUserId, request);
        UserResponse userResponse = appUserRepository.getUserResponseById(CurrentUser.appUserId);
        recruiterProfile.setUserInformation(userResponse);
        return recruiterProfile;
    }

}
