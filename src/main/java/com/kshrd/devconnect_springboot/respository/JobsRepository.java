package com.kshrd.devconnect_springboot.respository;
import com.kshrd.devconnect_springboot.model.dto.response.JobResponse;
import com.kshrd.devconnect_springboot.model.entity.JobType;
import com.kshrd.devconnect_springboot.utils.SqlQueryProvider;
import org.apache.ibatis.annotations.*;
import java.util.List;
import com.kshrd.devconnect_springboot.model.entity.Jobs;
import com.kshrd.devconnect_springboot.model.dto.request.JobsRequest;

import java.util.UUID;

@Mapper
public interface JobsRepository {
 
    // GET Jobs BY ID
    @Select("""
        SELECT *
        FROM jobs
        WHERE job_id = #{jobId}
    """)
    @Results(id = "BaseResultMap", value = {
            @Result(property = "jobId", column = "job_id"),
            @Result(property = "isBookmark", column = "is_bookmark"),
            @Result(property = "position", column = "position_id", one = @One(select = "com.kshrd.devconnect_springboot.respository.PositionRepository.getPositionNameById")),
            @Result(property = "jobBoard", column = "job_board", typeHandler = com.kshrd.devconnect_springboot.config.JobBoardTypeHandler.class),
            @Result(property = "skills", column = "job_id" , many = @Many(select = "com.kshrd.devconnect_springboot.respository.JobSkillRepository.getSkillByJobId")),
            @Result(property = "jobType", column = "job_type_id" , one = @One(select = "com.kshrd.devconnect_springboot.respository.JobsRepository.selectJobTypeById")),
            @Result(property = "postedDate", column = "posted_date"),
            @Result(property = "creator", column = "user_id" , one = @One(select = "com.kshrd.devconnect_springboot.respository.AppUserRepository.getUserResponseById")),
    })
    
    Jobs selectJobsById(UUID jobId);
    
    // DELETE Jobs
    @Delete("""
        DELETE
        FROM jobs
        WHERE job_id = #{jobId} AND user_id = #{userId}
        """)
    void deleteJobs(UUID jobId , @Param("userId") UUID userId);

    // GET Jobs BY Creator ID
    @ResultMap("BaseResultMap")
    @SelectProvider(type = SqlQueryProvider.class, method = "selectJobsByCreatorId")
    List<Jobs> selectJobsByCreatorId(Integer page, Integer size, String title, List<UUID> skills, UUID creatorId);

    // INSERT Jobs
    @Select("""
        INSERT INTO jobs
        (position_id, salary, location, status, description, posted_date, user_id , job_board , job_type_id , pax)
        VALUES
        (
            #{jobs.positionId},
            #{jobs.salary},
            #{jobs.location},
            #{jobs.status},
            #{jobs.description},
            #{creatorId},
            #{jobs.jobBoard, typeHandler=com.kshrd.devconnect_springboot.config.JobBoardTypeHandler},
            #{jobs.jobTypeId},
            #{jobs.pax}
        )
        RETURNING *;
        """)
        @ResultMap("BaseResultMap")
    Jobs insertJobs(@Param("jobs") JobsRequest jobsRequest , UUID creatorId);

    // UPDATE  Jobs
    @Select("""
    UPDATE jobs
    SET
         position_id = #{jobs.positionId},
         salary = #{jobs.salary},
         location = #{jobs.location},
         status = #{jobs.status},
         description = #{jobs.description},
         job_board = #{jobs.jobBoard, typeHandler=com.kshrd.devconnect_springboot.config.JobBoardTypeHandler},
         job_type_id = #{jobs.jobTypeId},
         pax = #{jobs.pax}
         WHERE job_id = #{jobId} AND user_id = #{userId}
    RETURNING *;
    """)
    @ResultMap("BaseResultMap")
    Jobs updateJobs(UUID jobId , UUID userId, @Param("jobs") JobsRequest jobsRequest);

    // GET ALL Jobs
    @ResultMap("BaseResultMap")
    @SelectProvider(type = SqlQueryProvider.class, method = "getAllJobs")
    List<Jobs> getAllJobs(Integer page, Integer size, String title, List<UUID> skills);

    // UPDATE STATUS
    @Select("""
        UPDATE jobs
        SET status = #{status}
        WHERE job_id = #{jobId} AND user_id = #{userId}
        RETURNING *
    """)
    @ResultMap("BaseResultMap")
    Jobs updateStatus(@Param("jobId") UUID jobId, @Param("status") Boolean status , @Param("userId") UUID userId);

    // GET JOBS TYPE BY JOB TYPE ID
    @Select("""
        SELECT type_name
        FROM job_types
        WHERE job_type_id = #{jobTypeId}
    """)
    String selectJobTypeById(@Param("jobTypeId") UUID jobTypeId);

    // GET JOB BY ID FOR RESPONSE IN CARD
    @Select("""
        SELECT job_id , salary , location , status , pax , posted_date, job_type_id ,user_id , position_id
        FROM jobs
        WHERE job_id = #{jobId}
    """)
    @Results(id = "jobResponse", value = {
            @Result(property = "jobId", column = "job_id"),
            @Result(property = "isBookmark", column = "is_bookmark"),
            @Result(property = "position", column = "position_id", one = @One(select = "com.kshrd.devconnect_springboot.respository.PositionRepository.getPositionNameById")),
            @Result(property = "skills", column = "job_id" , many = @Many(select = "com.kshrd.devconnect_springboot.respository.JobSkillRepository.getSkillByJobId")),
            @Result(property = "jobType", column = "job_type_id" , one = @One(select = "com.kshrd.devconnect_springboot.respository.JobsRepository.selectJobTypeById")),
            @Result(property = "creator", column = "user_id" , one = @One(select = "com.kshrd.devconnect_springboot.respository.AppUserRepository.getUserResponseById")),
    })
    JobResponse selectJobById(@Param("jobId") UUID jobId);

    @SelectProvider(type = SqlQueryProvider.class, method = "getCountAllJob")
    Integer getCountAllJob(Integer page, Integer size, String title, List<UUID> skills);

    @SelectProvider(type = SqlQueryProvider.class, method = "getCountJobsByCreatorId")
    Integer getCountJobsByCreatorId(Integer page, Integer size, String title, List<UUID> skills, UUID creatorId);

    @Select("""
        SELECT *
        FROM job_types
    """)
    List<JobType> getAllJobTypes();
}
