CREATE TABLE recipes (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    recipe_name VARCHAR(255) NOT NULL
);

CREATE TABLE recipe_ingredients (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    recipe_id BIGINT NOT NULL REFERENCES recipes (id) ON DELETE CASCADE,
    order_index INTEGER,
    quantity_numerator INTEGER NOT NULL,
    quantity_denominator INTEGER NOT NULL,
    unit VARCHAR(255) NOT NULL,
    ingredient VARCHAR(255) NOT NULL
);

CREATE INDEX idx_recipe_ingredients_recipe_id ON recipe_ingredients (recipe_id);

CREATE TABLE recipe_instructions (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    recipe_id BIGINT NOT NULL REFERENCES recipes (id) ON DELETE CASCADE,
    order_index INTEGER,
    instruction_text VARCHAR(2000) NOT NULL
);

CREATE INDEX idx_recipe_instructions_recipe_id ON recipe_instructions (recipe_id);
