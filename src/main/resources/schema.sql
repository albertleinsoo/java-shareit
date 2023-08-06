DROP TABLE IF EXISTS users, items, requests, bookings, comments CASCADE;

CREATE TABLE IF NOT EXISTS users (
    id integer GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
    name varchar(255) NOT NULL,
    email varchar(50) NOT NULL,
    CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS requests (
    id integer GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
    description varchar(512) NOT NULL,
    created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    requesting_user_id integer REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS items (
    id integer GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
    name varchar(255) NOT NULL,
    description varchar(512) NOT NULL,
    is_available boolean NOT NULL,
    owner_id integer REFERENCES users(id) ON DELETE CASCADE,
    request_id integer REFERENCES requests(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS bookings (
    id integer GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
    start_time TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    end_time TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    item_id integer REFERENCES items(id) ON DELETE CASCADE,
    booker_id integer REFERENCES users(id) ON DELETE CASCADE,
    status varchar(10) NOT NULL
);

CREATE TABLE IF NOT EXISTS comments (
    id integer GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
    text varchar(5000),
    item_id integer REFERENCES items(id) ON DELETE CASCADE,
    author_id integer REFERENCES users(id) ON DELETE CASCADE,
    created TIMESTAMP WITHOUT TIME ZONE
);
