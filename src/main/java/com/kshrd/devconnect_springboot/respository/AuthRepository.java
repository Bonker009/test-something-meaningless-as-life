package com.kshrd.devconnect_springboot.respository;
import com.kshrd.devconnect_springboot.config.UuidTypeHandler;
import com.kshrd.devconnect_springboot.model.dto.request.AppUserRequest;
import com.kshrd.devconnect_springboot.model.entity.AppUser;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.UUID;

@Mapper
public interface AuthRepository {
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
        INSERT INTO app_users (first_name, last_name, email, password, is_recruiter)
                                    VALUES
        (#{req.firstName}, #{req.lastName}, #{req.email}, #{req.password}, #{req.isRecruiter})
        RETURNING *;
    """)
    AppUser register(@Param("req") AppUserRequest request);

    @ResultMap("authMapper")
    @Select("""
        UPDATE app_users SET password = #{password} WHERE email = #{email}
        RETURNING *;
    """)
    AppUser updatePassword(String email, String password);

    @ResultMap("authMapper")
    @Select("""
        SELECT
            user_id, first_name, last_name, LOWER(email) AS email, password, is_recruiter, is_verified, profile_image_url, created_at
        FROM app_users
        WHERE email = #{email}
    """)
    AppUser getUserByEmail(String email);

    @ResultMap("authMapper")
    @Select("""
        SELECT * FROM app_users WHERE user_id = #{userId}
    """)
    AppUser getUserById(UUID userId);

    @Update("""
        UPDATE app_users SET password = #{password} WHERE user_id = #{appUserId}
    """)
    void changePassword(String password, UUID appUserId);
}
