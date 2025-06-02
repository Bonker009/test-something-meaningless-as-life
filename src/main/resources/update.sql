CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS app_users
(
    user_id           UUID PRIMARY KEY NOT NULL DEFAULT uuid_generate_v4(),
    first_name        VARCHAR(50)      NOT NULL,
    last_name         VARCHAR(50)      NOT NULL,
    email             VARCHAR(50)      NOT NULL UNIQUE,
    password          VARCHAR(100)     NOT NULL,
    is_recruiter      BOOLEAN          NOT NULL DEFAULT FALSE,
    is_verified       BOOLEAN          NOT NULL DEFAULT FALSE,
    profile_image_url VARCHAR(255),
    created_at        TIMESTAMP        NOT NULL DEFAULT CURRENT_TIMESTAMP
    );

-- job_types
CREATE TABLE IF NOT EXISTS job_types
(
    job_type_id UUID PRIMARY KEY   NOT NULL DEFAULT uuid_generate_v4(),
    type_name   VARCHAR(50) UNIQUE NOT NULL
    );

-- skill
CREATE TABLE IF NOT EXISTS skills
(
    skill_id   UUID PRIMARY KEY   NOT NULL DEFAULT uuid_generate_v4(),
    skill_name VARCHAR(50) UNIQUE NOT NULL
    );

CREATE TABLE IF NOT EXISTS developer_profile
(
    developer_id    UUID PRIMARY KEY NOT NULL DEFAULT uuid_generate_v4(),
    bio             VARCHAR(100),
    is_female       BOOLEAN          NOT NULL  DEFAULT FALSE,
    address         VARCHAR(200),
    phone_number    VARCHAR(20),
    cover_picture   TEXT,
    cv              TEXT,
    github_username VARCHAR(100),
    top_comment     INTEGER                    DEFAULT 0,
    mvp_count       INTEGER                    DEFAULT 0,
    top_one_count   INTEGER                    DEFAULT 0,
    employee_status employee_status,
    job_type_id     UUID,
    user_id         UUID              NOT NULL,
    position_id     UUID              NOT NULL,
    FOREIGN KEY (job_type_id) REFERENCES job_types (job_type_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (position_id) REFERENCES positions (position_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (user_id) REFERENCES app_users (user_id) ON DELETE CASCADE ON UPDATE CASCADE
    );

-- recruiters
CREATE TABLE IF NOT EXISTS recruiter_profile
(
    recruiter_id     UUID PRIMARY KEY NOT NULL DEFAULT uuid_generate_v4(),
    company_name     VARCHAR(100),
    phone_number     VARCHAR(20),
    industry         VARCHAR(100),
    company_location VARCHAR(200),
    bio              VARCHAR(100),
    company_site     VARCHAR(255),
    founded_at       TIMESTAMP,
    cover_picture    TEXT,
    user_id          UUID             NOT NULL,
    FOREIGN KEY (user_id) REFERENCES app_users (user_id) ON DELETE CASCADE ON UPDATE CASCADE
    );

-- jobs
CREATE TABLE IF NOT EXISTS jobs
(
    job_id      UUID PRIMARY KEY NOT NULL DEFAULT uuid_generate_v4(),
    salary      VARCHAR(50)      NOT NULL,
    location    VARCHAR(200)     NOT NULL,
    status      BOOLEAN          NOT NULL DEFAULT FALSE,
    description TEXT,
    job_board   JSONB            NOT NULL,
    pax         INTEGER          NOT NULL DEFAULT 0,
    posted_date TIMESTAMP                 DEFAULT CURRENT_TIMESTAMP,
    job_type_id     UUID         NOT NULL,
    user_id         UUID         NOT NULL,
    position_id     UUID         NOT NULL,
    FOREIGN KEY (job_type_id) REFERENCES job_types (job_type_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (position_id) REFERENCES positions (position_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (user_id) REFERENCES app_users (user_id) ON DELETE CASCADE ON UPDATE CASCADE
    );

-- job_skill
CREATE TABLE IF NOT EXISTS job_skills
(
    job_id   UUID NOT NULL,
    skill_id UUID NOT NULL,
    FOREIGN KEY (job_id) REFERENCES jobs (job_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (skill_id) REFERENCES skills (skill_id) ON DELETE CASCADE ON UPDATE CASCADE,
    PRIMARY KEY (job_id, skill_id)
    );


-- developer_skill
CREATE TABLE IF NOT EXISTS developer_skills
(
    user_id  UUID NOT NULL,
    skill_id UUID NOT NULL,
    FOREIGN KEY (user_id) REFERENCES app_users (user_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (skill_id) REFERENCES skills (skill_id) ON DELETE CASCADE ON UPDATE CASCADE,
    PRIMARY KEY (user_id, skill_id)
    );

-- badge
CREATE TABLE IF NOT EXISTS badges
(
    badge_id    UUID PRIMARY KEY NOT NULL DEFAULT uuid_generate_v4(),
    name        VARCHAR(50)      NOT NULL UNIQUE,
    icon        VARCHAR(255)     NOT NULL UNIQUE,
    description TEXT             NOT NULL UNIQUE
    );

-- developer_badge
CREATE TABLE IF NOT EXISTS developer_badges
(
    user_id  UUID NOT NULL,
    badge_id UUID NOT NULL,
    FOREIGN KEY (user_id) REFERENCES app_users (user_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (badge_id) REFERENCES badges (badge_id) ON DELETE CASCADE ON UPDATE CASCADE,
    PRIMARY KEY (user_id, badge_id)
    );

-- topics
CREATE TABLE IF NOT EXISTS topics
(
    topic_id   UUID PRIMARY KEY NOT NULL DEFAULT uuid_generate_v4(),
    title      VARCHAR(255)  NOT NULL,
    content    TEXT          NOT NULL,
    created_at TIMESTAMP               DEFAULT CURRENT_TIMESTAMP,
    user_id    UUID          NOT NULL,
    FOREIGN KEY (user_id) REFERENCES app_users (user_id) ON DELETE CASCADE ON UPDATE CASCADE
    );


-- comments
CREATE TABLE IF NOT EXISTS comments
(
    comment_id    UUID PRIMARY KEY NOT NULL DEFAULT uuid_generate_v4(),
    text          TEXT             NOT NULL,
    total_upvotes INTEGER          NOT NULL DEFAULT 0,
    created_at    TIMESTAMP                 DEFAULT CURRENT_TIMESTAMP,
    edited_at     TIMESTAMP,
    topic_id      UUID REFERENCES topics (topic_id) ON DELETE CASCADE ON UPDATE CASCADE,
    parent_id     UUID REFERENCES comments (comment_id) ON DELETE CASCADE ON UPDATE CASCADE,
    user_id       UUID REFERENCES app_users (user_id) ON DELETE CASCADE ON UPDATE CASCADE
    );


-- upvote
CREATE TABLE IF NOT EXISTS upvotes
(
    comment_id UUID NOT NULL,
    user_id    UUID NOT NULL,
    FOREIGN KEY (comment_id) REFERENCES comments (comment_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (user_id) REFERENCES app_users (user_id) ON DELETE CASCADE ON UPDATE CASCADE,
    PRIMARY KEY (comment_id, user_id)
    );

-- resume
CREATE TABLE IF NOT EXISTS resumes
(
    resume_id       UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    full_name       VARCHAR(100) NOT NULL,
    picture         VARCHAR(255) NOT NULL,
    is_female       BOOLEAN      NOT NULL DEFAULT FALSE,
    phone_number    VARCHAR(20)  NOT NULL,
    address         VARCHAR(255) NOT NULL,
    email           VARCHAR(100) NOT NULL,
    dob             DATE         NOT NULL,
    place_of_birth  VARCHAR(255) NOT NULL,
    position        VARCHAR(100) NOT NULL,
    description     TEXT         NOT NULL,
    information     JSONB        NOT NULL,
    user_id         UUID         NOT NULL,
    FOREIGN KEY (user_id) REFERENCES app_users (user_id) ON DELETE CASCADE ON UPDATE CASCADE
    );

-- join_job
CREATE TABLE IF NOT EXISTS join_jobs
(
    title        VARCHAR(100) NOT NULL,
    description  TEXT         NOT NULL,
    is_approved  BOOLEAN      NOT NULL DEFAULT FALSE,
    cover_letter TEXT         NOT NULL,
    is_invited   BOOLEAN               DEFAULT FALSE,
    created_at   TIMESTAMP             DEFAULT CURRENT_TIMESTAMP,
    job_id       UUID         NOT NULL,
    user_id      UUID         NOT NULL,
    FOREIGN KEY (job_id) REFERENCES jobs (job_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (user_id) REFERENCES app_users (user_id) ON DELETE CASCADE ON UPDATE CASCADE,
    PRIMARY KEY (user_id, job_id)
    );

-- code_challenge
CREATE TABLE IF NOT EXISTS code_challenges
(
    challenge_id   UUID    PRIMARY KEY DEFAULT uuid_generate_v4(),
    title          VARCHAR(100) NOT NULL,
    instruction    TEXT         NOT NULL,
    description    VARCHAR(255) NOT NULL,
    test_case      JSONB        NOT NULL,
    created_at     TIMESTAMP            DEFAULT CURRENT_TIMESTAMP,
    score          INTEGER      NOT NULL DEFAULT 0,
    language       VARCHAR(30)  NOT NULL,
    start_code     TEXT         NOT NULL,
    user_id        UUID         NOT NULL,
    FOREIGN KEY (user_id) REFERENCES app_users (user_id) ON DELETE CASCADE ON UPDATE CASCADE
    );

-- submission
CREATE TABLE IF NOT EXISTS submissions
(
    score          INTEGER              DEFAULT 0,
    submitted_time INTEGER     NOT NULL,
    created_at     TIMESTAMP            DEFAULT CURRENT_TIMESTAMP,
    challenge_id   UUID        NOT NULL,
    user_id        UUID        NOT NULL,
    FOREIGN KEY (user_id) REFERENCES app_users (user_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (challenge_id) REFERENCES code_challenges (challenge_id) ON DELETE CASCADE ON UPDATE CASCADE,
    PRIMARY KEY (challenge_id, user_id)
    );

-- hackathons
CREATE TABLE IF NOT EXISTS hackathons
(
    hackathon_id UUID PRIMARY KEY NOT NULL DEFAULT uuid_generate_v4(),
    title        VARCHAR(100)          NOT NULL,
    description  VARCHAR(255)          NOT NULL,
    started_at   TIMESTAMP             NOT NULL,
    finished_at  TIMESTAMP             NOT NULL,
    created_at   TIMESTAMP                       DEFAULT CURRENT_TIMESTAMP,
    full_score   INTEGER                NOT NULL DEFAULT 0,
    is_available BOOLEAN                NOT NULL DEFAULT FALSE,
    user_id      UUID                   NOT NULL,
    FOREIGN KEY (user_id) REFERENCES app_users (user_id) ON DELETE CASCADE ON UPDATE CASCADE
    );

-- join_hackathon
CREATE TABLE IF NOT EXISTS join_hackathons
(
    score        INTEGER            DEFAULT 0,
    joined_at    TIMESTAMP          DEFAULT CURRENT_TIMESTAMP,
    submission   TEXT,
    submitted_at TIMESTAMP,
    hackathon_id UUID       NOT NULL,
    user_id      UUID       NOT NULL,
    FOREIGN KEY (user_id) REFERENCES app_users (user_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (hackathon_id) REFERENCES hackathons (hackathon_id) ON DELETE CASCADE ON UPDATE CASCADE,
    PRIMARY KEY (user_id, hackathon_id)
    );

-- hackathon_certificate
CREATE TABLE IF NOT EXISTS hackathon_certificate
(
    certificate_id UUID PRIMARY KEY NOT NULL DEFAULT uuid_generate_v4(),
    description    TEXT             NOT NULL,
    issued_date    TIMESTAMP        NOT NULL DEFAULT CURRENT_DATE,
    hackathon_id   UUID             NOT NULL,
    user_id        UUID             NOT NULL,
    FOREIGN KEY (user_id) REFERENCES app_users (user_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (hackathon_id) REFERENCES hackathons (hackathon_id) ON DELETE CASCADE ON UPDATE CASCADE,
    UNIQUE (user_id, hackathon_id)
    );

-- projects
CREATE TABLE IF NOT EXISTS projects
(
    project_id   UUID PRIMARY KEY NOT NULL DEFAULT uuid_generate_v4(),
    title        VARCHAR(100)          NOT NULL,
    description  VARCHAR(255)          NOT NULL,
    is_open      BOOLEAN       NOT NULL DEFAULT TRUE,
    start_at     TIMESTAMP     NOT NULL,
    end_at       TIMESTAMP     NOT NULL,
    created_at   TIMESTAMP              DEFAULT CURRENT_TIMESTAMP,
    user_id      UUID          NOT NULL,
    FOREIGN KEY (user_id) REFERENCES app_users (user_id) ON DELETE CASCADE ON UPDATE CASCADE
    );

-- positions
CREATE TABLE IF NOT EXISTS positions
(
    position_id   UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    position_name VARCHAR(50) UNIQUE NOT NULL
    );

-- position_limits
CREATE TABLE IF NOT EXISTS project_positions
(
    max_members  INTEGER DEFAULT 0,
    project_id   UUID NOT NULL,
    positions_id UUID NOT NULL,
    FOREIGN KEY (project_id) REFERENCES projects (project_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (positions_id) REFERENCES positions (position_id) ON DELETE CASCADE ON UPDATE CASCADE,
    PRIMARY KEY (project_id, positions_id)
    );

-- join_project
CREATE TABLE IF NOT EXISTS join_projects
(
    title       VARCHAR(100)    NOT NULL,
    description VARCHAR(255)    NOT NULL,
    is_approved BOOLEAN         NOT NULL DEFAULT FALSE,
    is_invited  BOOLEAN         NOT NULL DEFAULT FALSE,
    created_at  TIMESTAMP                DEFAULT CURRENT_TIMESTAMP,
    project_id  UUID            NOT NULL,
    user_id     UUID            NOT NULL,
    position_id UUID            NOT NULL,
    FOREIGN KEY (project_id) REFERENCES projects (project_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (user_id) REFERENCES app_users (user_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (position_id) REFERENCES positions (position_id) ON DELETE CASCADE ON UPDATE CASCADE,
    PRIMARY KEY (project_id, user_id, position_id)
    );

CREATE TABLE IF NOT EXISTS project_skills
(
    project_id UUID NOT NULL,
    skill_id   UUID NOT NULL,
    FOREIGN KEY (project_id) REFERENCES projects (project_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (skill_id) REFERENCES skills (skill_id) ON DELETE CASCADE ON UPDATE CASCADE,
    PRIMARY KEY (project_id, skill_id)
    );

-- bookmarks
CREATE TABLE IF NOT EXISTS bookmarks
(
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    target_id   UUID        NOT NULL,
    bookmark_by UUID        NOT NULL,
    FOREIGN KEY (bookmark_by) REFERENCES app_users (user_id) ON DELETE CASCADE ON UPDATE CASCADE,
    PRIMARY KEY (target_id, bookmark_by)

    );

CREATE TABLE IF NOT EXISTS topic_skills (
   topic_id uuid NOT NULL,
   skill_id uuid NOT NULL,
   FOREIGN KEY (topic_id) REFERENCES topics(topic_id),
   FOREIGN KEY (skill_id) REFERENCES skills(skill_id)
);
-- create ENUM type for employee_status
CREATE TYPE employee_status AS ENUM ('EMPLOYED', 'UNEMPLOYED', 'FREELANCE');

select j.* from jobs j
    join job_skills js on j.job_id = js.job_id
    join job_types jt on j.job_type_id = jt.job_type_id
    join positions p on j.position_id = p.position_id
where p.position_name ilike '%a%';

select * from projects where user_id = 'bcd45aa7-936c-4f07-a105-a5f171e9cf18';

SELECT  count(distinct p.*)
FROM projects p
        LEFT JOIN project_skills ps ON p.project_id = ps.project_id
        LEFT JOIN project_positions pp ON p.project_id = pp.project_id
WHERE p.user_id = 'bcd45aa7-936c-4f07-a105-a5f171e9cf18';

SELECT DISTINCT j.* FROM jobs j
LEFT JOIN job_skills js on j.job_id = js.job_id
LEFT JOIN job_types jt on j.job_type_id = jt.job_type_id
LEFT JOIN positions p on j.position_id = p.position_id WHERE user_id = '';

SELECT COUNT(p.*) FROM projects p JOIN join_projects jp ON p.project_id = jp.project_id WHERE jp.user_id = '850b7252-2faf-4a46-9710-4aba6feabf38';

SELECT DISTINCT * FROM jobs j
LEFT JOIN job_skills js on j.job_id = js.job_id;

SELECT DISTINCT p.*
FROM projects p
WHERE 1 = 1 AND p.project_id IN (
    SELECT project_id
    FROM project_skills
    WHERE skill_id IN ('20edbc6b-4027-4d18-86f3-cdebc1d23a48', 'ae0d3f63-2666-427b-baa5-a986ce5607f7')
    GROUP BY project_id
    HAVING COUNT(DISTINCT skill_id) = 2
);

SELECT COUNT(p.*) FROM projects p JOIN join_projects jp ON p.project_id = jp.project_id WHERE jp.user_id = 'bcd45aa7-936c-4f07-a105-a5f171e9cf18'
                                                                                          AND end_at < CURRENT_TIMESTAMP AND p.project_id IN (
        SELECT project_id
        FROM project_skills
        WHERE skill_id IN ('20edbc6b-4027-4d18-86f3-cdebc1d23a48', 'ae0d3f63-2666-427b-baa5-a986ce5607f7')
        GROUP BY project_id
        HAVING COUNT(DISTINCT skill_id) = 2
    );

SELECT COUNT(DISTINCT p.*) FROM projects p WHERE p.user_id = 'bcd45aa7-936c-4f07-a105-a5f171e9cf18';

SELECT DISTINCT h.* FROM hackathons h
                    LEFT JOIN join_hackathons jh ON jh.hackathon_id = h.hackathon_id
WHERE jh.user_id = 'bcd45aa7-936c-4f07-a105-a5f171e9cf18' AND finished_at < current_timestamp;

select distinct * from jobs j left join positions p on j.position_id = p.position_id where j.job_id in (
    select job_id from job_skills where skill_id in ('29593466-eadc-46f2-829d-d79cee68c7ee')
                                  group by job_id
                                  having count(distinct skill_id) = 1
    ) and p.position_name ilike '%a%';

-- SELECT COUNT(DISTINCT j.*) FROM jobs j
-- LEFT JOIN positions p ON j.position_id = p.position_id WHERE j.job_id IN (
--     SELECT job_id FROM job_skills WHERE job_id IN ()
--                                   GROUP BY job_id
--                                   HAVING COUNT(DISTINCT job_id) = 1
--     );

SELECT DISTINCT j.* FROM jobs j LEFT JOIN positions p ON j.position_id = p.position_id WHERE 1 = 1;

SELECT jp.* FROM projects p
    JOIN join_projects jp ON p.project_id = jp.project_id
            WHERE p.user_id = 'bcd45aa7-936c-4f07-a105-a5f171e9cf18'
              AND jp.is_invited = false AND jp.position_id = '';

SELECT count(jp.user_id) FROM join_projects jp
    LEFT JOIN project_positions pp ON jp.project_id = pp.project_id
    LEFT JOIN positions p ON p.position_id = pp.position_id WHERE jp.project_id = 'd4e5f6a7-b8c9-4d0e-1f2a-3b4c5d6e7f8a' AND jp.position_id = '417f6426-8419-4802-a29a-2ab91626ff4b' AND is_approved = true;

DELETE FROM bookmarks WHERE target_id = 'f2a3b4c5-d6e7-4f8a-9b0c-1d2e3f4a5b7c' AND bookmark_by = 'bcd45aa7-936c-4f07-a105-a5f171e9cf18';

-- UPDATE developer_profile dp SET dp.mvp_count = 1 WHERE
SELECT DISTINCT dp.* FROM hackathons h
LEFT JOIN join_hackathons jh ON h.hackathon_id = jh.hackathon_id
LEFT JOIN developer_profile dp ON dp.user_id = jh.user_id WHERE h.hackathon_id = '4d5e6f7a-8b9c-0d1e-2f3a-4b5c6d7e8f9a';

select * from hackathons h
    inner join join_hackathons jh on h.hackathon_id = jh.hackathon_id
where h.hackathon_id = '4d5e6f7a-8b9c-0d1e-2f3a-4b5c6d7e8f9a';

SELECT *
FROM join_hackathons
WHERE hackathon_id = '4d5e6f7a-8b9c-0d1e-2f3a-4b5c6d7e8f9a'
  AND submitted_at IS NOT NULL
ORDER BY score DESC, submitted_at ASC;

UPDATE join_hackathons
SET submission = '', submitted_at = CURRENT_TIMESTAMP
WHERE hackathon_id = '' AND user_id = '';

SELECT distinct * FROM join_jobs jj
    left join job_skills js on jj.job_id = js.job_id
where is_approved = true and jj.job_id = '5ee6d2dd-fb86-4126-bd22-a22f94ea2560';

select distinct jj.*
from jobs j left join join_jobs jj on jj.job_id = j.job_id where j.user_id = 'b790bfe5-533e-4b53-ac20-6bde97a9c3a3';

SELECT DISTINCT jj.* FROM jobs j
    LEFT JOIN join_jobs jj ON j.job_id = jj.job_id
                     WHERE j.user_id = 'b790bfe5-533e-4b53-ac20-6bde97a9c3a3'
                       AND j.job_type_id = 'aeb4f0dd-15d5-4a6f-b118-87df7994ca58'
AND j.position_id = '';

DELETE FROM join_projects jp where jp.project_id = '' and jp.user_id = '';

SELECT COUNT(DISTINCT p.*) FROM projects p
LEFT JOIN join_projects jp on p.project_id = jp.project_id
WHERE jp.user_id = 'd19e0587-d2fa-4210-9d46-1023f5340975' AND end_at < CURRENT_TIMESTAMP AND p.project_id IN (
    SELECT project_id
    FROM project_skills
    WHERE skill_id IN ('742c692b-1be0-4293-a02c-b5ac652d7a09')
    GROUP BY project_id
    HAVING COUNT(DISTINCT skill_id) = 1
    );

select distinct ps.*
from projects p left join project_skills ps on p.project_id = ps.project_id WHERE p.project_id = 'a7b8c9d0-e1f2-4a3b-5c6d-7e8f9a0b1c2d';

select * from developer_profile dp
    inner join app_users a on a.user_id = dp.user_id
         where 1=1 and concat(a.first_name, a.last_name) ilike concat('%', trim(' '), '%');

SELECT
    p.title AS "project name",
    p.user_id AS "creator project",
    (b.target_id IS NOT NULL) AS is_bookmark
FROM
    projects p
        LEFT JOIN
    bookmarks b ON p.project_id = b.target_id;


SELECT DISTINCT p.*, (b.target_id IS NOT NULL) AS is_bookmark
FROM projects p LEFT JOIN
     bookmarks b ON p.project_id = b.target_id
WHERE 1 = 1 AND p.project_id IN (
    SELECT project_id
    FROM project_skills
    WHERE skill_id IN ('27ac2a4f-7b85-4f0e-ae0a-989305fd724d')
    GROUP BY project_id
    HAVING COUNT(DISTINCT skill_id) = 1
);

select *
from projects p left join project_skills ps on p.project_id = ps.project_id where p.project_id = 'c62980af-5eaa-4e96-9df8-5a1a5500b899';

select h.*, (b.target_id IS NOT NULL) as is_bookmark
from hackathons h left join bookmarks b ON b.target_id = h.hackathon_id;

select *
from hackathons where hackathon_id = 'd73bb13a-7ac6-4391-91df-a73cc7fdd9be';

SELECT DISTINCT j.*, (b.target_id IS NOT NULL ) as is_bookmark FROM jobs j LEFT JOIN bookmarks b ON b.target_id = j.job_id
    LEFT JOIN positions p ON j.position_id = p.position_id WHERE 1 = 1;

select *
from join_jobs where job_id = '05db417c-ec25-4d16-8fb6-eba10b724e01' and created_at::date = '2025-05-30';





CREATE OR REPLACE FUNCTION increase_top_comment_count()
    RETURNS TRIGGER AS $$
DECLARE
    max_upvotes INTEGER;
    current_top_user UUID;
BEGIN
    -- Check if new total_upvotes is >= 100
    IF NEW.total_upvotes >= 100 THEN

        -- Find max upvotes in this topic (only those >= 100)
        SELECT MAX(total_upvotes)
        INTO max_upvotes
        FROM comments
        WHERE topic_id = NEW.topic_id AND total_upvotes >= 100;

        -- Find user who owns the current top comment
        SELECT user_id
        INTO current_top_user
        FROM comments
        WHERE topic_id = NEW.topic_id
          AND total_upvotes = max_upvotes
        ORDER BY created_at DESC -- or DESC depending on preference
        LIMIT 1;

        -- If this comment becomes the new top one, and belongs to the updating user
        IF NEW.user_id = current_top_user THEN
            UPDATE developer_profile
            SET top_comment = top_comment + 1
            WHERE user_id = NEW.user_id;
        END IF;

    END IF;

    RETURN NULL;
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE TRIGGER trg_top_comment_upvote
    AFTER UPDATE ON comments
    FOR EACH ROW
    WHEN (OLD.total_upvotes < 100 AND NEW.total_upvotes >= 100)
EXECUTE FUNCTION increase_top_comment_count();


