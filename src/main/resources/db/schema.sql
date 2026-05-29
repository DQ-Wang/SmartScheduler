CREATE TABLE IF NOT EXISTS courses (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    course_code VARCHAR(64) UNIQUE NOT NULL,
    standard_name VARCHAR(255),
    teacher VARCHAR(128),
    location VARCHAR(255),
    created_at DATETIME,
    updated_at DATETIME
);

CREATE TABLE IF NOT EXISTS course_aliases (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    course_id BIGINT,
    alias_name VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS exams (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    exam_code VARCHAR(64) UNIQUE NOT NULL,
    standard_name VARCHAR(255),
    exam_time DATETIME
);

CREATE TABLE IF NOT EXISTS exam_aliases (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    exam_id BIGINT,
    alias_name VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS time_slots (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    relation_type VARCHAR(20),
    relation_code VARCHAR(64),
    day_of_week INT,
    start_time TIME,
    end_time TIME
);

CREATE TABLE IF NOT EXISTS plan_items (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    item_code VARCHAR(64) UNIQUE,
    title VARCHAR(255),
    description TEXT,
    start_time DATETIME,
    end_time DATETIME,
    status VARCHAR(30),
    created_at DATETIME,
    updated_at DATETIME
);

CREATE TABLE IF NOT EXISTS plan_resources (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    plan_item_code VARCHAR(64),
    resource_name VARCHAR(255),
    resource_url TEXT
);

CREATE TABLE IF NOT EXISTS user_profiles (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_code VARCHAR(64) UNIQUE,
    display_name VARCHAR(255),
    created_at DATETIME,
    updated_at DATETIME
);
