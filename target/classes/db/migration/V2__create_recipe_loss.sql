CREATE TABLE recipe_loss (
    id SERIAL PRIMARY KEY,
    recipe_id BIGINT NOT NULL REFERENCES recipes(id),
    quantity_lost DOUBLE PRECISION,
    unit VARCHAR(50),
    loss_date TIMESTAMP,
    user_id BIGINT,
    notes VARCHAR(255),
    image_url VARCHAR(255)
);

