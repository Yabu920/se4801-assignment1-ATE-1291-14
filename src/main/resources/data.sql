INSERT INTO categories (id, name, description)
VALUES (1, 'Perfumes', 'Perfume and fragrance products');

ALTER TABLE categories ALTER COLUMN id RESTART WITH 2;

INSERT INTO products (id, name, description, price, stock, category_id, created_at)
VALUES
    (1, 'Rose Bloom', 'Floral perfume with rose notes', 59.99, 15, 1, CURRENT_TIMESTAMP),
    (2, 'Ocean Mist', 'Fresh aquatic perfume', 64.99, 12, 1, CURRENT_TIMESTAMP),
    (3, 'Velvet Oud', 'Warm woody oud fragrance', 89.99, 8, 1, CURRENT_TIMESTAMP),
    (4, 'Citrus Amber', 'Bright citrus perfume with amber base', 54.99, 20, 1, CURRENT_TIMESTAMP),
    (5, 'Midnight Musk', 'Deep musk evening fragrance', 79.99, 10, 1, CURRENT_TIMESTAMP);

ALTER TABLE products ALTER COLUMN id RESTART WITH 6;
