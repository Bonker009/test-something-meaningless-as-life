package com.kshrd.devconnect_springboot.model.mapper;

import com.kshrd.devconnect_springboot.model.dto.response.DeveloperProfileResponse;
import com.kshrd.devconnect_springboot.model.dto.response.UserResponse;
import com.kshrd.devconnect_springboot.model.entity.DeveloperProfiles;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DeveloperProfileMapper {
    default DeveloperProfileResponse toResponse(DeveloperProfiles entity) {
        DeveloperProfileResponse response = new DeveloperProfileResponse();
        response.setUserId(entity.getUserId());
        response.setFirstName(entity.getFirstName());
        response.setLastName(entity.getLastName());
        response.setFemale(entity.isFemale());
        response.setProfilePicture(entity.getProfilePicture());
        response.setBio(entity.getBio());
        response.setAddress(entity.getAddress());
        response.setCoverPicture(entity.getCoverPicture());
        response.setCv(entity.getCv());
        response.setEmployeeStatus(entity.getEmployeeStatus());
        response.setJobType(entity.getJobType()); // already mapped by MyBatis
        response.setPhoneNumber(entity.getPhoneNumber());
        response.setPosition(entity.getPosition());
        response.setSkills(entity.getSkills());
        return response;
    }
    List<DeveloperProfileResponse> toDetailResponse(List<DeveloperProfiles> developerProfiles);
}

