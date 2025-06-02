package com.kshrd.devconnect_springboot.respository;

import com.kshrd.devconnect_springboot.model.dto.request.SubmissionRequest;
import com.kshrd.devconnect_springboot.model.dto.response.AppUserResponse;
import com.kshrd.devconnect_springboot.model.entity.AppUser;
import com.kshrd.devconnect_springboot.model.entity.Submission;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Mapper
public interface SubmissionRepository {

    // INSERT Submission
    @Insert("""
        INSERT INTO submissions
        (score, submitted_time, challenge_id, user_id, submitted_at)
        VALUES
        (
            #{submission.score},
            #{submission.submitTime},
            #{submission.challengeId},
            #{submission.developerId},
            #{submission.submittedAt}
        )
     """)
    void insertSubmission(@Param("submission") SubmissionRequest submission);

    // GET ALL Submission
    @Select("""
        SELECT * FROM submissions
        ORDER BY CAST(score AS DOUBLE PRECISION) DESC;
""")
    @Results(id = "BaseResultMap", value = {
            @Result(property = "submissionId", column = "submission_id"),
            @Result(property = "submitTime", column = "submitted_time"),
            @Result(property = "challengeId", column = "challenge_id"),
            @Result(property = "developerId", column = "user_id" , one = @One(select = "com.kshrd.devconnect_springboot.respository.AppUserRepository.getUserById")),
            @Result(property = "submittedAt", column = "submitted_at")
    })
    List<Submission> getAllSubmission();

    // GET Submission By ID
    @Select("""
        SELECT * FROM submissions
        WHERE challenge_id = #{challengeId}
       """)
    @ResultMap("BaseResultMap")
    Submission getSubmissionByChallengeId(UUID challengeId);
};