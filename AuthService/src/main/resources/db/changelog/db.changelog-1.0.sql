--liquibase formatted sql

--changeset dshparko:1
CREATE TABLE IF NOT EXISTS roles
(
    id        BIGSERIAL PRIMARY KEY,
    role_name VARCHAR(32) UNIQUE NOT NULL
);


--changeset dshparko:2
CREATE TABLE IF NOT EXISTS users
(
    id         BIGSERIAL PRIMARY KEY,
    role_id    BIGINT                NOT NULL,
    created_at DATE    DEFAULT now() NOT NULL,
    is_active  BOOLEAN DEFAULT true,
    CONSTRAINT fk_users_role FOREIGN KEY (role_id) REFERENCES roles (id)
);

--changeset dshparko:3
CREATE TABLE credentials
(
    id            BIGSERIAL PRIMARY KEY,
    email         VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    salt          VARCHAR(255) NOT NULL,
    user_id       BIGINT       NOT NULL,
    CONSTRAINT fk_credentials_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);
