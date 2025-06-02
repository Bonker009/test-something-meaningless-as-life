package com.kshrd.devconnect_springboot.  respository;
import com.kshrd.devconnect_springboot.model.dto.request.InviteJobRequest;
import com.kshrd.devconnect_springboot.model.dto.response.JoinJobResponse;
import com.kshrd.devconnect_springboot.utils.SqlQueryProvider;
import org.apache.ibatis.annotations.*;

import java.time.LocalDate;
import java.util.List;
import com.kshrd.devconnect_springboot.model.entity.JoinJob;
import com.kshrd.devconnect_springboot.model.dto.request.JoinJobRequest;
import java.util.UUID;

@Mapper
public interface JoinJobRepository {
 
    // GET JoinJob BY ID
    @Select("""
     SELECT *
     from join_jobs
    WHERE join_job_id = #{joinJobId}
    """)
    @Results(id = "BaseResultMap", value = {
            @Result(property = "joinJobId", column = "join_job_id"),
            @Result(property = "isInvited", column = "is_invited"),
            @Result(property = "job", column = "job_id" , one = @One(select = "com.kshrd.devconnect_springboot.respository.JobsRepository.selectJobById")),
            @Result(property = "cv", column = "user_id" , one = @One(select = "com.kshrd.devconnect_springboot.respository.DeveloperProfilesRepository.selectCvByUserId")),
            @Result(property = "coverLetter", column = "cover_letter"),
            @Result(property = "user", column = "user_id" , one = @One(select = "com.kshrd.devconnect_springboot.respository.AppUserRepository.getUserResponseById")),
    })
    JoinJob selectJoinJobById(UUID joinJobId);
    
    // DELETE JoinJob
    @Delete("""
        DELETE
        FROM join_jobs
        WHERE join_job_id = #{joinJobId} AND is_approved = false AND cover_letter IS NULL
        """)
    void deleteJoinJob(UUID joinJobId);

    // INSERT JoinJob
    @Select("""
        INSERT INTO join_jobs
        (title, description, job_id, user_id, cover_letter)
        VALUES
        (
            #{joinJob.coverLetter},
            #{joinJob.description},
            #{jobId},
            #{developerId},
            #{joinJob.coverLetter}
        )
        RETURNING *;
        """)
        @ResultMap("BaseResultMap")
    JoinJob insertJoinJob(@Param("joinJob") JoinJobRequest entity , UUID developerId , @Param("jobId") UUID jobId);

    // GET ALL JoinJob
    @SelectProvider(type = SqlQueryProvider.class, method = "getAllJoinJob")
    @ResultMap("BaseResultMap")
    List<JoinJob> getAllJoinJob(Integer page, Integer size, UUID userId, UUID positionId, UUID jobType, String date);

    @SelectProvider(type = SqlQueryProvider.class, method = "getCountAllJoinJob")
    Integer getCountAllJoinJob(Integer page, Integer size, UUID userId, UUID positionId, UUID jobType, String date);

    // UPDATE IS APPROVE
    @Select("""
        UPDATE join_jobs
        SET is_approved = #{isApprove}
        WHERE join_job_id = #{joinJobId}
        RETURNING *
    """)
    @ResultMap("BaseResultMap")
    JoinJob updateIsApprove(@Param("isApprove") Boolean isApprove , @Param("joinJobId") UUID joinJobId);

    // GET ALL JOIN JOB BY STATUS
    @Select("""
        SELECT * FROM join_jobs
        where is_approved = #{isApprove} AND cover_letter IS NOT NULL
    """)
    @ResultMap("BaseResultMap")
    List<JoinJob> getAllJoinJobByStatus(@Param("isApprove") Boolean isApprove);

    // INVITE JOIN JOB
    @Select("""
        INSERT INTO join_jobs
        (title, description, job_id, user_id, is_invited)
        VALUES
        (
            #{invite.title},
            #{invite.description},
            #{jobId},
            #{developerId},
            #{isInvited}
        )
        RETURNING *;
    """)
    @ResultMap("BaseResultMap")
    JoinJob inviteJoinJob(@Param("invite") InviteJobRequest inviteJobRequest, @Param("developerId") UUID developerId, @Param("jobId") UUID jobId, Boolean isInvited);

    @Select("""
        SELECT *
        FROM join_jobs
        WHERE is_approved = false AND cover_letter IS NULL AND job_id = #{jobId};
    """)
    @ResultMap("BaseResultMap")
    List<JoinJob> getAllInvitation(UUID jobId);

    @Select("""
        UPDATE join_jobs
        SET
            is_approved = true
        WHERE join_job_id = #{joinJobId} AND cover_letter IS NULL AND user_id = #{userId}
    """)
    @ResultMap("BaseResultMap")
    JoinJob approveInviteJob(UUID joinJobId , UUID userId);

    // GWT ALL JOIN JOB BY DEVELOPER ID
    @Select("""
        SELECT *
        FROM join_jobs
        WHERE user_id = #{developerId} AND cover_letter IS NULL
    """)
    @ResultMap("BaseResultMap")
    JoinJob getJobByDevId(UUID developerId);

    // GWT ALL JOIN JOB BY DEVELOPER ID
    @Select("""
        SELECT *
        FROM join_jobs
        WHERE user_id = #{developerId} AND cover_letter IS NOT NULL
    """)
    @ResultMap("BaseResultMap")
    JoinJob getJobByUserId(UUID developerId);

    @Select("""
        SELECT *
        FROM join_jobs
        WHERE job_id = #{jobId}
    """)
    @ResultMap("BaseResultMap")
    JoinJob getJoinJobByJobId(@Param("jobId") UUID jobId);

    @Select("""
        SELECT *
        FROM join_jobs
        WHERE job_id = #{jobId} AND is_approved = #{isApprove} AND is_invited = false AND user_id =#{userId}
    """)
    @ResultMap("BaseResultMap")
    List<JoinJob> getAllApplicationByJobId(@Param("jobId") UUID jobId, @Param("isApprove") Boolean isApprove , @Param("userId") UUID userId);

    @Select("""
        SELECT *
        FROM join_jobs
        WHERE user_id = #{userId} AND is_invited = true
    """)
    List<JoinJob> getAllInviteByCurrentUser(UUID userId);
}
