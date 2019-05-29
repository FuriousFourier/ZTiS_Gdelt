
CREATE TABLE organization
(
    id BIGINT PRIMARY KEY,
    name VARCHAR(255)
);

CREATE TABLE event_type
(
    id BIGINT PRIMARY KEY,
    name VARCHAR(255)
);

CREATE TABLE source
(
    id BIGINT PRIMARY KEY,
    base_url VARCHAR(255)
);

CREATE TABLE source_event
(
    source_id BIGINT REFERENCES source(id),
    event_id BIGINT REFERENCES event(id),
    PRIMARY KEY(source_id, event_id)
);

CREATE TABLE source_url
(
    id BIGINT PRIMARY KEY,
    source_id BIGINT REFERENCES source(id),
    exact_url VARCHAR(1000)
);

CREATE TABLE person
(
    id BIGINT PRIMARY KEY,
    name VARCHAR(255)
);

CREATE TABLE person_event
(
    person_id BIGINT REFERENCES person(id),
    event_id BIGINT REFERENCES event(id),
    PRIMARY KEY(person_id, event_id)
);

CREATE TABLE location
(
    id BIGINT PRIMARY KEY,
    country_code VARCHAR(255),
    country VARCHAR(255),
    city VARCHAR(255),
    latitude FLOAT,
    longitude FLOAT
);

CREATE TABLE location_event
(
    location_id BIGINT REFERENCES location(id),
    event_id BIGINT REFERENCES event(id),
    PRIMARY KEY(location_id, event_id)
);

CREATE TABLE cameo_event
(
    id BIGINT PRIMARY KEY
);

CREATE TABLE event
(
    id BIGINT PRIMARY KEY,
    date DATE,
    count BIGINT,
    articles_number INTEGER,

    -- many-to-one
    organization_id BIGINT REFERENCES organization(id),
    event_type_id BIGINT REFERENCES event_type(id),

    -- many-to-many
   sources BIGINT REFERENCES source_event(event_id),
   persons BIGINT REFERENCES person_event(event_id),
   locations BIGINT REFERENCES location_event(event_id)
);
