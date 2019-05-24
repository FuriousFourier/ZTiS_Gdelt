
CREATE TABLE events
(
    id INTEGER PRIMARY KEY,
    date DATE,
    count BIGINT,
    articles_number INTEGER
);

CREATE TABLE organizations
(
    id INTEGER PRIMARY KEY,
    name VARCHAR(255)
);

CREATE TABLE sources
(
    id INTEGER PRIMARY KEY,
    base_url VARCHAR(255)
);

CREATE TABLE source_urls
(
    id INTEGER PRIMARY KEY,
    exact_url VARCHAR(1000)
);

CREATE TABLE types
(
    id INTEGER PRIMARY KEY,
    name VARCHAR(255)

);

CREATE TABLE persons
(
    id INTEGER PRIMARY KEY,
    name VARCHAR(255)
);

CREATE TABLE locations
(
    id INTEGER PRIMARY KEY,
    country_code VARCHAR(255),
    country VARCHAR(255),
    city VARCHAR(255),
    latitude FLOAT,
    longitude FLOAT
);

CREATE TABLE cameo_events
(
    id INTEGER PRIMARY KEY
)
