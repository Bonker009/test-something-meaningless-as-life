package com.kshrd.devconnect_springboot.respository;

import com.kshrd.devconnect_springboot.model.dto.request.CommentRequest;
import com.kshrd.devconnect_springboot.model.entity.Comment;
import org.apache.ibatis.annotations.*;
import java.util.List;
import java.util.UUID;

@Mapper
public interface CommentRepository {
 
    // GET Comment BY ID
    @Select("""
        SELECT *
        FROM comments
        WHERE topic_id = #{id} AND parent_id IS NULL;
    """)
    @Results(id = "BaseResultMap", value = {
            @Result(property = "commentId", column = "comment_id"),
            @Result(property = "content", column = "text"),
            @Result(property = "postedAt", column = "created_at"),
            @Result(property = "totalUpvote", column = "total_upvotes"),
            @Result(property = "topicId", column = "topic_id"),
            @Result(property = "creator", column = "user_id",
                    one = @One(select = "com.kshrd.devconnect_springboot.respository.AppUserRepository.getUserById")),
            @Result(property = "parentId", column = "parent_id"),
            @Result(property = "replies", column = "comment_id",
                    many = @Many(select = "selectCommentsByParentId"))
    })
    List<Comment> selectCommentsByTopicId(@Param("id") UUID id);
    
    // DELETE Comment
    @Delete("""
        DELETE
        FROM comments
        WHERE comment_id = #{commentId}
        RETURNING *
        """)
    void deleteComments(UUID commentId);

    // INSERT Comment
    @Select("""
        INSERT INTO comments
        (text, topic_id, user_id)
        VALUES
        (
            #{comments.content},
            #{comments.postedAt},
            #{topicId},
            #{creatorId}
        )
        RETURNING *;
        """)
        @ResultMap("BaseResultMap")
    Comment insertComments(@Param("comments") CommentRequest entity , UUID creatorId ,UUID topicId);

    // GET Comment BY ID
    @Select("""
        SELECT *
        FROM comments
        WHERE comment_id = #{id}
    """)
    @ResultMap("BaseResultMap")
    Comment selectCommentsById(@Param("id") UUID id);
    // UPDATE  Comment
    @Select("""
        UPDATE comments
        SET
             text = #{comments.content},
             created_at = #{comments.postedAt},
             user_id = #{creatorId}
        WHERE comment_id = #{id}
        RETURNING *;
    """)
    @ResultMap("BaseResultMap")

    Comment updateComments(@Param("comments") CommentRequest entity , UUID id , UUID creatorId);

    // ADD UPVOTE
    @Update("""
        UPDATE comments
        SET total_upvotes = #{totalUpvote}
        WHERE comment_id = #{id}
    """)
    void addUpvote(@Param("id") UUID id , @Param("totalUpvote") Integer totalUpvote);

    // INSERT REPLY Comment
    @Select("""
    INSERT INTO comments
        (text, topic_id, user_id, parent_id)
        VALUES
        (
            #{reply.content},
            (SELECT topic_id FROM comments WHERE comment_id = #{parentId}),
            #{creatorId},
            #{parentId}
        )
        RETURNING *;
    """)
    @ResultMap("BaseResultMap")
    Comment insertReplyComment(@Param("reply") CommentRequest entity, UUID creatorId , UUID parentId);

    @Select("""
        SELECT *
        FROM comments
        WHERE parent_id = #{parentId}
    """)
    @ResultMap("BaseResultMap")
    List<Comment> selectCommentsByParentId(@Param("parentId") UUID parentId);

    @Select("""
            SELECT user_id
              FROM comments
              WHERE total_upvotes = (
              SELECT MAX(total_upvotes)
              FROM comments
              WHERE total_upvotes > 100) AND topic_id = #{topicId}
    """)
    UUID findUpvote(UUID topicId);
}
