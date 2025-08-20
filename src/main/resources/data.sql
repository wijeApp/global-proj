-- This file will be automatically executed by Spring Boot after schema.sql
-- if spring.sql.init.mode=always in application.properties

-- Add sample data for transfers_main table
-- Only insert if the table is empty
INSERT INTO transfers_main (employee_id, rate_id, category_id, txn_type, txn_date, amount, hours_worked, description, reference_no, status, currency, created_date, is_active)
SELECT 1, 1, 1, 'SALARY', CURRENT_TIMESTAMP, 2500.00, NULL, 'Monthly Salary', 'REF-001', 'PROCESSED', 'USD', CURRENT_TIMESTAMP, TRUE
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM transfers_main WHERE reference_no = 'REF-001');

INSERT INTO transfers_main (employee_id, rate_id, category_id, txn_type, txn_date, amount, hours_worked, description, reference_no, status, currency, created_date, is_active)
SELECT 2, 2, 2, 'BONUS', CURRENT_TIMESTAMP, 500.00, NULL, 'Performance Bonus', 'REF-002', 'APPROVED', 'USD', CURRENT_TIMESTAMP, TRUE
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM transfers_main WHERE reference_no = 'REF-002');

INSERT INTO transfers_main (employee_id, rate_id, category_id, txn_type, txn_date, amount, hours_worked, description, reference_no, status, currency, created_date, is_active)
SELECT 3, 1, 3, 'OVERTIME', CURRENT_TIMESTAMP, 150.00, 5.0, 'Weekend Overtime', 'REF-003', 'PENDING', 'USD', CURRENT_TIMESTAMP, TRUE
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM transfers_main WHERE reference_no = 'REF-003');

INSERT INTO transfers_main (employee_id, rate_id, category_id, txn_type, txn_date, amount, hours_worked, description, reference_no, status, currency, created_date, is_active)
SELECT 1, 3, 1, 'ALLOWANCE', CURRENT_TIMESTAMP, 200.00, NULL, 'Transport Allowance', 'REF-004', 'PENDING', 'USD', CURRENT_TIMESTAMP, TRUE
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM transfers_main WHERE reference_no = 'REF-004');

INSERT INTO transfers_main (employee_id, rate_id, category_id, txn_type, txn_date, amount, hours_worked, description, reference_no, status, currency, created_date, is_active)
SELECT 2, 2, 2, 'DEDUCTION', CURRENT_TIMESTAMP, 100.00, NULL, 'Late Arrival Penalty', 'REF-005', 'PROCESSED', 'USD', CURRENT_TIMESTAMP, TRUE
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM transfers_main WHERE reference_no = 'REF-005');

-- Add sample countries data
-- Only insert if the table is empty
INSERT INTO countries (country_name)
SELECT 'United States' FROM dual
WHERE NOT EXISTS (SELECT 1 FROM countries WHERE country_name = 'United States');

INSERT INTO countries (country_name)
SELECT 'United Kingdom' FROM dual
WHERE NOT EXISTS (SELECT 1 FROM countries WHERE country_name = 'United Kingdom');

INSERT INTO countries (country_name)
SELECT 'Canada' FROM dual
WHERE NOT EXISTS (SELECT 1 FROM countries WHERE country_name = 'Canada');

INSERT INTO countries (country_name)
SELECT 'Australia' FROM dual
WHERE NOT EXISTS (SELECT 1 FROM countries WHERE country_name = 'Australia');

INSERT INTO countries (country_name)
SELECT 'Germany' FROM dual
WHERE NOT EXISTS (SELECT 1 FROM countries WHERE country_name = 'Germany');

INSERT INTO countries (country_name)
SELECT 'France' FROM dual
WHERE NOT EXISTS (SELECT 1 FROM countries WHERE country_name = 'France');

INSERT INTO countries (country_name)
SELECT 'India' FROM dual
WHERE NOT EXISTS (SELECT 1 FROM countries WHERE country_name = 'India');

INSERT INTO countries (country_name)
SELECT 'Japan' FROM dual
WHERE NOT EXISTS (SELECT 1 FROM countries WHERE country_name = 'Japan');
