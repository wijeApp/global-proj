-- Create Users table for User Management System
-- This script creates the users table with all necessary fields for user management and admin rights

CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    role ENUM('USER', 'ADMIN', 'SUPER_ADMIN', 'MANAGER', 'HR', 'FINANCE') NOT NULL DEFAULT 'USER',
    status ENUM('ACTIVE', 'INACTIVE', 'SUSPENDED', 'PENDING', 'LOCKED') NOT NULL DEFAULT 'ACTIVE',
    phone_number VARCHAR(20),
    profile_image VARCHAR(255),
    last_login DATETIME,
    created_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by VARCHAR(50),
    department VARCHAR(50),
    employee_id BIGINT,
    
    -- Indexes for better performance
    INDEX idx_username (username),
    INDEX idx_email (email),
    INDEX idx_role (role),
    INDEX idx_status (status),
    INDEX idx_department (department),
    INDEX idx_employee_id (employee_id),
    INDEX idx_created_date (created_date),
    
    -- Foreign key constraint (if employees table exists)
    FOREIGN KEY (employee_id) REFERENCES employees(id) ON DELETE SET NULL
);

-- Insert sample admin users
INSERT INTO users (
    username, email, password, first_name, last_name, role, status, 
    department, created_by, created_date
) VALUES 
(
    'admin', 
    'admin@globalven.com', 
    'admin123', -- In production, this should be hashed
    'System', 
    'Administrator', 
    'SUPER_ADMIN', 
    'ACTIVE', 
    'IT',
    'SYSTEM',
    NOW()
),
(
    'hradmin', 
    'hr@globalven.com', 
    'hr123', -- In production, this should be hashed
    'HR', 
    'Manager', 
    'HR', 
    'ACTIVE', 
    'HR',
    'SYSTEM',
    NOW()
),
(
    'financeadmin', 
    'finance@globalven.com', 
    'finance123', -- In production, this should be hashed
    'Finance', 
    'Manager', 
    'FINANCE', 
    'ACTIVE', 
    'Finance',
    'SYSTEM',
    NOW()
),
(
    'testuser', 
    'user@globalven.com', 
    'user123', -- In production, this should be hashed
    'Test', 
    'User', 
    'USER', 
    'ACTIVE', 
    'IT',
    'admin',
    NOW()
);

-- Create a view for active users (optional)
CREATE OR REPLACE VIEW active_users AS
SELECT 
    id, username, email, first_name, last_name, role, department, 
    last_login, created_date, employee_id
FROM users 
WHERE status = 'ACTIVE';

-- Create a view for admin users (optional)
CREATE OR REPLACE VIEW admin_users AS
SELECT 
    id, username, email, first_name, last_name, role, department, 
    last_login, created_date, employee_id
FROM users 
WHERE role IN ('ADMIN', 'SUPER_ADMIN');

COMMIT;



