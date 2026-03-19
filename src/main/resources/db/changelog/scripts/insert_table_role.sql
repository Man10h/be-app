--liquibase formatted sql
--changeset manh:1

INSERT INTO ROLE(id, name) VALUES (1, 'ADMIN'), (2, 'USER');