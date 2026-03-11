
-- ==============================
-- CREATE DATABASE
-- ==============================
CREATE DATABASE IF NOT EXISTS revhirep2;
USE revhirep2;

-- ==============================
-- USERS (NO CASCADE)
-- ==============================
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    security_question VARCHAR(255) NOT NULL,
	security_answer VARCHAR(255) NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- ==============================
-- JOB SEEKERS (NO CASCADE)
-- ==============================
CREATE TABLE job_seekers (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL UNIQUE,
    full_name VARCHAR(150),
    phone VARCHAR(20),
    location VARCHAR(150),
    employment_status VARCHAR(50),
    profile_summary TEXT,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- ==============================
-- EMPLOYERS (NO CASCADE)
-- ==============================
CREATE TABLE employers (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL UNIQUE,
    company_name VARCHAR(200),
    industry VARCHAR(150),
    company_size VARCHAR(50),
    description TEXT,
    website VARCHAR(200),
    location VARCHAR(150),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- ==============================
-- RESUMES (NO CASCADE)
-- ==============================
CREATE TABLE resumes (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    job_seeker_id BIGINT NOT NULL UNIQUE,
    objective TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (job_seeker_id) REFERENCES job_seekers(id)
);

-- ==============================
-- EDUCATION (SAFE CASCADE)
-- ==============================
CREATE TABLE education (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    resume_id BIGINT NOT NULL,
    degree VARCHAR(150),
    institution VARCHAR(200),
    start_date DATE,
    end_date DATE,
    grade VARCHAR(50),
    FOREIGN KEY (resume_id) REFERENCES resumes(id) ON DELETE CASCADE
);

-- ==============================
-- EXPERIENCE (SAFE CASCADE)
-- ==============================
CREATE TABLE experience (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    resume_id BIGINT NOT NULL,
    company_name VARCHAR(200),
    job_title VARCHAR(150),
    start_date DATE,
    end_date DATE,
    description TEXT,
    FOREIGN KEY (resume_id) REFERENCES resumes(id) ON DELETE CASCADE
);

-- ==============================
-- SKILLS (SAFE CASCADE)
-- ==============================
CREATE TABLE skills (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    resume_id BIGINT NOT NULL,
    skill_name VARCHAR(100),
    proficiency_level VARCHAR(50),
    FOREIGN KEY (resume_id) REFERENCES resumes(id) ON DELETE CASCADE
);

-- ==============================
-- CERTIFICATIONS (SAFE CASCADE)
-- ==============================
CREATE TABLE certifications (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    resume_id BIGINT NOT NULL,
    name VARCHAR(150),
    issued_by VARCHAR(200),
    issue_date DATE,
    FOREIGN KEY (resume_id) REFERENCES resumes(id) ON DELETE CASCADE
);
CREATE TABLE projects (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    resume_id BIGINT NOT NULL,
    name VARCHAR(200) NOT NULL,
    technologies_used VARCHAR(300),
    description TEXT,
    FOREIGN KEY (resume_id) REFERENCES resumes(id) ON DELETE CASCADE
);

-- ==============================
-- RESUME FILES (OPTIONAL CASCADE)
-- ==============================
CREATE TABLE resume_files (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    job_seeker_id BIGINT NOT NULL,
    file_name VARCHAR(200),
    file_path VARCHAR(300),
    file_size BIGINT,
    uploaded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (job_seeker_id) REFERENCES job_seekers(id) ON DELETE CASCADE
);

-- ==============================
-- JOBS (NO CASCADE – SOFT DELETE)
-- ==============================
CREATE TABLE jobs (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    employer_id BIGINT NOT NULL,
    title VARCHAR(200),
    description TEXT,
    company_name varchar(25),
    skills_required TEXT,
    location VARCHAR(150),
    salary_min DECIMAL(10,2),
    salary_max DECIMAL(10,2),
    experience_required INT,
    education_required VARCHAR(150),
    job_type VARCHAR(50),
    deadline DATE,
    openings INT,
    status VARCHAR(50),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (employer_id) REFERENCES employers(id)
);

-- ==============================
-- APPLICATIONS (SAFE CASCADE)
-- ==============================

CREATE TABLE applications (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    job_id BIGINT NOT NULL,
    job_seeker_id BIGINT NOT NULL,
    resume_id BIGINT NOT NULL,
    resume_file_id BIGINT NOT NULL,
    cover_letter TEXT,
    status VARCHAR(50),
    applied_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE (job_id, job_seeker_id),
    FOREIGN KEY (job_id) REFERENCES jobs(id) ON DELETE CASCADE,
    FOREIGN KEY (job_seeker_id) REFERENCES job_seekers(id),
    FOREIGN KEY (resume_id) REFERENCES resumes(id),
    FOREIGN KEY (resume_file_id) REFERENCES resume_files(id)
);

-- ==============================
-- SAVED JOBS (NO CASCADE)
-- ==============================
CREATE TABLE saved_jobs (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    job_seeker_id BIGINT NOT NULL,
    job_id BIGINT NOT NULL,
    saved_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (job_seeker_id, job_id),
    FOREIGN KEY (job_seeker_id) REFERENCES job_seekers(id),
    FOREIGN KEY (job_id) REFERENCES jobs(id)
);

-- ==============================
-- NOTIFICATIONS (NO CASCADE)
-- ==============================
CREATE TABLE notifications (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    title VARCHAR(200),
    message TEXT,
    type VARCHAR(50),
    is_read BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);



-- ==============================
-- PERFORMANCE INDEXES
-- ==============================
CREATE INDEX idx_jobs_location ON jobs(location);
CREATE INDEX idx_jobs_experience ON jobs(experience_required);
CREATE INDEX idx_applications_status ON applications(status);
CREATE INDEX idx_applications_job ON applications(job_id);
CREATE INDEX idx_applications_jobseeker ON applications(job_seeker_id);

SELECT * FROM users;

SELECT * FROM job_seekers;

SELECT * FROM employers;

SELECT * FROM resumes;

SELECT * FROM education;

SELECT * FROM experience;

SELECT * FROM skills;

SELECT * FROM certifications;

SELECT * FROM projects;

SELECT * FROM resume_files;

SELECT * FROM jobs;

SELECT * FROM applications;

SELECT * FROM saved_jobs;

SELECT * FROM notifications;
