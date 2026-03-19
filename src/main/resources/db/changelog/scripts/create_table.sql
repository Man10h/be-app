--liquibase formatted sql
--changeset manh:1

-- ======================
-- ROLE
-- ======================
CREATE TABLE role (
                      id BIGINT PRIMARY KEY,
                      name VARCHAR(255)
);

-- ======================
-- USER
-- ======================
CREATE TABLE user (
                      id CHAR(36) PRIMARY KEY,
                      username VARCHAR(255),
                      password VARCHAR(255),
                      email VARCHAR(255),
                      first_name VARCHAR(255),
                      last_name VARCHAR(255),
                      gender VARCHAR(50),
                      enabled BOOLEAN,
                      role_id BIGINT,
                      CONSTRAINT fk_user_role FOREIGN KEY (role_id) REFERENCES role(id)
);

-- ======================
-- POST
-- ======================
CREATE TABLE post (
                      id CHAR(36) PRIMARY KEY,
                      title VARCHAR(255),
                      content TEXT,
                      like_count BIGINT,
                      create_date DATETIME,
                      user_id CHAR(36),
                      CONSTRAINT fk_post_user FOREIGN KEY (user_id) REFERENCES user(id)
);

-- ======================
-- IMAGE
-- ======================
CREATE TABLE image (
                       id CHAR(36) PRIMARY KEY,
                       url VARCHAR(500),
                       user_id CHAR(36),
                       post_id CHAR(36),
                       CONSTRAINT fk_image_user FOREIGN KEY (user_id) REFERENCES user(id),
                       CONSTRAINT fk_image_post FOREIGN KEY (post_id) REFERENCES post(id)
);

-- ======================
-- COMMENT
-- ======================
CREATE TABLE comment (
                         id CHAR(36) PRIMARY KEY,
                         user_id CHAR(36),
                         full_name VARCHAR(255),
                         url VARCHAR(500),
                         content TEXT,
                         post_id CHAR(36),
                         CONSTRAINT fk_comment_post FOREIGN KEY (post_id) REFERENCES post(id)
);

-- ======================
-- FOLLOWER
-- ======================
CREATE TABLE follower (
                          id CHAR(36) PRIMARY KEY,
                          follower_id CHAR(36),
                          user_id CHAR(36),
                          CONSTRAINT fk_follower_user FOREIGN KEY (user_id) REFERENCES user(id),
                          CONSTRAINT uk_user_follower UNIQUE (user_id, follower_id)
);

-- ======================
-- POST LIKE (UPDATED)
-- ======================
CREATE TABLE post_like (
                           id CHAR(36) PRIMARY KEY,
                           user_id CHAR(36),
                           post_id CHAR(36),
                           create_at DATETIME,
                           CONSTRAINT fk_postlike_user FOREIGN KEY (user_id) REFERENCES user(id),
                           CONSTRAINT fk_postlike_post FOREIGN KEY (post_id) REFERENCES post(id),
                           CONSTRAINT uk_like_post_user UNIQUE (post_id, user_id)
);

-- ======================
-- REPORT (UPDATED)
-- ======================
CREATE TABLE report (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        title VARCHAR(255),
                        content TEXT,
                        post_id CHAR(36),
                        user_id CHAR(36),
                        CONSTRAINT fk_report_post FOREIGN KEY (post_id) REFERENCES post(id),
                        CONSTRAINT fk_report_user FOREIGN KEY (user_id) REFERENCES user(id),
                        CONSTRAINT uk_report_post_user UNIQUE (post_id, user_id)
);

-- ======================
-- NOTIFICATION (FIXED)
-- ======================
CREATE TABLE notification (
                              id CHAR(36) PRIMARY KEY,
                              receiver_id CHAR(36),
                              sender_id CHAR(36),
                              content TEXT,
                              is_read BOOLEAN DEFAULT FALSE,
                              created_at DATETIME,
                              target_id CHAR(36),
                              CONSTRAINT fk_notification_receiver FOREIGN KEY (receiver_id) REFERENCES user(id),
                              CONSTRAINT fk_notification_sender FOREIGN KEY (sender_id) REFERENCES user(id)
);