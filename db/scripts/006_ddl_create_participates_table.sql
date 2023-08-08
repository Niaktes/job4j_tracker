CREATE TABLE IF NOT EXISTS participates (
    id      serial  PRIMARY KEY,
    item_id int     NOT NULL REFERENCES items(id),
    user_id int     NOT NULL REFERENCES j_user(id),
    UNIQUE (item_id, user_id)
);