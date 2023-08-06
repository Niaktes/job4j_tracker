CREATE TABLE IF NOT EXISTS j_user_notification (
    id          serial  PRIMARY KEY,
    messenger   text,
    identify    text,
    j_user_id   int     REFERENCES j_user(id)
);