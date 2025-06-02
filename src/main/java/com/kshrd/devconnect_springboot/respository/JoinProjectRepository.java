package com.kshrd.devconnect_springboot.respository;

import com.kshrd.devconnect_springboot.model.dto.request.JoinProjectRequest;
import com.kshrd.devconnect_springboot.model.dto.request.RequestToJoinProject;
import com.kshrd.devconnect_springboot.model.entity.JoinProject;
import com.kshrd.devconnect_springboot.utils.SqlQueryProvider;
import org.apache.ibatis.annotations.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Mapper
public interface JoinProjectRepository {
    @Results(id = "baseMapper", value = {
            @Result(property = "isApproved", column = "is_approved"),
            @Result(property = "isInvited", column = "is_invited"),
            @Result(property = "projectId", column = "project_id"),
            @Result(property = "projectName", column = "project_name"),
            @Result(property = "position", column = "position_id", one = @One(select = "com.kshrd.devconnect_springboot.respository.PositionRepository.getPositionById")),
            @Result(property = "developer", column = "user_id", one = @One(select = "com.kshrd.devconnect_springboot.respository.AppUserRepository.getUserResponseById")),
    })
    @Select("""
        SELECT * FROM join_projects WHERE project_id = #{projectId}
    """)
    List<JoinProject> getAllJoinProjectByProjectId(UUID projectId);

    @ResultMap("baseMapper")
    @Select("""
        SELECT * FROM join_projects WHERE project_id = #{projectId} AND is_approved = true
    """)
    List<JoinProject> getAllJoinProjectByProjectIdAndApproved(UUID projectId);

    @ResultMap("baseMapper")
    @Select("""
        SELECT * FROM join_projects WHERE project_id = #{projectId} AND is_approved = false
    """)
    List<JoinProject> getAllJoinProjectByProjectIdAndDeny(UUID projectId);

    @ResultMap("baseMapper")
    @Select("""
        INSERT INTO join_projects VALUES (#{jp.title}, #{jp.description}, DEFAULT, #{jp.projectId}, #{jp.developerId}, #{jp.positionId}, #{isInvited}, DEFAULT)
        RETURNING *;
    """)
    JoinProject createJoinProject(@Param("jp") JoinProjectRequest jp, Boolean isInvited);

    @ResultMap("baseMapper")
    @Select("""
        INSERT INTO join_projects VALUES (#{jp.title}, #{jp.description}, DEFAULT, #{projectId}, #{appUserId}, #{jp.positionId}, DEFAULT, DEFAULT)
        RETURNING *;
    """)
    JoinProject createReqJoinProject(@Param("jp") RequestToJoinProject requestToJoinProject, UUID appUserId, UUID projectId);

    @Delete("""
        DELETE FROM join_projects WHERE project_id = #{projectId}
    """)
    void deleteAllJoinProject(UUID projectId);

    @Select("""
        SELECT COUNT(DISTINCT jp.*) FROM join_projects jp
           LEFT JOIN project_positions pp ON jp.project_id = pp.project_id
           LEFT JOIN positions p ON p.position_id = pp.position_id WHERE jp.project_id = #{projectId} AND jp.position_id = #{positionId} AND is_approved = true
    """)
    Integer getApprovedCount(UUID projectId, UUID positionId);

    @ResultMap("baseMapper")
    @Select("""
        SELECT * FROM join_projects WHERE user_id = #{developerId}
    """)
    List<JoinProject> getJoinProjectByDeveloperId(UUID developerId);

    @ResultMap("baseMapper")
    @Select("""
        SELECT * FROM join_projects WHERE project_id = #{projectId} AND user_id = #{developerId}
    """)
    JoinProject getJoinProjectByDeveloperIdAndProjectId(UUID projectId, UUID developerId);

    @Update("""
        UPDATE join_projects SET is_approved = true WHERE project_id = #{projectId} AND user_id = #{developerId}
    """)
    void updateApprovalStatus(UUID projectId, UUID developerId);

    @Update("""
        UPDATE join_projects SET is_approved = true WHERE project_id = #{projectId} AND user_id = #{developerId} AND is_invited = true
    """)
    void acceptProjectInvite(UUID projectId, UUID developerId);

    @ResultMap("baseMapper")
    @SelectProvider(type = SqlQueryProvider.class, method = "getJoinProjectByOwner")
    List<JoinProject> getJoinProjectByOwner(Integer page, Integer size, UUID userId, UUID positionId, Boolean isInvited, String date);

    @Delete("""
        DELETE FROM join_projects WHERE project_id = #{projectId} AND user_id = #{appUserId}
    """)
    void deleteJoinProject(UUID projectId, UUID appUserId);
}
