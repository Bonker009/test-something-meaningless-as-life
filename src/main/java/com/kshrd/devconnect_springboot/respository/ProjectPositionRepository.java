package com.kshrd.devconnect_springboot.respository;

import com.kshrd.devconnect_springboot.config.UuidTypeHandler;
import com.kshrd.devconnect_springboot.model.entity.Position;
import com.kshrd.devconnect_springboot.model.entity.ProjectPosition;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.security.core.parameters.P;

import java.util.List;
import java.util.UUID;

@Mapper
public interface ProjectPositionRepository {
   @Results(id = "baseMapper", value = {
           @Result(property = "maxMembers", column = "max_members"),
           @Result(property = "positionId", column = "position_id"),
           @Result(property = "positionName", column = "position_name")
   })
    @Select("""
        SELECT * FROM project_positions pp INNER JOIN positions p ON p.position_id = pp.position_id WHERE project_id = #{projectId}
    """)
    List<ProjectPosition> getAllProjectPositionById(UUID projectId);

    @Insert("""
        INSERT INTO project_positions VALUES (#{maxMembers}, #{projectId}, #{positionId})
    """)
    void createProjectPosition(Integer maxMembers, UUID projectId, UUID positionId);

    @Delete("""
        DELETE FROM project_positions WHERE project_id = #{projectId}
    """)
    void deleteAllProjectPosition(UUID projectId);

    @ResultMap("baseMapper")
    @Select("""
        SELECT * FROM project_positions WHERE project_id = #{projectId} AND position_id = #{positionId}
    """)
    ProjectPosition getPositionByProject(UUID projectId, UUID positionId);

//    @ResultMap("baseMapper")
//    @Select("""
//        SELECT * FROM project_positions WHERE project_id = #{projectId}
//    """)
//    List<ProjectPosition> getAllPositionByProjectId(UUID projectId);
}
