package com.kshrd.devconnect_springboot.respository;

import com.kshrd.devconnect_springboot.model.entity.*;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.UUID;

@Mapper
public interface BookmarkRepository {
    @Results(id = "baseMapper", value = {
            @Result(property = "targetId", column = "target_id"),
            @Result(property = "bookmarkBy", column = "bookmark_by")
    })
    @Select("""
        INSERT INTO bookmarks VALUES (DEFAULT, #{targetId}, #{bookmarkBy})
        RETURNING *;
    """)
    Bookmark createBookmark(UUID targetId, UUID bookmarkBy);

    @ResultMap("baseMapper")
    @Select("""
        SELECT * FROM bookmarks WHERE target_id = #{targetId} AND bookmark_by = #{bookmarkBy}
    """)
    Bookmark getBookmarkById(UUID targetId, UUID bookmarkBy);

    @Result(property = "projectId", column = "project_id")
    @Result(property = "isOpen", column = "is_open")
    @Result(property = "isBookmark", column = "is_bookmark")
    @Result(property = "createdAt", column = "created_at")
    @Result(property = "startAt", column = "start_at")
    @Result(property = "endAt", column = "end_at")
    @Result(property = "owner", column = "user_id", one = @One(select = "com.kshrd.devconnect_springboot.respository.AppUserRepository.getUserResponseById"))
    @Result(property = "skills", column = "project_id", many = @Many(select = "com.kshrd.devconnect_springboot.respository.ProjectSkillRepository.getSkillByProjectId"))
    @Result(property = "positions", column = "project_id", many = @Many(select = "com.kshrd.devconnect_springboot.respository.ProjectPositionRepository.getAllProjectPositionById"))
    @Select("""
        SELECT p.* FROM projects p INNER JOIN bookmarks b ON p.project_id = b.target_id WHERE bookmark_by = #{bookmarkBy}
        OFFSET #{page} LIMIT #{size}
    """)
    List<Project> getAllBookmarkProject(UUID bookmarkBy, Integer page, Integer size);

    @Select("""
        SELECT COUNT(p.*) FROM projects p INNER JOIN bookmarks b ON p.project_id = b.target_id WHERE bookmark_by = #{bookmarkBy}
    """)
    Integer getCountAllBookmarkProject(UUID bookmarkBy);

    @Result(property = "jobId", column = "job_id")
    @Result(property = "jobBoard", column = "job_board", typeHandler = com.kshrd.devconnect_springboot.config.JobBoardTypeHandler.class)
    @Result(property = "jobType", column = "job_type" , one = @One(select = "com.kshrd.devconnect_springboot.respository.JobsRepository.selectJobTypeById"))
    @Result(property = "postedDate", column = "posted_date")
    @Result(property = "creator", column = "creator_id" , one = @One(select = "com.kshrd.devconnect_springboot.respository.AppUserRepository.getUserById"))
    @Select("""
        SELECT j.* FROM jobs j INNER JOIN bookmarks b ON j.job_id = b.target_id WHERE bookmark_by = #{bookmarkBy}
        OFFSET #{page} LIMIT #{size}
    """)
    List<Jobs> getAllBookmarkJob(UUID bookmarkBy, Integer page, Integer size);

    @Select("""
        SELECT COUNT(j.*) FROM jobs j INNER JOIN bookmarks b ON j.job_id = b.target_id WHERE bookmark_by = #{bookmarkBy}
    """)
    Integer getCountAllBookmarkJob(UUID bookmarkBy);

    @Result(property = "hackathonId", column = "hackathon_id")
    @Result(property = "startDate", column = "started_at")
    @Result(property = "endDate", column = "finished_at")
    @Result(property = "createdDate", column = "created_at")
    @Result(property = "isAvailable", column = "is_available")
    @Result(property = "creatorId", column = "user_id", one = @One(select = "com.kshrd.devconnect_springboot.respository.AppUserRepository.getUserResponseById"))
    @Result(property = "fullScore", column = "full_score")
    @Result(property = "developerId", column = "developer_id")
    @Result(property = "joinHackathons", column = "hackathon_id", many = @Many(select = "com.kshrd.devconnect_springboot.respository.JoinHackathonRepository.getAllJoinHackathonByHackathonId"))
    @Select("""
        SELECT h.* FROM hackathons h INNER JOIN bookmarks b ON h.hackathon_id = b.target_id WHERE bookmark_by = #{bookmarkBy}
        OFFSET #{page} LIMIT #{size}
    """)
    List<Hackathon> getAllBookmarkHackathon(UUID bookmarkBy, Integer page, Integer size);

    @Select("""
        SELECT COUNT(h.*) FROM hackathons h INNER JOIN bookmarks b ON h.hackathon_id = b.target_id WHERE bookmark_by = #{bookmarkBy}
    """)
    Integer getCountAllBookmarkHackathon(UUID bookmarkBy);

    @Result(property = "recruiterId", column = "recruiter_id")
    @Result(property = "companyName", column = "company_name")
    @Result(property = "phoneNumber", column = "phone_number")
    @Result(property = "companyLocation", column = "company_location")
    @Result(property = "establishDate", column = "establish_date")
    @Result(property = "coverPicture", column = "cover_picture")
    @Result(property = "userInformation", column = "user_id", one = @One(select = "com.kshrd.devconnect_springboot.respository.AppUserRepository.getUserById"))
    @Select("""
        SELECT r.* FROM recruiter_profile r INNER JOIN bookmarks b ON b.target_id = r.recruiter_id WHERE bookmark_by = #{bookmarkBy}
        OFFSET #{page} LIMIT #{size}
    """)
    List<Recruiter> getAllBookmarkRecruiter(UUID bookmarkBy, Integer page, Integer size);

    @Select("""
        SELECT COUNT(r.*) FROM recruiter_profile r INNER JOIN bookmarks b ON b.target_id = r.recruiter_id WHERE bookmark_by = #{bookmarkBy}
    """)
    Integer getCountAllBookmarkRecruiter(UUID bookmarkBy);

    @Result(property = "developerId", column = "developer_id")
    @Result(property = "userId", column = "user_id")
    @Result(property = "bio", column = "bio")
    @Result(property = "address", column = "address")
    @Result(property = "coverPicture", column = "cover_picture")
    @Result(property = "cv", column = "cv")
    @Result(property = "githubUsername", column = "github_username")
    @Result(property = "employeeStatus", column = "employee_status")
    @Result(property = "jobTypeId", column = "job_type_id")
    @Result(property = "phoneNumber", column = "phone_number")
    @Result(property = "skills", column = "user_id",
            many = @Many(select = "com.kshrd.devconnect_springboot.respository.DeveloperProfilesRepository.getSkillsByUserId"))
    @Select("""
        SELECT d.* FROM developer_profile d INNER JOIN bookmarks b ON  b.target_id = d.developer_id WHERE bookmark_by = #{bookmarkBy}
        OFFSET #{page} LIMIT #{size}
    """)
    List<Developer> getAllBookmarkDeveloper(UUID bookmarkBy, Integer page, Integer size);

    @Select("""
        SELECT COUNT(d.*) FROM developer_profile d INNER JOIN bookmarks b ON  b.target_id = d.developer_id WHERE bookmark_by = #{bookmarkBy}
    """)
    Integer getCountAllBookmarkDeveloper(UUID bookmarkBy);

    @Delete("""
        DELETE FROM bookmarks WHERE target_id = #{targetId} AND bookmark_by = #{appUserId}
    """)
    void deleteBookmark(UUID targetId, UUID appUserId);
}
