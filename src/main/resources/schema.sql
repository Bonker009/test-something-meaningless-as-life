CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE TABLE IF NOT EXISTS app_users (
    user_id UUID PRIMARY KEY DEFAULT uuid_generate_v4() ,
    first_name  VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email     VARCHAR(50) NOT NULL UNIQUE ,
    password  VARCHAR(100) NOT NULL,
    is_recruiter BOOLEAN DEFAULT FALSE,
    is_verified BOOLEAN DEFAULT FALSE,
    profile_image_url VARCHAR,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

-- job_types
CREATE TABLE IF NOT EXISTS job_types (
     job_type_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
     type_name VARCHAR(50) UNIQUE NOT NULL
    );

-- skill
CREATE TABLE IF NOT EXISTS skills (
    skill_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    skill_name VARCHAR(50) UNIQUE NOT NULL
    );

CREATE TABLE IF NOT EXISTS developer_profiles (
    developer_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    bio VARCHAR(100),
    gender VARCHAR(10),
    address VARCHAR(200),
    cover_picture VARCHAR(255),
    cv VARCHAR,
    github_username VARCHAR(100),
    top_comment INTEGER DEFAULT 0,
    mvp_count INTEGER DEFAULT 0,
    top_one_count INTEGER DEFAULT 0,
    employee_status BOOLEAN DEFAULT FALSE,
    job_type_id UUID NOT NULL ,
    user_id UUID NOT NULL ,
    FOREIGN KEY (job_type_id) REFERENCES job_types(job_type_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (user_id) REFERENCES app_users(user_id) ON DELETE CASCADE ON UPDATE CASCADE
    );

-- recruiters
CREATE TABLE IF NOT EXISTS recruiter_profiles (
    recruiter_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    company_name VARCHAR(100),
    gender VARCHAR(10),
    phone_number VARCHAR(14),
    industry VARCHAR(100),
    company_location VARCHAR(200),
    bio VARCHAR(100),
    establish_date DATE,
    cover_picture VARCHAR(255),
    user_id UUID NOT NULL ,
    FOREIGN KEY (user_id) REFERENCES app_users(user_id) ON DELETE CASCADE ON UPDATE CASCADE
    );

-- jobs
CREATE TABLE IF NOT EXISTS jobs (
    job_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    title VARCHAR(100) NOT NULL ,
    salary VARCHAR(50) NOT NULL ,
    location VARCHAR(200) NOT NULL ,
    status BOOLEAN DEFAULT FALSE,
    description VARCHAR(100),
    job_board JSONB NOT NULL,
    pax INTEGER DEFAULT 0,
    posted_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    creator_id UUID REFERENCES app_users(user_id) ON DELETE CASCADE ON UPDATE CASCADE
    );

-- job_skill
CREATE TABLE IF NOT EXISTS job_skills (
    job_id UUID NOT NULL,
    skill_id UUID NOT NULL,
    FOREIGN KEY (job_id) REFERENCES jobs(job_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (skill_id) REFERENCES skills(skill_id) ON DELETE CASCADE ON UPDATE CASCADE,
    PRIMARY KEY (job_id, skill_id)
    );


-- developer_skill
CREATE TABLE IF NOT EXISTS developer_skills (
    user_id UUID NOT NULL,
    skill_id UUID NOT NULL,
    FOREIGN KEY (user_id) REFERENCES app_users(user_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (skill_id) REFERENCES skills(skill_id) ON DELETE CASCADE ON UPDATE CASCADE,
    PRIMARY KEY (user_id, skill_id)
    );

-- badge
CREATE TABLE IF NOT EXISTS badges (
    badge_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(50) NOT NULL UNIQUE ,
    icon VARCHAR(255) NOT NULL ,
    description VARCHAR(100) NOT NULL
    );

-- developer_badge
CREATE TABLE IF NOT EXISTS developer_badges (
    user_id UUID NOT NULL,
    badge_id UUID NOT NULL,
    FOREIGN KEY (user_id) REFERENCES app_users(user_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (badge_id) REFERENCES badges(badge_id) ON DELETE CASCADE ON UPDATE CASCADE,
    PRIMARY KEY (user_id, badge_id)
    );

-- topics
CREATE TABLE IF NOT EXISTS topics (
    topic_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    title VARCHAR(255) NOT NULL ,
    content VARCHAR NOT NULL ,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    user_id UUID NOT NULL,
    FOREIGN KEY (user_id) REFERENCES app_users(user_id) ON DELETE CASCADE ON UPDATE CASCADE
    );


-- comments
CREATE TABLE IF NOT EXISTS comments (
    comment_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    text VARCHAR NOT NULL ,
    total_upvotes INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    edited_at TIMESTAMP,
    topic_id UUID NOT NULL ,
    parent_id UUID NOT NULL,
    user_id UUID NOT NULL ,
    FOREIGN KEY (topic_id) REFERENCES topics(topic_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (comment_id) REFERENCES comments(comment_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (user_id) REFERENCES app_users(user_id) ON DELETE CASCADE ON UPDATE CASCADE
    );

-- upvote
CREATE TABLE IF NOT EXISTS upvote (
    comment_id UUID NOT NULL,
    user_id UUID NOT NULL,
    FOREIGN KEY (comment_id) REFERENCES comments(comment_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (user_id) REFERENCES app_users(user_id) ON DELETE CASCADE ON UPDATE CASCADE,
    PRIMARY KEY (comment_id, user_id)
    );

-- resume
CREATE TABLE IF NOT EXISTS resumes (
    resume_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    fullname VARCHAR(100) NOT NULL ,
    gender VARCHAR(10),
    phone_number VARCHAR(20) NOT NULL ,
    address VARCHAR(200) NOT NULL ,
    email VARCHAR(100) NOT NULL ,
    dob DATE NOT NULL ,
    position VARCHAR(100) NOT NULL ,
    description VARCHAR NOT NULL ,
    information JSONB NOT NULL ,
    user_id UUID NOT NULL,
    FOREIGN KEY (user_id) REFERENCES app_users(user_id) ON DELETE CASCADE ON UPDATE CASCADE
    );

-- join_job
CREATE TABLE IF NOT EXISTS join_jobs (
    title VARCHAR(100) NOT NULL ,
    description VARCHAR NOT NULL ,
    is_approve BOOLEAN DEFAULT FALSE,
    job_id UUID NOT NULL,
    user_id UUID NOT NULL,
    FOREIGN KEY (job_id) REFERENCES jobs(job_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (user_id) REFERENCES app_users(user_id) ON DELETE CASCADE ON UPDATE CASCADE,
    PRIMARY KEY (user_id, job_id)
    );

-- code_challenge
CREATE TABLE IF NOT EXISTS code_challenges (
    challenge_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    title VARCHAR(100) NOT NULL ,
    description VARCHAR NOT NULL ,
    instruction VARCHAR NOT NULL,
    test_case JSONB NOT NULL ,
    problem_detail VARCHAR NOT NULL ,
    language VARCHAR(30) NOT NULL,
    starter_code VARCHAR NOT NULL ,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    score INTEGER DEFAULT 0,
    user_id UUID NOT NULL,
    FOREIGN KEY (user_id) REFERENCES app_users(user_id) ON DELETE CASCADE ON UPDATE CASCADE
    );

-- submission
CREATE TABLE IF NOT EXISTS submissions (
    score INTEGER DEFAULT 0,
    submit_time VARCHAR(50) NOT NULL ,
    challenge_id UUID NOT NULL,
    developer_id UUID NOT NULL,
    user_id UUID NOT NULL,
    FOREIGN KEY (user_id) REFERENCES app_users(user_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (challenge_id) REFERENCES code_challenges(challenge_id) ON DELETE CASCADE ON UPDATE CASCADE,
    PRIMARY KEY (challenge_id, user_id)
    );

-- hackathons
CREATE TABLE IF NOT EXISTS hackathons (
    hackathon_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    title VARCHAR(100) NOT NULL ,
    description VARCHAR NOT NULL ,
    start_at TIMESTAMP,
    finished_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_available BOOLEAN DEFAULT FALSE,
    user_id UUID NOT NULL,
    FOREIGN KEY (user_id) REFERENCES app_users(user_id) ON DELETE CASCADE ON UPDATE CASCADE
    );

-- join_hackathon
CREATE TABLE IF NOT EXISTS join_hackathons (
    score INTEGER DEFAULT 0,
    joined_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    submission VARCHAR,
    hackathon_id UUID NOT NULL,
    user_id UUID NOT NULL,
    FOREIGN KEY (user_id) REFERENCES app_users(user_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (hackathon_id) REFERENCES hackathons(hackathon_id) ON DELETE CASCADE ON UPDATE CASCADE,
    PRIMARY KEY (user_id, hackathon_id)
    );

-- hackathon_certificate
CREATE TABLE IF NOT EXISTS hackathon_certificate (
     description VARCHAR NOT NULL ,
     issued_date TIMESTAMP NOT NULL ,
     hackathon_id UUID NOT NULL,
     user_id UUID NOT NULL,
     FOREIGN KEY (user_id) REFERENCES app_users(user_id) ON DELETE CASCADE ON UPDATE CASCADE,
     FOREIGN KEY (hackathon_id) REFERENCES hackathons(hackathon_id) ON DELETE CASCADE ON UPDATE CASCADE,
     PRIMARY KEY (user_id, hackathon_id)
    );

-- projects
CREATE TABLE IF NOT EXISTS projects (
    project_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    title VARCHAR(100) NOT NULL ,
    description VARCHAR NOT NULL ,
    is_open BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    user_id UUID NOT NULL,
    FOREIGN KEY (user_id) REFERENCES app_users(user_id) ON DELETE CASCADE ON UPDATE CASCADE
    );

-- positions
CREATE TABLE IF NOT EXISTS positions (
    position_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    position_name VARCHAR(50) UNIQUE NOT NULL
    );

-- position_limits
CREATE TABLE IF NOT EXISTS project_positions (
    max_members INTEGER DEFAULT 0,
    project_id UUID NOT NULL,
    position_id UUID NOT NULL,
    FOREIGN KEY (project_id) REFERENCES projects(project_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (position_id) REFERENCES positions(position_id) ON DELETE CASCADE ON UPDATE CASCADE,
    PRIMARY KEY (project_id, position_id)
    );

-- join_project
CREATE TABLE IF NOT EXISTS join_projects (
    title VARCHAR(100) NOT NULL ,
    description VARCHAR NOT NULL ,
    is_approved BOOLEAN DEFAULT FALSE,
    project_id UUID NOT NULL ,
    user_id UUID NOT NULL,
    position_id UUID NOT NULL,
    FOREIGN KEY (position_id) REFERENCES positions(position_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (project_id) REFERENCES projects(project_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (user_id) REFERENCES app_users(user_id) ON DELETE CASCADE ON UPDATE CASCADE,
    PRIMARY KEY (project_id, user_id)
    );

CREATE TABLE IF NOT EXISTS project_skills (
    project_id UUID NOT NULL,
    skill_id UUID NOT NULL,
    FOREIGN KEY (project_id) REFERENCES projects(project_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (skill_id) REFERENCES skills(skill_id) ON DELETE CASCADE ON UPDATE CASCADE,
    PRIMARY KEY (project_id, skill_id)
    );

-- bookmarks
CREATE TABLE IF NOT EXISTS bookmarks (
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    target_id UUID NOT NULL,
    target_type VARCHAR(50) NOT NULL,
    bookmark_by UUID NOT NULL,
    FOREIGN KEY (bookmark_by) REFERENCES app_users(user_id) ON DELETE CASCADE ON UPDATE CASCADE,
    PRIMARY KEY (target_id, bookmark_by, target_type),
    CHECK (target_type IN ('project', 'hackathon', 'recruiter', 'developer', 'job'))
    );

-- topic_skill
CREATE TABLE IF NOT EXISTS topic_skill (
   topic_id uuid NOT NULL,
   skill_id uuid NOT NULL,
   FOREIGN KEY (topic_id) REFERENCES topics(topic_id),
   FOREIGN KEY (skill_id) REFERENCES skills(skill_id)
);

