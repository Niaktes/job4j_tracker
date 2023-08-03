CREATE TABLE IF NOT EXISTS j_user (
    id      serial          PRIMARY KEY,
    name    varchar(2000),
    role_id int             NOT NULL    REFERENCES j_role(id)
);