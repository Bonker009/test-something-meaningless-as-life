package com.kshrd.devconnect_springboot.respository;
import com.kshrd.devconnect_springboot.model.dto.response.DeveloperProfileResponse;
import com.kshrd.devconnect_springboot.utils.SqlQueryProvider;
import org.apache.ibatis.annotations.*;
import java.util.List;
import com.kshrd.devconnect_springboot.model.entity.DeveloperProfiles;
import com.kshrd.devconnect_springboot.model.dto.request.DeveloperProfilesRequest;
import java.util.UUID;

@Mapper
public interface DeveloperProfilesRepository {
 
    // GET DeveloperProfiles BY ID
    @Select("""
        SELECT *
        FROM developer_profile
        WHERE user_id = #{userId}
    """)
    @Results(id = "BaseResultMap", value = {
            @Result(property = "developerId", column = "developer_id"),
            @Result(property = "userId", column = "user_id"),
            @Result(property = "firstName", column = "first_name"),
            @Result(property = "lastName", column = "last_name"),
            @Result(property = "bio", column = "bio"),
            @Result(property = "address", column = "address"),
            @Result(property = "coverPicture", column = "cover_picture"),
            @Result(property = "cv", column = "cv"),
            @Result(property = "githubUsername", column = "github_username"),
            @Result(property = "employeeStatus", column = "employee_status"),
            @Result(property = "phoneNumber", column = "phone_number"),
            @Result(property = "position", column = "position_id",
                    one = @One(select = "com.kshrd.devconnect_springboot.respository.PositionRepository.getPositionNameById")),
            @Result(property = "jobType", column = "job_type_id",
                    one = @One(select = "com.kshrd.devconnect_springboot.respository.JobsRepository.selectJobTypeById")),
            @Result(property = "skills", column = "user_id",
                    many = @Many(select = "com.kshrd.devconnect_springboot.respository.DeveloperProfilesRepository.getSkillsByUserId")),
    })
    DeveloperProfiles selectDeveloperProfilesByUserId(UUID userId);
    
    // DELETE DeveloperProfiles
    @Select("""
        DELETE
        FROM developer_profile
        WHERE user_id = #{developerId}
        RETURNING *
        """)
        @ResultMap("BaseResultMap")
    DeveloperProfiles deleteDeveloperProfiles(UUID developerId);

    // INSERT DeveloperProfiles
    @Select("""
        INSERT INTO developer_profile
        (user_id)
        VALUES
        (
            #{userId}
        )
        RETURNING *;
        """)
    @ResultMap("BaseResultMap")
    DeveloperProfiles insertDeveloperProfiles(@Param("userId") UUID userId);

    // UPDATE  DeveloperProfiles
    @Select("""
    UPDATE developer_profile
    
    SET
         bio = trim(#{developerProfiles.bio}),
         is_female = #{developerProfiles.isFemale},
         phone_number = #{developerProfiles.phoneNumber},
         address = trim(#{developerProfiles.address}),
         cover_picture = trim(#{developerProfiles.coverPicture}),
         cv = trim(#{developerProfiles.cv}),
         employee_status = #{developerProfiles.employeeStatus}::employee_status,
         job_type_id = #{jobTypeId},
         position_id = #{positionId}
          WHERE
              user_id = #{userId}
    RETURNING *;
    """)
    @ResultMap("BaseResultMap")
    DeveloperProfiles updateDeveloperProfiles(@Param("developerProfiles") DeveloperProfilesRequest entity , UUID jobTypeId , UUID positionId, UUID userId);

    @Update("""
    UPDATE developer_profile
        SET job_type_id = NULL,
            position_id = NULL
    WHERE user_id = #{userId};
    """)
    void deleteJobTypeIdAndPositionId(@Param("userId") UUID userId);

    @Select("""
    INSERT INTO developer_skills (user_id, skill_id)
    VALUES (
            #{userId},
            #{skillId}
            )
    """)
    void insertDeveloperSkills(@Param("skillId") UUID skillId , @Param("userId") UUID userId);

    @Delete("""
    DELETE FROM developer_skills
    WHERE user_id = #{userId}
    """)
    void deleteDeveloperSkills(@Param("userId") UUID userId);

    @Select("""
        SELECT  s.skill_name
        FROM developer_skills ds
        JOIN skills s ON ds.skill_id = s.skill_id
        WHERE ds.user_id = #{userId}
        """)
    List<String> getSkillsByUserId(@Param("userId") UUID userId);

    @Select("""
        SELECT  s.skill_id
        FROM developer_skills ds
        JOIN skills s ON ds.skill_id = s.skill_id
        WHERE ds.user_id = #{userId}
        """)
    List<UUID> getSkillsIdByUserId(@Param("userId") UUID userId);

    // get github username
    @Select("""
    SELECT github_username
        FROM developer_profile
        WHERE user_id = #{userId}
    """)
    String getGithubUsername(@Param("userId") UUID userId);

    @Select("""
        SELECT cv
        FROM developer_profile
        WHERE user_id = #{userId}
    """)
    String selectCvByUserId(@Param("userId") UUID userId);

    @Insert("""
        UPDATE developer_profile
        SET github_username = #{githubSyncRequest}
        WHERE user_id = #{userId}
    """)
    void gitHubSync(@Param("githubSyncRequest") String githubSyncRequest, @Param("userId") UUID userId);

    @ResultMap("BaseResultMap")
    @SelectProvider(type = SqlQueryProvider.class, method = "getAllDeveloper")
    List<DeveloperProfiles> getAllDeveloper(Integer page, Integer size, String name);

}
