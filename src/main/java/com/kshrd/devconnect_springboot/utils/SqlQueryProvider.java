package com.kshrd.devconnect_springboot.utils;

import com.kshrd.devconnect_springboot.model.enums.DateStatus;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class SqlQueryProvider {
    public String getAllProject(Map<String, Object> params) {
        Object userId = params.get("userId");
        StringBuilder sql = new StringBuilder("SELECT DISTINCT p.*\n" + ", (b.target_id IS NOT NULL) AS is_bookmark " +
                "FROM projects p\n" + " LEFT JOIN bookmarks b ON p.project_id = b.target_id " +
                "WHERE NOT user_id = '").append(userId).append("' ");

        Object name = params.get("name");
        if (name != null && !((String) name).isEmpty()) {
            sql.append(" AND p.title ILIKE CONCAT('%', TRIM(#{name}), '%') ");
        }

        List<UUID> skills = (List<UUID>) params.get("skills");

        if (skills != null && !skills.isEmpty() ) {
            Integer count = skills.size();
            sql.append(" AND p.project_id IN (" +
                    " SELECT project_id " +
                    "FROM project_skills " +
                    "WHERE skill_id IN (" );
            for (UUID skillId : skills) {
                sql.append("'").append(skillId).append("'");
                if (!skills.getLast().equals(skillId)) {
                    sql.append(" , ");
                }
            }
            sql.append(") GROUP BY project_id" +
                    " HAVING COUNT(DISTINCT skill_id) = ").append(count).append(" )");
        }

        DateStatus dateStatus = (DateStatus) params.get("dateStatus");
        if (dateStatus != null) {
            if (dateStatus.equals(DateStatus.OPEN)) {
                sql.append(" AND start_at > CURRENT_TIMESTAMP");
            } else if (dateStatus.equals(DateStatus.PROGRESS)) {
                sql.append(" AND CURRENT_TIMESTAMP BETWEEN start_at AND end_at ");
            } else {
                sql.append(" AND end_at < CURRENT_TIMESTAMP");
            }
        }

        Object page = params.get("page");
        Object size = params.get("size");
        sql.append(" OFFSET #{page} LIMIT #{size}");
        return sql.toString();
    }

    public String getAllProjectByUser(Map<String, Object> params) {
        Object userId = params.get("userId");
        StringBuilder sql = new StringBuilder("SELECT DISTINCT p.*\n" +", (b.target_id IS NOT NULL) AS is_bookmark " +
                "FROM projects p\n" +" LEFT JOIN bookmarks b ON p.project_id = b.target_id " +
                "WHERE p.user_id = '").append(userId).append("' ");

        Object name = params.get("name");
        if (name != null && !((String) name).isEmpty()) {
            sql.append(" AND p.title ILIKE CONCAT('%', TRIM(#{name}), '%') ");
        }

        List<UUID> skills = (List<UUID>) params.get("skills");

        if (skills != null && !skills.isEmpty() ) {
            Integer count = skills.size();
            sql.append(" AND p.project_id IN (" +
                    " SELECT project_id " +
                    "FROM project_skills " +
                    "WHERE skill_id IN (" );
            for (UUID skillId : skills) {
                sql.append("'").append(skillId).append("'");
                if (!skills.getLast().equals(skillId)) {
                    sql.append(" , ");
                }
            }
            sql.append(") GROUP BY project_id" +
                    " HAVING COUNT(DISTINCT skill_id) = ").append(count).append(" )");
        }

        DateStatus dateStatus = (DateStatus) params.get("dateStatus");
        if (dateStatus != null) {
            if (dateStatus.equals(DateStatus.OPEN)) {
                sql.append(" AND start_at > CURRENT_TIMESTAMP");
            } else if (dateStatus.equals(DateStatus.PROGRESS)) {
                sql.append(" AND CURRENT_TIMESTAMP BETWEEN start_at AND end_at ");
            } else {
                sql.append(" AND end_at < CURRENT_TIMESTAMP");
            }
        }

        Object page = params.get("page");
        Object size = params.get("size");
        sql.append(" OFFSET #{page} LIMIT #{size}");
        return sql.toString();
    }

    public String getAllProjectHistory(Map<String, Object> params) {
        Object userId = params.get("userId");
        StringBuilder sql = new StringBuilder("SELECT DISTINCT p.*\n" +", (b.target_id IS NOT NULL) AS is_bookmark " +
                "FROM projects p\n" +" LEFT JOIN bookmarks b ON p.project_id = b.target_id " +
                "WHERE p.user_id = '").append(userId).append("' AND end_at < CURRENT_TIMESTAMP ");

        Object name = params.get("name");
        if (name != null && !((String) name).isEmpty()) {
            sql.append(" AND p.title ILIKE CONCAT('%', TRIM(#{name}), '%') ");
        }

        List<UUID> skills = (List<UUID>) params.get("skills");

        if (skills != null && !skills.isEmpty() ) {
            Integer count = skills.size();
            sql.append(" AND p.project_id IN (" +
                    " SELECT project_id " +
                    "FROM project_skills " +
                    "WHERE skill_id IN (" );
            for (UUID skillId : skills) {
                sql.append("'").append(skillId).append("'");
                if (!skills.getLast().equals(skillId)) {
                    sql.append(" , ");
                }
            }
            sql.append(") GROUP BY project_id" +
                    " HAVING COUNT(DISTINCT skill_id) = ").append(count).append(" )");
        }

        Object page = params.get("page");
        Object size = params.get("size");
        sql.append(" OFFSET #{page} LIMIT #{size}");
        return sql.toString();
    }

    public String getAllJoinedProject(Map<String, Object> params) {
        Object userId = params.get("userId");
        StringBuilder sql = new StringBuilder("SELECT DISTINCT p.* FROM projects p\n" +
                "LEFT JOIN join_projects jp on p.project_id = jp.project_id\n" +
                "WHERE jp.user_id = '").append(userId).append("' AND end_at < CURRENT_TIMESTAMP");

        Object name = params.get("name");
        if (name != null && !((String) name).isEmpty()) {
            sql.append(" AND p.title ILIKE CONCAT('%', TRIM(#{name}), '%') ");
        }

        List<UUID> skills = (List<UUID>) params.get("skills");

        if (skills != null && !skills.isEmpty() ) {
            Integer count = skills.size();
            sql.append(" AND p.project_id IN (" +
                    " SELECT project_id " +
                    "FROM project_skills " +
                    "WHERE skill_id IN (" );
            for (UUID skillId : skills) {
                sql.append("'").append(skillId).append("'");
                if (!skills.getLast().equals(skillId)) {
                    sql.append(" , ");
                }
            }
            sql.append(") GROUP BY project_id" +
                    " HAVING COUNT(DISTINCT skill_id) = ").append(count).append(" )");
        }

        Object page = params.get("page");
        Object size = params.get("size");
        sql.append(" OFFSET #{page} LIMIT #{size}");
        return sql.toString();
    }

    public String getAllHackathon(Map<String, Object> params) {
        StringBuilder sql = new StringBuilder("select h.*, (b.target_id IS NOT NULL) as is_bookmark\n" +
                "from hackathons h left join bookmarks b ON b.target_id = h.hackathon_id WHERE 1 = 1");

        Object title = params.get("title");
        if (title != null && !((String) title).isEmpty()) {
            sql.append(" AND title ILIKE CONCAT('%', #{title}, '%')");
        }

        Object page = params.get("page");
        Object size = params.get("size");
        sql.append(" OFFSET #{page} LIMIT #{size}");
        return sql.toString();
    }



//    history
    public String getJoinedHistory(Map<String, Object> params) {
        Object userId = params.get("appUserId");
        StringBuilder sql = new StringBuilder("SELECT DISTINCT h.*, (b.target_id IS NOT NULL) as is_bookmark FROM hackathons h\n" +
                " LEFT JOIN bookmarks b ON b.target_id = h.hackathon_id" +
                " LEFT JOIN join_hackathons jh ON jh.hackathon_id = h.hackathon_id\n" +
                " WHERE jh.user_id = '").append (userId).append("' AND finished_at < current_timestamp ");

        Object title = params.get("title");
        if (title != null && !((String) title).isEmpty()) {
            sql.append(" AND title ILIKE CONCAT('%', TRIM(#{title}), '%')");
        }

        Object page = params.get("page");
        Object size = params.get("size");
        sql.append(" OFFSET #{page} LIMIT #{size}");
        return sql.toString();
    }

//    owner (recruiter)
    public String getAllHackathonsByCurrentUser(Map<String, Object> params) {
        Object creatorId = params.get("creatorId");
        StringBuilder sql = new StringBuilder("select h.*, (b.target_id IS NOT NULL) as is_bookmark\n" +
                " from hackathons h left join bookmarks b ON b.target_id = h.hackathon_id WHERE h.user_id = '").append(creatorId).append("' ");

        Object title = params.get("title");
        if (title != null && !((String) title).isEmpty()) {
            sql.append(" AND title ILIKE CONCAT('%', TRIM(#{title}), '%')");
        }

        Object page = params.get("page");
        Object size = params.get("size");
        sql.append(" OFFSET #{page} LIMIT #{size}");
        return sql.toString();
    }

    public String getAllJobs(Map<String, Object> params) {
        StringBuilder sql = new StringBuilder("SELECT DISTINCT j.*, (b.target_id IS NOT NULL ) as is_bookmark FROM jobs j LEFT JOIN bookmarks b ON b.target_id = j.job_id \n" +
                " LEFT JOIN positions p ON j.position_id = p.position_id WHERE 1 = 1 ");

        List<UUID> skills = (List<UUID>) params.get("skills");

        if (skills != null && !skills.isEmpty() ) {
            Integer count = skills.size();
            sql.append(" AND j.job_id IN (" +
                    " SELECT job_id FROM job_skills WHERE job_id IN (" );
            for (UUID skillId : skills) {
                sql.append("'").append(skillId).append("'");
                if (!skills.getLast().equals(skillId)) {
                    sql.append(" , ");
                }
            }
            sql.append(")  GROUP BY job_id " +
                    " HAVING COUNT(DISTINCT job_id) = ").append(count).append(" )");
        }

        Object title = params.get("title");
        if (title != null && !((String) title).isEmpty()) {
            sql.append(" AND position_name ILIKE CONCAT('%', TRIM(#{title}), '%')");
        }

        Object page = params.get("page");
        Object size = params.get("size");
        sql.append(" OFFSET #{page} LIMIT #{size}");
        return sql.toString();
    }

    public String getAllJoinJob(Map<String, Object> params) {
        Object userId = params.get("userId");
        StringBuilder sql = new StringBuilder("SELECT DISTINCT jj.* FROM jobs j \n" +
                "    LEFT JOIN join_jobs jj ON j.job_id = jj.job_id \n" +
                "                     WHERE j.user_id = '").append(userId).append("' ");

        Object jobType = params.get("jobType");
        if (jobType != null) {
            sql.append(" AND j.job_type_id = '").append(jobType).append("'");
        }

        Object positionId = params.get("positionId");
        if (positionId != null) {
            sql.append(" AND j.position_id = '").append(positionId).append("' ");
        }

        Object date = params.get("date");
        if (date != null) {
            sql.append(" AND jj.created_at::date = #{date}::date ");
        }

        Object page = params.get("page");
        Object size = params.get("size");
        sql.append(" OFFSET #{page} LIMIT #{size}");
        return sql.toString();
    }

    public String getCountAllJoinJob(Map<String, Object> params) {
        Object userId = params.get("userId");
        StringBuilder sql = new StringBuilder("SELECT COUNT(DISTINCT jj.*) FROM jobs j \n" +
                "    LEFT JOIN join_jobs jj ON j.job_id = jj.job_id \n" +
                "                     WHERE j.user_id = '").append(userId).append("' ");

        Object jobType = params.get("jobType");
        if (jobType != null) {
            sql.append(" AND j.job_type_id = '").append(jobType).append("'");
        }

        Object positionId = params.get("positionId");
        if (positionId != null) {
            sql.append(" AND j.position_id = '").append(positionId).append("' ");
        }

        Object date = params.get("date");
        if (date != null) {
            sql.append(" AND jj.created_at::date = #{date}::date ");
        }

        return sql.toString();
    }

    public String selectJobsByCreatorId(Map<String, Object> params) {
        Object creatorId = params.get("creatorId");
        StringBuilder sql = new StringBuilder("SELECT DISTINCT j.*, (b.target_id IS NOT NULL ) as is_bookmark FROM jobs j LEFT JOIN bookmarks b ON b.target_id = j.job_id \n" +
                " LEFT JOIN positions p ON j.position_id = p.position_id WHERE user_id = #{creatorId} ");

        List<UUID> skills = (List<UUID>) params.get("skills");

        if (skills != null && !skills.isEmpty() ) {
            Integer count = skills.size();
            sql.append(" AND j.job_id IN (" +
                    " SELECT job_id FROM job_skills WHERE job_id IN (" );
            for (UUID skillId : skills) {
                sql.append("'").append(skillId).append("'");
                if (!skills.getLast().equals(skillId)) {
                    sql.append(" , ");
                }
            }
            sql.append(")  GROUP BY job_id " +
                    " HAVING COUNT(DISTINCT job_id) = ").append(count).append(" )");
        }

        Object title = params.get("title");
        if (title != null && !((String) title).isEmpty()) {
            sql.append(" AND position_name ILIKE CONCAT('%', TRIM(#{title}), '%')");
        }

        Object page = params.get("page");
        Object size = params.get("size");
        sql.append(" OFFSET #{page} LIMIT #{size}");
        return sql.toString();
    }

    public String getProjectTotalCount(Map<String, Object> params) {
        Object userId = params.get("userId");
        StringBuilder sql = new StringBuilder("SELECT COUNT(DISTINCT p.*)\n" +
                "FROM projects p\n" +
                "WHERE NOT user_id = '").append(userId).append("' ");

        Object name = params.get("name");
        if (name != null && !((String) name).isEmpty()) {
            sql.append(" AND p.title ILIKE CONCAT('%', TRIM(#{name}), '%') ");
        }

        List<UUID> skills = (List<UUID>) params.get("skills");

        if (skills != null && !skills.isEmpty() ) {
            Integer count = skills.size();
            sql.append(" AND p.project_id IN (" +
                    " SELECT project_id " +
                    "FROM project_skills " +
                    "WHERE skill_id IN (" );
            for (UUID skillId : skills) {
                sql.append("'").append(skillId).append("'");
                if (!skills.getLast().equals(skillId)) {
                    sql.append(" , ");
                }
            }
            sql.append(") GROUP BY project_id" +
                    " HAVING COUNT(DISTINCT skill_id) = ").append(count).append(" )");
        }

        DateStatus dateStatus = (DateStatus) params.get("dateStatus");
        if (dateStatus != null) {
            if (dateStatus.equals(DateStatus.OPEN)) {
                sql.append(" AND start_at > CURRENT_TIMESTAMP");
            } else if (dateStatus.equals(DateStatus.PROGRESS)) {
                sql.append(" AND CURRENT_TIMESTAMP BETWEEN start_at AND end_at ");
            } else {
                sql.append(" AND end_at < CURRENT_TIMESTAMP");
            }
        }
        return sql.toString();
    }

    public String getProjectTotalUserCount(Map<String, Object> params) {
        Object userId = params.get("userId");
        StringBuilder sql = new StringBuilder("SELECT COUNT(DISTINCT p.*)\n" +
                "FROM projects p\n" +
                "WHERE p.user_id = '").append(userId).append("' ");

        Object name = params.get("name");
        if (name != null && !((String) name).isEmpty()) {
            sql.append(" AND p.title ILIKE CONCAT('%', TRIM(#{name}), '%') ");
        }

        List<UUID> skills = (List<UUID>) params.get("skills");

        if (skills != null && !skills.isEmpty() ) {
            Integer count = skills.size();
            sql.append(" AND p.project_id IN (" +
                    " SELECT project_id " +
                    "FROM project_skills " +
                    "WHERE skill_id IN (" );
            for (UUID skillId : skills) {
                sql.append("'").append(skillId).append("'");
                if (!skills.getLast().equals(skillId)) {
                    sql.append(" , ");
                }
            }
            sql.append(") GROUP BY project_id" +
                    " HAVING COUNT(DISTINCT skill_id) = ").append(count).append(" )");
        }

        DateStatus dateStatus = (DateStatus) params.get("dateStatus");
        if (dateStatus != null) {
            if (dateStatus.equals(DateStatus.OPEN)) {
                sql.append(" AND start_at > CURRENT_TIMESTAMP");
            } else if (dateStatus.equals(DateStatus.PROGRESS)) {
                sql.append(" AND CURRENT_TIMESTAMP BETWEEN start_at AND end_at ");
            } else {
                sql.append(" AND end_at < CURRENT_TIMESTAMP");
            }
        }

        return sql.toString();
    }

    public String getProjectHistoryTotalUserCount(Map<String, Object> params) {
        Object userId = params.get("userId");
        StringBuilder sql = new StringBuilder("SELECT COUNT(DISTINCT p.*)\n" +
                "FROM projects p\n" +
                "WHERE p.user_id = '").append(userId).append("' AND end_at < CURRENT_TIMESTAMP ");

        Object name = params.get("name");
        if (name != null && !((String) name).isEmpty()) {
            sql.append(" AND p.title ILIKE CONCAT('%', TRIM(#{name}), '%') ");
        }

        List<UUID> skills = (List<UUID>) params.get("skills");

        if (skills != null && !skills.isEmpty() ) {
            Integer count = skills.size();
            sql.append(" AND p.project_id IN (" +
                    " SELECT project_id " +
                    "FROM project_skills " +
                    "WHERE skill_id IN (" );
            for (UUID skillId : skills) {
                sql.append("'").append(skillId).append("'");
                if (!skills.getLast().equals(skillId)) {
                    sql.append(" , ");
                }
            }
            sql.append(") GROUP BY project_id" +
                    " HAVING COUNT(DISTINCT skill_id) = ").append(count).append(" )");
        }

        return sql.toString();
    }

    public String getCountAllHackathon(String title) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM hackathons WHERE 1 = 1");

        System.out.println(title);
        if (title != null && !title.isEmpty()) {
            sql.append(" AND title ILIKE CONCAT('%', TRIM('").append(title).append("'), '%')");
        }
        return sql.toString();
    }

    public String getCountAllHackathonsByCurrentUser(Map<String, Object> params) {
        Object creatorId = params.get("creatorId");
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM hackathons WHERE user_id = '").append(creatorId).append("' ");

        Object title = params.get("title");
        if (title != null && !((String) title).isEmpty()) {
            sql.append(" AND title ILIKE CONCAT('%', TRIM(#{title}), '%')");
        }
        return sql.toString();
    }

    public String getCountJoinedHistory(Map<String, Object> params) {
        Object userId = params.get("appUserId");
        StringBuilder sql = new StringBuilder("SELECT COUNT(DISTINCT h.*) FROM hackathons h\n" +
                "                    LEFT JOIN join_hackathons jh ON jh.hackathon_id = h.hackathon_id\n" +
                "WHERE jh.user_id = '").append (userId).append("' AND finished_at < current_timestamp");

        Object title = params.get("title");
        if (title != null && !((String) title).isEmpty()) {
            sql.append(" AND title ILIKE CONCAT('%', TRIM(#{title}), '%')");
        }
        return sql.toString();
    }

    public String getCountAllJob(Map<String, Object> params) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(DISTINCT j.*) FROM jobs j \n" +
                " LEFT JOIN positions p ON j.position_id = p.position_id WHERE 1 = 1 ");

        List<UUID> skills = (List<UUID>) params.get("skills");

        if (skills != null && !skills.isEmpty() ) {
            Integer count = skills.size();
            sql.append(" AND j.job_id IN (" +
                    " SELECT job_id FROM job_skills WHERE job_id IN (" );
            for (UUID skillId : skills) {
                sql.append("'").append(skillId).append("'");
                if (!skills.getLast().equals(skillId)) {
                    sql.append(" , ");
                }
            }
            sql.append(")  GROUP BY job_id " +
                    " HAVING COUNT(DISTINCT job_id) = ").append(count).append(" )");
        }

        Object title = params.get("title");
        if (title != null && !((String) title).isEmpty()) {
            sql.append(" AND position_name ILIKE CONCAT('%', TRIM(#{title}), '%')");
        }
        return sql.toString();
    }

    public String getCountJobsByCreatorId(Map<String, Object> params) {
        Object creatorId = params.get("creatorId");
        StringBuilder sql = new StringBuilder("SELECT COUNT(DISTINCT j.*) FROM jobs j \n" +
                " LEFT JOIN positions p ON j.position_id = p.position_id WHERE user_id = #{creatorId} ");

        List<UUID> skills = (List<UUID>) params.get("skills");

        if (skills != null && !skills.isEmpty() ) {
            Integer count = skills.size();
            sql.append(" AND j.job_id IN (" +
                    " SELECT job_id FROM job_skills WHERE job_id IN (" );
            for (UUID skillId : skills) {
                sql.append("'").append(skillId).append("'");
                if (!skills.getLast().equals(skillId)) {
                    sql.append(" , ");
                }
            }
            sql.append(")  GROUP BY job_id " +
                    " HAVING COUNT(DISTINCT job_id) = ").append(count).append(" )");
        }

        Object title = params.get("title");
        if (title != null && !((String) title).isEmpty()) {
            sql.append(" AND position_name ILIKE CONCAT('%', TRIM(#{title}), '%')");
        }

        return sql.toString();
    }

    public String getJoinProjectByOwner(Map<String, Object> params) {
        Object userId = params.get("userId");
        StringBuilder sql = new StringBuilder("SELECT jp.* FROM projects p " +
                "JOIN join_projects jp ON p.project_id = jp.project_id WHERE p.user_id = " +
                "#{userId} ");

        Boolean isInvited = (Boolean) params.get("isInvited");

        if (isInvited) {
            sql.append("AND jp.is_invited = true ");
        } else {
            sql.append("AND jp.is_invited = false ");
        }

        Object positionId = params.get("positionId");
        if (positionId != null) {
            sql.append(" AND jp.position_id = #{positionId}");
        }

        Object date = params.get("date");
        if (date != null) {
            sql.append(" AND jp.created_at::date = #{date}::date ");
        }

        Object page = params.get("page");
        Object size = params.get("size");
        sql.append(" OFFSET #{page} LIMIT #{size}");
        return sql.toString();
    }

    public String getCountAllJoinedProject(Map<String, Object> params) {
        Object userId = params.get("userId");
        StringBuilder sql = new StringBuilder("SELECT COUNT(DISTINCT p.*) FROM projects p\n" +
                "LEFT JOIN join_projects jp on p.project_id = jp.project_id\n" +
                "WHERE jp.user_id = '").append(userId).append("' AND end_at < CURRENT_TIMESTAMP");

        Object name = params.get("name");
        if (name != null && !((String) name).isEmpty()) {
            sql.append(" AND p.title ILIKE CONCAT('%', TRIM(#{name}), '%') ");
        }

        List<UUID> skills = (List<UUID>) params.get("skills");

        if (skills != null && !skills.isEmpty() ) {
            Integer count = skills.size();
            sql.append(" AND p.project_id IN (" +
                    " SELECT project_id " +
                    "FROM project_skills " +
                    "WHERE skill_id IN (" );
            for (UUID skillId : skills) {
                sql.append("'").append(skillId).append("'");
                if (!skills.getLast().equals(skillId)) {
                    sql.append(" , ");
                }
            }
            sql.append(") GROUP BY project_id" +
                    " HAVING COUNT(DISTINCT skill_id) = ").append(count).append(" )");
        }

        return sql.toString();
    }

    public String getAllDeveloper(Map<String, Object> params) {
        StringBuilder sql = new StringBuilder("SELECT * FROM developer_profile dp\n" +
                "    INNER JOIN app_users a ON a.user_id = dp.user_id\n" +
                "         WHERE 1 = 1");
        Object name = params.get("name");
        if (name != null && !((String) name).isEmpty()) {
            sql.append(" AND CONCAT(a.first_name, a.last_name) ILIKE CONCAT('%', TRIM(#{name}), '%') ");
        }

        Object page = params.get("page");
        Object size = params.get("size");
        sql.append(" OFFSET #{page} LIMIT #{size}");
        return sql.toString();
    }

}
