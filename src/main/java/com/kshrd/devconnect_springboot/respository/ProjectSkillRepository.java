package com.kshrd.devconnect_springboot.respository;

import com.kshrd.devconnect_springboot.config.UuidTypeHandler;
import com.kshrd.devconnect_springboot.model.entity.Skill;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;
import java.util.UUID;

@Mapper
public interface ProjectSkillRepository {
    @Result(property = "skillId", column = "skill_id")
    @Result(property = "skillName", column = "skill_name")
    @Select("""
        SELECT * FROM project_skills ps INNER JOIN skills s ON s.skill_id = ps.skill_id WHERE project_id = #{projectId}
    """)
    List<Skill> getSkillByProjectId(UUID projectId);

    @Insert("""
        INSERT INTO project_skills VALUES (#{projectId}, #{skillId})
    """)
    void createProjectSkill(UUID projectId, UUID skillId);

    @Delete("""
        DELETE FROM project_skills WHERE project_id = #{projectId}
    """)
    void deleteAllProjectSkill(UUID projectId);
}