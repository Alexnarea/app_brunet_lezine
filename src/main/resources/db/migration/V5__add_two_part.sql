CREATE TABLE responses (
    id SERIAL PRIMARY KEY,
    evaluation_id INTEGER NOT NULL,
    item_id INTEGER NOT NULL,
    passed BOOLEAN NOT NULL, -- TRUE = aprobado (1.5 meses), FALSE = no aprobado (0)
    FOREIGN KEY (evaluation_id) REFERENCES evaluations(id) ON DELETE CASCADE,
    FOREIGN KEY (item_id) REFERENCES test_items(id) ON DELETE CASCADE,
    UNIQUE (evaluation_id, item_id) -- evitar respuestas duplicadas
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

CREATE TABLE recommendations (
    id SERIAL PRIMARY KEY,
    result_id INTEGER NOT NULL,
    activity TEXT NOT NULL,
    frequency VARCHAR(50),     -- por ejemplo: "3 veces por semana"
    FOREIGN KEY (result_id) REFERENCES global_results(id) ON DELETE CASCADE
);

CREATE TABLE reports (
    id SERIAL PRIMARY KEY,
    evaluation_id INTEGER NOT NULL,
    file_path VARCHAR(255) NOT NULL, -- Ruta del archivo PDF generado
    generated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (evaluation_id) REFERENCES evaluations(id) ON DELETE CASCADE
);
