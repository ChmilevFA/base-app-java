CREATE TABLE users
(
    id               UUID        NOT NULL,
    state            VARCHAR(15) NOT NUll,
    created_date     TIMESTAMP   NOT NULL,
    updated_date     TIMESTAMP   NOT NULL,
    username         VARCHAR(20) NOT NULL,
    password         VARCHAR(20) NOT NULL,
    email            VARCHAR(20) NOT NULL,

    CONSTRAINT users_pk PRIMARY KEY (id)
);

CREATE UNIQUE INDEX users_username_unique_index ON users (lower(username)) WHERE state != 'DELETED';
CREATE UNIQUE INDEX users_email_unique_index ON users (lower(email)) WHERE state != 'DELETED';
