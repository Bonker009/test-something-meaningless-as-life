package com.kshrd.devconnect_springboot.model.mapper;

import com.kshrd.devconnect_springboot.model.dto.response.AppUserResponse;
import com.kshrd.devconnect_springboot.model.dto.response.UserResponse;
import com.kshrd.devconnect_springboot.model.entity.AppUser;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AppUserMapper {
    AppUserResponse toResponse(AppUser appUser);
    UserResponse toUserResponse(AppUserResponse appUserResponse);
}
