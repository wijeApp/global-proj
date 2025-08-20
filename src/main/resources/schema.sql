-- This file will be automatically executed by Spring Boot when the application starts
-- if spring.sql.init.mode=always in application.properties

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

-- Countries table
CREATE TABLE IF NOT EXISTS countries (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    country_name VARCHAR(100) NOT NULL UNIQUE,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
