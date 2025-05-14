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
    nui VARCHAR(50) NOT NULL ,
    birthdate DATE NOT NULL ,
    gender VARCHAR(50),
    creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE  IF NOT EXISTS evaluators
(
    id SERIAL PRIMARY KEY,
    speciality VARCHAR(100),
    user_id INTEGER NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS domains
(
    id SERIAL PRIMARY KEY,
    description_domain VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS test_items
(
    id SERIAL PRIMARY KEY,
    domain_id INTEGER NOT NULL,
    description TEXT NOT NULL,
    reference_age_months INTEGER NOT NULL,
    item_order INTEGER NOT NULL,
    FOREIGN KEY (domain_id) REFERENCES domains(id)
);

CREATE TABLE IF NOT EXISTS evaluations
(
    id SERIAL PRIMARY KEY,
    application_date TIMESTAMP,
    chronological_age_months INTEGER NOT NULL,
    children_id INTEGER NOT NULL ,
    evaluator_id INTEGER NOT NULL ,
    FOREIGN KEY (children_id) REFERENCES children (id),
    FOREIGN KEY (evaluator_id) REFERENCES evaluators (id)
);

/*CREATE TABLE responses (
     id SERIAL PRIMARY KEY,
     evaluation_id INTEGER NOT NULL,
     item_id INTEGER NOT NULL,
     passed BOOLEAN NOT NULL, -- TRUE=✅ (1.5 meses), FALSE=⬜ (0)
     FOREIGN KEY (evaluation_id) REFERENCES evaluations(id) ON DELETE CASCADE,
     FOREIGN KEY (item_id) REFERENCES test_items(id) ON DELETE CASCADE,
     UNIQUE (evaluation_id, item_id) -- Evitar duplicados
);

CREATE TABLE global_results (
     id SERIAL PRIMARY KEY,
     evaluation_id INTEGER NOT NULL UNIQUE,
     total_months_approved DECIMAL(5,1) NOT NULL,
     coefficient DECIMAL(5,2) NOT NULL,
     result_years TEXT NOT NULL,
     result_detail TEXT NOT NULL,
     classification VARCHAR(50) NOT NULL,
     FOREIGN KEY (evaluation_id) REFERENCES evaluations(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS parents_children
(
    id SERIAL PRIMARY KEY,
    relationship VARCHAR(100) NOT NULL ,
    children_id INTEGER NOT NULL ,
    user_id INTEGER NOT NULL ,
    FOREIGN KEY (children_id) REFERENCES children (id),
    FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE recommendations (
    id SERIAL PRIMARY KEY,
    result_id INTEGER NOT NULL,
    activity TEXT NOT NULL,
    frequency VARCHAR(50),     -- "3 veces por semana"
    FOREIGN KEY (result_id) REFERENCES global_results(id) ON DELETE CASCADE
);

CREATE TABLE reports (
    id SERIAL PRIMARY KEY,
    evaluation_id INTEGER NOT NULL,
    file_path VARCHAR(255) NOT NULL, -- Ruta del PDF
    generated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (evaluation_id) REFERENCES evaluations(id) ON DELETE CASCADE
);*/