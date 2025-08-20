-- Use the globalschema database
USE globalschema;

-- Create the transfers_main table if it doesn't exist
CREATE TABLE IF NOT EXISTS transfers_main (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    employee_id BIGINT NOT NULL,
    rate_id BIGINT NOT NULL,
    category_id BIGINT NOT NULL,
    txn_type VARCHAR(50) NOT NULL,
    txn_date TIMESTAMP NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    hours_worked DOUBLE,
    description VARCHAR(255),
    reference_no VARCHAR(100),
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    currency VARCHAR(10) NOT NULL DEFAULT 'USD',
    exchange_rate DECIMAL(10, 4),
    amount_base_currency DECIMAL(10, 2),
    created_by VARCHAR(100),
    created_date TIMESTAMP NOT NULL,
    updated_by VARCHAR(100),
    updated_date TIMESTAMP,
    approved_by VARCHAR(100),
    approved_date TIMESTAMP,
    notes TEXT,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    FOREIGN KEY (employee_id) REFERENCES employee(id),
    FOREIGN KEY (rate_id) REFERENCES rates(rate_id),
    FOREIGN KEY (category_id) REFERENCES category(cat_id)
);

-- Check if the table is empty before inserting sample data
SET @count = (SELECT COUNT(*) FROM transfers_main);

-- Only insert sample data if the table is empty
INSERT INTO transfers_main (employee_id, rate_id, category_id, txn_type, txn_date, amount, hours_worked, description, reference_no, status, currency, created_date, is_active)
SELECT * FROM (
    SELECT 1, 1, 1, 'SALARY', CURRENT_TIMESTAMP, 2500.00, NULL, 'Monthly Salary', 'REF-001', 'PROCESSED', 'USD', CURRENT_TIMESTAMP, TRUE
) AS tmp
WHERE @count = 0;

INSERT INTO transfers_main (employee_id, rate_id, category_id, txn_type, txn_date, amount, hours_worked, description, reference_no, status, currency, created_date, is_active)
SELECT * FROM (
    SELECT 2, 2, 2, 'BONUS', CURRENT_TIMESTAMP, 500.00, NULL, 'Performance Bonus', 'REF-002', 'APPROVED', 'USD', CURRENT_TIMESTAMP, TRUE
) AS tmp
WHERE @count = 0;

INSERT INTO transfers_main (employee_id, rate_id, category_id, txn_type, txn_date, amount, hours_worked, description, reference_no, status, currency, created_date, is_active)
SELECT * FROM (
    SELECT 3, 1, 3, 'OVERTIME', CURRENT_TIMESTAMP, 150.00, 5.0, 'Weekend Overtime', 'REF-003', 'PENDING', 'USD', CURRENT_TIMESTAMP, TRUE
) AS tmp
WHERE @count = 0;

INSERT INTO transfers_main (employee_id, rate_id, category_id, txn_type, txn_date, amount, hours_worked, description, reference_no, status, currency, created_date, is_active)
SELECT * FROM (
    SELECT 1, 3, 1, 'ALLOWANCE', CURRENT_TIMESTAMP, 200.00, NULL, 'Transport Allowance', 'REF-004', 'PENDING', 'USD', CURRENT_TIMESTAMP, TRUE
) AS tmp
WHERE @count = 0;

INSERT INTO transfers_main (employee_id, rate_id, category_id, txn_type, txn_date, amount, hours_worked, description, reference_no, status, currency, created_date, is_active)
SELECT * FROM (
    SELECT 2, 2, 2, 'DEDUCTION', CURRENT_TIMESTAMP, 100.00, NULL, 'Late Arrival Penalty', 'REF-005', 'PROCESSED', 'USD', CURRENT_TIMESTAMP, TRUE
) AS tmp
WHERE @count = 0;

-- Verify the data was inserted
SELECT * FROM transfers_main;
