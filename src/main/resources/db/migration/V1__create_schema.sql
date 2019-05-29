
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
    source_id BIGINT,
    event_id BIGINT,
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
    person_id BIGINT,
    event_id BIGINT,
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
    location_id BIGINT,
    event_id BIGINT,
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
    event_type_id BIGINT REFERENCES event_type(id)
);

ALTER TABLE source_event
    ADD CONSTRAINT source_event_source_fk FOREIGN KEY (source_id) REFERENCES source(id),
    ADD CONSTRAINT source_event_event_fk FOREIGN KEY (event_id) REFERENCES event(id);

ALTER TABLE person_event
    ADD CONSTRAINT person_event_person_fk FOREIGN KEY (person_id) REFERENCES person(id),
    ADD CONSTRAINT person_event_event_fk FOREIGN KEY (event_id) REFERENCES event(id);

ALTER TABLE location_event
    ADD CONSTRAINT location_event_person_fk FOREIGN KEY (location_id) REFERENCES location(id),
    ADD CONSTRAINT location_event_event_fk FOREIGN KEY (event_id) REFERENCES event(id);
