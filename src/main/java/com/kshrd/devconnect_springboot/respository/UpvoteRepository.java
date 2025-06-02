package com.kshrd.devconnect_springboot.respository;

import com.kshrd.devconnect_springboot.model.entity.Upvote;
import org.apache.ibatis.annotations.*;

import java.util.UUID;

@Mapper
public interface UpvoteRepository {

    // DELETE Upvote
    @Select("""
        DELETE
        FROM upvotes
        WHERE comment_id = #{upvote.commentId} AND user_id = #{upvote.userId}
        RETURNING comment_id
        """)
    UUID deleteUpvote(@Param("upvote") Upvote entity);

    // INSERT Upvote
    @Select("""
        INSERT INTO upvotes
        (comment_id, user_id)
        VALUES
        (
            #{upvote.commentId},
            #{upvote.userId}
        )
        """)

    void insertUpvote(@Param("upvote") Upvote entity);

    @Select("""
     SELECT COUNT(*) AS total_upvotes FROM upvotes
     WHERE comment_id = #{commentId}
    """)
    Integer countUpvote(@Param("commentId") UUID commentId);
}
