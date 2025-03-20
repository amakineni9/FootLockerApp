DROP TABLE IF EXISTS sales;
DROP TABLE IF EXISTS invoices;
DROP TABLE IF EXISTS inventory;
DROP TABLE IF EXISTS products;
DROP TABLE IF EXISTS product_types;
DROP TABLE IF EXISTS brands;
DROP TABLE IF EXISTS stores;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
    user_id SERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    is_owner BOOLEAN NOT NULL DEFAULT false
);

CREATE TABLE stores (
    store_id SERIAL PRIMARY KEY,
    city VARCHAR(255) NOT NULL,
    state VARCHAR(2) NOT NULL
);

CREATE TABLE brands (
    brand_id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE product_types (
    product_type_id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE products (
    product_id SERIAL PRIMARY KEY,
    product_type_id INTEGER REFERENCES product_types(product_type_id),
    brand_id INTEGER REFERENCES brands(brand_id),
    price DECIMAL(10,2) NOT NULL
);

CREATE TABLE inventory (
    inventory_id SERIAL PRIMARY KEY,
    store_id INTEGER REFERENCES stores(store_id),
    product_id INTEGER REFERENCES products(product_id),
    quantity INTEGER NOT NULL DEFAULT 0,
    min_threshold INTEGER NOT NULL,
    max_threshold INTEGER NOT NULL,
    low_stock_alert BOOLEAN NOT NULL DEFAULT false
);

CREATE TABLE invoices (
    invoice_number SERIAL PRIMARY KEY,
    store_id INTEGER REFERENCES stores(store_id),
    total_price DECIMAL(10,2) NOT NULL,
    date TIMESTAMP NOT NULL,
    user_id INTEGER REFERENCES users(user_id)
);

CREATE TABLE sales (
    sale_id SERIAL PRIMARY KEY,
    store_id INTEGER REFERENCES stores(store_id),
    unites INTEGER NOT NULL,
    total_price DECIMAL(10,2) NOT NULL,
    invoice_number INTEGER REFERENCES invoices(invoice_number),
    product_id INTEGER REFERENCES products(product_id),
    user_id INTEGER REFERENCES users(user_id)
);
