INSERT INTO categories (id, name, description)
VALUES (1, 'Electronics', 'Electronic items');

ALTER TABLE categories ALTER COLUMN id RESTART WITH 2;

INSERT INTO products (id, name, description, price, stock, category_id, created_at)
VALUES (1, 'Laptop', 'Lightweight laptop', 1200.00, 10, 1, CURRENT_TIMESTAMP);

ALTER TABLE products ALTER COLUMN id RESTART WITH 2;
