CREATE TABLE IF NOT EXISTS administrators (
    id SERIAL PRIMARY KEY,
    full_name VARCHAR(50) NOT NULL,
    nui VARCHAR(15) NOT NULL UNIQUE,
    phone VARCHAR(20),
    birthdate DATE NOT NULL,
    gender VARCHAR(10),
    user_id INTEGER NOT NULL UNIQUE,
    FOREIGN KEY (user_id) REFERENCES users(id)
    );
