package com.kshrd.devconnect_springboot.respository;

import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.UUID;

@Mapper
public interface JobSkillRepository {
    // INSERT SKILL BY JOB TYPE ID
    @Insert("""
        INSERT INTO job_skills
        (job_id, skill_id)
        VALUES
        (
            #{jobId},
            #{skillId}
        )
    """)
    void insertSkillByJobId(@Param("jobId") UUID jobId, @Param("skillId") UUID skillId);

    // DELETE SKILL BY JOB TYPE ID
    @Delete("""
    DELETE FROM job_skills
    WHERE job_id = #{jobId}
    """)
    void deleteSkillByJobId(UUID jobId);

    // GET SKILL BY JOB ID
    @Select("""
        SELECT s.skill_name
        FROM skills s
        JOIN job_skills ts ON s.skill_id = ts.skill_id
        WHERE ts.job_id = #{JobId}
    """)
    List<String> getSkillByJobId(UUID JobId);
}
