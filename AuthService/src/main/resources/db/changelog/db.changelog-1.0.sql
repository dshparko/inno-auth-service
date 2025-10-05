--liquibase formatted sql

--changeset dshparko:1
CREATE TABLE IF NOT EXISTS roles
(
    id        BIGSERIAL PRIMARY KEY,
    role_name VARCHAR(32) UNIQUE
);

--changeset dshparko:2
CREATE TABLE IF NOT EXISTS users
(
    id            BIGSERIAL PRIMARY KEY,
    email         VARCHAR(128)          NOT NULL,
    password_hash VARCHAR(64)           NOT NULL,
    role_id       BIGINT                NOT NULL,
    created_at    DATE    DEFAULT now() NOT NULL,
    is_active     BOOLEAN DEFAULT true,
    CONSTRAINT fk_users_role FOREIGN KEY (role_id) REFERENCES roles (id),
    CONSTRAINT uq_users_email UNIQUE (email)

);