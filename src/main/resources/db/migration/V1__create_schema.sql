
CREATE TABLE organizations
(
    id INTEGER PRIMARY KEY,
    name VARCHAR(255)
);

CREATE TABLE types
(
    id INTEGER PRIMARY KEY,
    name VARCHAR(255)

);

CREATE TABLE sources
(
    id INTEGER PRIMARY KEY,
    base_url VARCHAR(255)
);

CREATE TABLE source_event
(
    id INTEGER PRIMARY KEY,
    source_id INTEGER REFERENCES sources(id),
    event_id INTEGER REFERENCES events(id)
);

CREATE TABLE source_urls
(
    id INTEGER PRIMARY KEY,
    source_id INTEGER REFERENCES sources(id),
    exact_url VARCHAR(1000)
);

CREATE TABLE persons
(
    id INTEGER PRIMARY KEY,
    name VARCHAR(255)
);

CREATE TABLE person_event
(
    id INTEGER PRIMARY KEY,
    person_id INTEGER REFERENCES persons(id),
    event_id INTEGER REFERENCES events(id)
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

CREATE TABLE location_event
(
    id INTEGER PRIMARY KEY,
    location_id INTEGER REFERENCES locations(id),
    event_id INTEGER REFERENCES events(id)
);

CREATE TABLE cameo_events
(
    id INTEGER PRIMARY KEY
);

CREATE TABLE events
(
    id INTEGER PRIMARY KEY,
    date DATE,
    count BIGINT,
    articles_number INTEGER,

    -- many-to-one
    organization_id INTEGER REFERENCES organizations(id),
    type_id INTEGER REFERENCES types(id)

    /*
    many-to-many:
    - source_event
    - person_event
    - location_event
     */
);
