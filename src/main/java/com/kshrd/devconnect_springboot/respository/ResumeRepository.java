package com.kshrd.devconnect_springboot.respository;

import com.kshrd.devconnect_springboot.config.ResumeInformationTypeHandler;
import com.kshrd.devconnect_springboot.model.dto.request.ResumeRequest;
import com.kshrd.devconnect_springboot.model.entity.Resume;
import com.kshrd.devconnect_springboot.model.JSONBTemplate.resumeInfomation.ResumeInformation;
import org.apache.ibatis.annotations.*;

import java.util.UUID;

@Mapper
public interface ResumeRepository {
 
    // GET Resume BY ID
    @Select("""
        SELECT *
        FROM resumes
        WHERE user_id = #{userId}
    """)
    @Results(id = "BaseResultMap", value = {
            @Result(property = "resumeId", column = "resume_id"),
            @Result(property = "fullName", column = "full_name"),
            @Result(property = "phoneNumber", column = "phone_number"),
            @Result(property = "isFemale", column = "is_female"),
            @Result(property = "placeOfBirth", column = "place_of_birth"),
            @Result(property = "description", column = "description"),
            @Result(property = "information", column = "information" , typeHandler = ResumeInformationTypeHandler.class),
            @Result(property = "developerId", column = "user_id")
    })
    Resume selectCurrentResumes(@Param("userId") UUID userId);
    
    // DELETE Resume
    @Delete("""
        DELETE
        FROM resumes
        WHERE user_id = #{userId}
        """)
        @ResultMap("BaseResultMap")
    void deleteResumes(UUID userId);

    // INSERT Resume
    @Select("""
        INSERT INTO resumes
        (full_name, picture , is_female ,phone_number, address, email, dob, place_of_birth , position, description, information, user_id)
        VALUES
        (
            #{resume.fullName},
            #{resume.picture},
            #{resume.isFemale},
            #{resume.phoneNumber},
            #{resume.address},
            #{resume.email},
            #{resume.dob},
            #{resume.placeOfBirth},
            #{position},
            #{resume.description},
            #{information, typeHandler=com.kshrd.devconnect_springboot.config.ResumeInformationTypeHandler},
            #{userId}
        )
        RETURNING *;
        """)
        @ResultMap("BaseResultMap")
   
    Resume insertResumes(@Param("resume") ResumeRequest entity ,String position ,@Param("information") ResumeInformation information, UUID userId);
    // UPDATE  Resume
    @Select("""
    UPDATE resumes
    SET
         full_name = #{resume.fullName},
         picture = #{resume.picture},
         is_female = #{resume.isFemale},
         phone_number = #{resume.phoneNumber},
         address = #{resume.address},
         email = #{resume.email},
         dob = #{resume.dob},
         place_of_birth = #{resume.placeOfBirth},
         position = #{position},
         description = #{resume.description},
         information = #{information, typeHandler=com.kshrd.devconnect_springboot.config.ResumeInformationTypeHandler}
    WHERE user_id = #{developerId}
    RETURNING *;
    """)
    @ResultMap("BaseResultMap")
    Resume updateResumes(@Param("resume") ResumeRequest entity, String position ,ResumeInformation information, UUID developerId);

    @Select("""
        SELECT phone_number , information , email
        FROM resumes
    """)
    @ResultMap("BaseResultMap")
    Resume selectAllForValidation();
}
