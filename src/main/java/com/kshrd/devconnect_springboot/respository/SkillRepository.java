package com.kshrd.devconnect_springboot.respository;

import com.kshrd.devconnect_springboot.config.UuidTypeHandler;
import com.kshrd.devconnect_springboot.model.entity.Skill;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;
import java.util.UUID;

@Mapper
public interface SkillRepository {
    @Results(id = "BaseResultMap", value = {
            @Result(property = "skillId", column = "skill_id"),
            @Result(property = "skillName", column = "skill_name")
    })
    @Select("""
        SELECT * FROM skills
    """)
    List<Skill> getAllSkill();

    @ResultMap("BaseResultMap")
    @Select("""
        SELECT * FROM skills WHERE skill_id = #{skillId}
    """)
    Skill getSkillById(UUID skillId);

}
