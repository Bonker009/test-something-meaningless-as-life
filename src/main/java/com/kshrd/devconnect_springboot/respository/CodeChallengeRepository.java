package com.kshrd.devconnect_springboot.respository;
import com.kshrd.devconnect_springboot.config.TestCaseListTypeHandler;
import com.kshrd.devconnect_springboot.model.dto.request.CodeChallengeRequest;
import com.kshrd.devconnect_springboot.model.entity.CodeChallenge;
import org.apache.ibatis.annotations.*;
import java.util.List;

import java.util.UUID;

@Mapper
public interface CodeChallengeRepository {

    // GET ALL CodeChallenge By Current User
    @Select("""
        SELECT
        code_challenges.*,
        code_challenges.user_id AS user_id,
        submissions.user_id AS submission_user_id
        FROM code_challenges
        JOIN submissions ON code_challenges.challenge_id = submissions.challenge_id
    """)
    @Results(id = "BaseResultMap", value = {
            @Result(property = "challengeId", column = "challenge_id"),
            @Result(property = "testCase", column = "test_case", typeHandler = TestCaseListTypeHandler.class),
            @Result(property = "createdAt", column = "created_at"),
            @Result(property = "creator", column = "user_id",
                    one = @One(select = "com.kshrd.devconnect_springboot.respository.AppUserRepository.getUserResponseById")),
            @Result(property = "starterCode", column = "start_code"),
            @Result(property = "profileUser", column = "submission_user_id",
                    one = @One(select = "com.kshrd.devconnect_springboot.respository.CodeChallengeRepository.getProfileImageUrlByUserId")),
            @Result(property = "participation", column = "challenge_id",
                    one = @One(select = "com.kshrd.devconnect_springboot.respository.CodeChallengeRepository.getParticipationCountByUserIdAndChallengeId"))

    })
    List<CodeChallenge> getAllCodeChallenge();


    // GET CodeChallenge BY ID
    @Select("""
        SELECT *
        FROM code_challenges
        WHERE code_challenges.challenge_id = #{id}
    """)
    @ResultMap("BaseResultMap")
    CodeChallenge selectCodeChallengeById(@Param("id") UUID id);

    // DELETE CodeChallenge
    @Select("""
        DELETE
        FROM code_challenges
        WHERE challenge_id = #{challengeId}
        RETURNING *
        """)
        @ResultMap("BaseResultMap")
    void deleteCodeChallenge(UUID challengeId);

    // INSERT CodeChallenge
    @Select("""
        INSERT INTO code_challenges
        (title, instruction, description, test_case, score, user_id, start_code, language)
        VALUES
        (
            #{codeChallenge.title},
            #{codeChallenge.instruction},
            #{codeChallenge.description},
            #{codeChallenge.testCase , typeHandler=com.kshrd.devconnect_springboot.config.TestCaseListTypeHandler},
            #{codeChallenge.score},
            #{creatorId},
            #{codeChallenge.starterCode},
            #{codeChallenge.language}
        )
        RETURNING *;
        """)
        @ResultMap("BaseResultMap")
    CodeChallenge insertCodeChallenge(@Param("codeChallenge") CodeChallengeRequest codeChallengeRequest , UUID creatorId);

    // UPDATE  CodeChallenge
    @Select("""
    UPDATE code_challenges
    SET
         title = #{codeChallenge.title},
         description = #{codeChallenge.description},
         test_case = #{codeChallenge.testCase , typeHandler=com.kshrd.devconnect_springboot.config.TestCaseListTypeHandler},
         instruction = #{codeChallenge.instruction},
         score = #{codeChallenge.score},
         start_code = #{codeChallenge.starterCode},
         language = #{codeChallenge.language}
    WHERE challenge_id = #{id}
    RETURNING *;
    """)
    @ResultMap("BaseResultMap")
    CodeChallenge updateCodeChallenge(UUID id , @Param("codeChallenge") CodeChallengeRequest entity);

    @Select("""
        SELECT * FROM code_challenges
        WHERE user_id = #{userId}
    """)
    @ResultMap("BaseResultMap")
    List<CodeChallenge> getAllCodeChallengesByUserId(@Param("userId") UUID userId);

    @Select("""
        SELECT profile_image_url FROM app_users
        WHERE user_id = #{userId}
    """)
    String getProfileImageUrlByUserId(@Param("userId") UUID userId);

    @Select("""
        SELECT COUNT(*) FROM submissions
        WHERE challenge_id = #{challengeId}
    """)
    Integer getParticipationCountByUserIdAndChallengeId(@Param("challengeId") UUID challengeId);
}
