CREATE TABLE recipe_production (
    id SERIAL PRIMARY KEY,
    recipe_id BIGINT NOT NULL REFERENCES recipes(id),
    quantity_produced DOUBLE PRECISION,
    unit VARCHAR(50),
    production_date TIMESTAMP,
    user_id BIGINT,
    expected_quantity DOUBLE PRECISION,
    yield DOUBLE PRECISION,
    cost DOUBLE PRECISION,
    notes VARCHAR(255)
);

