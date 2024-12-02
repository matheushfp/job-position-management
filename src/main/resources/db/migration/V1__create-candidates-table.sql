CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE candidates (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(255) NOT NULL,
    username VARCHAR(255) NOT NULL UNIQUE,
    description TEXT,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    curriculum TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);