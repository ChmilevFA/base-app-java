CREATE TABLE IF NOT EXISTS articles
(
    id                  UUID        NOT NULL,
    title               VARCHAR     NOT NULL,
    body                VARCHAR     NOT NULL,
    created_date        TIMESTAMP    NOT NULL,
    updated_date        TIMESTAMP    NOT NULL,

    CONSTRAINT articles_pk  PRIMARY KEY (id)
);