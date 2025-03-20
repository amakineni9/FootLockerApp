-- Sample Users (passwords are 'password123' for all users)
INSERT INTO users (email, password, first_name, last_name, is_owner) VALUES
('john.doe@example.com', 'password123', 'John', 'Doe', true),
('jane.smith@example.com', 'password123', 'Jane', 'Smith', false),
('mike.wilson@example.com', 'password123', 'Mike', 'Wilson', false);

-- Sample Brands
INSERT INTO brands (name) VALUES
('Nike'),
('Adidas'),
('New Balance'),
('Puma'),
('Under Armour');

-- Sample Product Types
INSERT INTO product_types (name) VALUES
('Running'),
('Basketball'),
('Casual'),
('Training'),
('Soccer');

-- Sample Products
INSERT INTO products (product_type_id, brand_id, price) VALUES
(1, 1, 129.99),  -- Nike Running
(2, 1, 159.99),  -- Nike Basketball
(1, 2, 119.99),  -- Adidas Running
(3, 2, 89.99),   -- Adidas Casual
(4, 3, 99.99),   -- New Balance Training
(1, 3, 109.99),  -- New Balance Running
(5, 4, 79.99),   -- Puma Soccer
(4, 5, 89.99);   -- Under Armour Training

-- Sample Stores
INSERT INTO stores (city, state) VALUES
('New York', 'NY'),
('Los Angeles', 'CA'),
('Chicago', 'IL'),
('Houston', 'TX'),
('Miami', 'FL');

-- Sample Inventory (adding inventory for each product in each store)
INSERT INTO inventory (store_id, product_id, quantity, min_threshold, max_threshold, low_stock_alert) VALUES
-- New York store
(1, 1, 25, 10, 50, false),  -- Nike Running
(1, 2, 8, 10, 30, true),   -- Nike Basketball (low stock)
(1, 3, 15, 10, 40, false), -- Adidas Running
(1, 4, 30, 15, 60, false), -- Adidas Casual

-- Los Angeles store
(2, 1, 30, 15, 60, false), -- Nike Running
(2, 2, 20, 10, 40, false), -- Nike Basketball
(2, 5, 5, 10, 30, true),   -- New Balance Training (low stock)
(2, 6, 25, 10, 50, false), -- New Balance Running

-- Chicago store
(3, 3, 20, 10, 40, false), -- Adidas Running
(3, 4, 7, 15, 45, true),   -- Adidas Casual (low stock)
(3, 7, 18, 10, 35, false), -- Puma Soccer
(3, 8, 22, 12, 40, false), -- Under Armour Training

-- Houston store
(4, 5, 28, 15, 50, false), -- New Balance Training
(4, 6, 12, 15, 45, true),  -- New Balance Running (low stock)
(4, 7, 35, 20, 60, false), -- Puma Soccer
(4, 8, 16, 10, 30, false), -- Under Armour Training

-- Miami store
(5, 1, 40, 20, 80, false), -- Nike Running
(5, 2, 25, 15, 45, false), -- Nike Basketball
(5, 7, 8, 15, 40, true),   -- Puma Soccer (low stock)
(5, 8, 30, 20, 50, false); -- Under Armour Training

-- Sample Invoices (for the past week)
INSERT INTO invoices (store_id, total_price, date, user_id) VALUES
(1, 289.98, now() - interval '7 days', 1),
(2, 159.99, now() - interval '6 days', 2),
(3, 199.98, now() - interval '5 days', 3),
(4, 269.97, now() - interval '4 days', 2),
(5, 179.98, now() - interval '3 days', 1);

-- Sample Sales
INSERT INTO sales (store_id, unites, total_price, invoice_number, product_id, user_id) VALUES
(1, 2, 289.98, 1, 1, 1),  -- 2 Nike Running shoes
(2, 1, 159.99, 2, 2, 2),  -- 1 Nike Basketball shoe
(3, 2, 199.98, 3, 3, 3),  -- 2 Adidas Running shoes
(4, 3, 269.97, 4, 4, 2),  -- 3 Adidas Casual shoes
(5, 2, 179.98, 5, 5, 1);  -- 2 New Balance Training shoes
