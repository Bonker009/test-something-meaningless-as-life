package com.kshrd.devconnect_springboot.respository;

import com.kshrd.devconnect_springboot.model.dto.request.ProjectRequest;
import com.kshrd.devconnect_springboot.model.dto.response.ProjectResponse;
import com.kshrd.devconnect_springboot.model.entity.Project;
import com.kshrd.devconnect_springboot.model.enums.DateStatus;
import com.kshrd.devconnect_springboot.utils.SqlQueryProvider;
import org.apache.ibatis.annotations.*;
import java.util.List;
import java.util.UUID;

@Mapper
public interface ProjectRepository {
    @Results(id = "projectMapper", value = {
            @Result(property = "projectId", column = "project_id"),
            @Result(property = "isOpen", column = "is_open"),
            @Result(property = "isBookmark", column = "is_bookmark"),
            @Result(property = "startAt", column = "start_at"),
            @Result(property = "endAt", column = "end_at"),
            @Result(property = "createdAt", column = "created_at"),
            @Result(property = "owner", column = "user_id", one = @One(select = "com.kshrd.devconnect_springboot.respository.AppUserRepository.getUserResponseById")),
            @Result(property = "skills", column = "project_id", many = @Many(select = "com.kshrd.devconnect_springboot.respository.ProjectSkillRepository.getSkillByProjectId")),
            @Result(property = "positions", column = "project_id", many = @Many(select = "com.kshrd.devconnect_springboot.respository.ProjectPositionRepository.getAllProjectPositionById")),
            @Result(property = "joinProjects", column = "project_id", many = @Many(select = "com.kshrd.devconnect_springboot.respository.JoinProjectRepository.getAllJoinProjectByProjectId"))
    })
    @SelectProvider(type = SqlQueryProvider.class, method = "getAllProject")
    List<Project> getAllProject(Integer page, Integer size, UUID userId, String name, List<UUID> skills, DateStatus dateStatus);

    @ResultMap("projectMapper")
    @Select("""
         SELECT p.*, (b.target_id IS NOT NULL) AS is_bookmark FROM projects p LEFT JOIN bookmarks b ON b.target_id = p.project_id WHERE p.project_id = #{projectId}
    """)
    Project getProjectById( UUID projectId);

    @ResultMap("projectMapper")
    @SelectProvider(type = SqlQueryProvider.class, method = "getAllProjectByUser")
    List<Project> getAllProjectByUser(UUID userId, Integer page, Integer size, String name, List<UUID> skills, DateStatus dateStatus);

    @ResultMap("projectMapper")
    @Select("""
        SELECT p.*, (b.target_id IS NOT NULL) AS is_bookmark FROM projects p LEFT JOIN bookmarks b ON b.target_id = p.project_id WHERE user_id = #{userId} AND project_id = #{projectId}
    """)
    Project getProjectByIdAndUser(UUID userId, UUID projectId);

    @ResultMap("projectMapper")
    @Select("""
        INSERT INTO projects VALUES (DEFAULT, #{req.title}, #{req.description}, #{req.isOpen}, #{req.startAt}, #{req.endAt}, DEFAULT, #{userId})
        RETURNING *;
    """)
    Project createProjectByUser(UUID userId, @Param("req") ProjectRequest request);

    @ResultMap("projectMapper")
    @Select("""
        UPDATE projects SET title = #{req.title}, description = #{req.description}, is_open = #{req.isOpen}, start_at = #{req.startAt}, end_at = #{req.endAt} WHERE project_id = #{projectId}
        RETURNING *;
    """)
    Project updateProject(UUID projectId, @Param("req") ProjectRequest request);

    @Delete("""
        DELETE FROM projects WHERE project_id = #{projectId} AND user_id = #{userId}
    """)
    void deleteProject(UUID userId, UUID projectId);

    @ResultMap("projectMapper")
    @Select("""
        UPDATE projects SET is_open = #{status} WHERE project_id = #{projectId} AND user_id = #{ownerId}
        RETURNING *;
    """)
    Project updateProjectStatus(Boolean status, UUID projectId, UUID ownerId);

    @ResultMap("projectMapper")
    @SelectProvider(type = SqlQueryProvider.class, method = "getAllProjectHistory")
    List<Project> getAllProjectHistory(Integer page, Integer size, String name, List<UUID> skills, UUID userId);

    @ResultMap("projectMapper")
    @SelectProvider(type = SqlQueryProvider.class, method = "getAllJoinedProject")
    List<Project> getAllJoinedProject(Integer page, Integer size, String name, List<UUID> skills, UUID userId);

    @SelectProvider(type = SqlQueryProvider.class, method = "getProjectTotalCount")
    Integer getProjectTotalCount(UUID userId,String name, List<UUID> skills, DateStatus dateStatus);

    @SelectProvider(type = SqlQueryProvider.class, method = "getCountAllJoinedProject")
    Integer getCountAllJoinedProject(String name, List<UUID> skills, UUID userId);

    @SelectProvider(type = SqlQueryProvider.class, method = "getProjectTotalUserCount")
    Integer getProjectTotalUserCount(UUID userId, String name, List<UUID> skills, DateStatus dateStatus);

    @SelectProvider(type = SqlQueryProvider.class, method = "getProjectHistoryTotalUserCount")
    Integer getProjectHistoryTotalUserCount(String name, List<UUID> skills, UUID userId);

}