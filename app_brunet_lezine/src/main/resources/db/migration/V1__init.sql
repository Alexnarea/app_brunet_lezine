CREATE TABLE IF NOT EXISTS users
(
    id       SERIAL PRIMARY KEY,
    username VARCHAR(20)  NOT NULL,
    password VARCHAR(200) NOT NULL,
    email    VARCHAR(50)  NOT NULL,
    locked   BOOLEAN      NOT NULL DEFAULT FALSE,
    disabled BOOLEAN      NOT NULL DEFAULT FALSE,
    UNIQUE (username),
    UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS children
(
    id SERIAL PRIMARY KEY ,
    full_name VARCHAR(100) NOT NULL ,
    nui VARCHAR(50) NOT NULL UNIQUE ,
    birthdate DATE NOT NULL ,
    gender VARCHAR(50),
    creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE  IF NOT EXISTS evaluators
(
    id SERIAL PRIMARY KEY,
    speciality VARCHAR(100),
    user_id INTEGER NOT NULL UNIQUE,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS domains
(
    id SERIAL PRIMARY KEY,
    description_domain VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS test_items
(
    id SERIAL PRIMARY KEY,
    domain_id INTEGER NOT NULL,
    description TEXT NOT NULL,
    reference_age_months INTEGER NOT NULL,
    item_order INTEGER NOT NULL,
    FOREIGN KEY (domain_id) REFERENCES domains(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS evaluations
(
    id                       SERIAL PRIMARY KEY,
    application_date         TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    chronological_age_months INTEGER NOT NULL,
    children_id              INTEGER NOT NULL,
    evaluator_id             INTEGER NOT NULL,
    FOREIGN KEY (children_id) REFERENCES children (id) ON DELETE CASCADE,
    FOREIGN KEY (evaluator_id) REFERENCES evaluators (id) ON DELETE CASCADE
);