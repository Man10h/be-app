--liquibase formatted sql
--changeset manh:2

INSERT INTO user (
    id,
    username,
    password,
    email,
    first_name,
    last_name,
    gender,
    enabled,
    role_id
) VALUES (
             UUID(),
             'admin',
             '$2a$10$7QJ9p7Y7kJpQw1YFJzYy.u0H9YvF0tQ9k2K3F7lX9VYxQwQeK9l6O',
             'admin@gmail.com',
             'Admin',
             'System',
             'MALE',
             true,
             1
);