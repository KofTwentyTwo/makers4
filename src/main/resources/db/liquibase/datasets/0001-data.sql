-- liquibase formatted sql

-- changeset template:orders-data-1 context:test,dev
INSERT INTO customer (name, email, createdate, modifydate) VALUES
 ('Alice Smith', 'alice@example.com', NOW(), NOW()),
 ('Bob Lee', 'bob@example.com', NOW(), NOW());

-- changeset template:orders-data-2 context:test,dev
INSERT INTO product (name, sku, price, createdate, modifydate) VALUES
 ('Widget', 'W-001', 19.99, NOW(), NOW()),
 ('Gadget', 'G-002', 49.50, NOW(), NOW());

-- changeset template:orders-data-3 context:test,dev
INSERT INTO orders (customer_id, status, order_total, createdate, modifydate) VALUES
 (1, 'NEW', 69.49, NOW(), NOW());

-- changeset template:orders-data-4 context:test,dev
INSERT INTO order_line (order_id, product_id, quantity, unit_price, line_total, notes) VALUES
 (1, 1, 1, 19.99, 19.99, 'Single widget'),
 (1, 2, 1, 49.50, 49.50, 'Single gadget');

