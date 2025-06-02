package com.kshrd.devconnect_springboot.respository;

import com.kshrd.devconnect_springboot.model.dto.response.AppUserResponse;
import com.kshrd.devconnect_springboot.model.dto.response.UserResponse;
import com.kshrd.devconnect_springboot.model.entity.AppUser;
import org.apache.ibatis.annotations.*;

import java.util.UUID;

@Mapper
public interface AppUserRepository {
    @Results(id = "authMapper", value = {
            @Result(property = "userId", column = "user_id"),
            @Result(property = "firstName", column = "first_name"),
            @Result(property = "lastName", column = "last_name"),
            @Result(property = "profileImageUrl", column = "profile_image_url"),
            @Result(property = "isRecruiter", column = "is_recruiter"),
            @Result(property = "isVerified", column = "is_verified"),
            @Result(property = "createdAt", column = "created_at")
    })
    @Select("""
        SELECT * FROM app_users WHERE user_id = #{id}
    """)
    AppUserResponse getUserById(UUID id);

    @Update("""
         UPDATE app_users SET is_verified = true WHERE email = #{email}
    """)
    void updateVerificationStatus(String email);


    @Result(property = "userId", column = "user_id")
    @Result(property = "firstName", column = "first_name")
    @Result(property = "lastName", column = "last_name")
    @Result(property = "profileImageUrl", column = "profile_image_url")
    @Select("""
        SELECT * FROM app_users WHERE user_id = #{id}
    """)
    UserResponse getUserResponseById(UUID id);

    @Select("""
        update app_users
        SET
            first_name = #{appUser.firstName},
            last_name = #{appUser.lastName},
            profile_image_url = #{appUser.profileImageUrl}
        WHERE user_id = #{userId}
    """)
    void updateAppUser(@Param("appUser") AppUser appUserRequest ,  UUID userId);
}
