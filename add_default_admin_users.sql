-- Add default admin users with BCrypt encoded passwords
-- These passwords are: admin123, hr123, finance123 respectively

INSERT INTO users (username, email, password, first_name, last_name, role, status, department, created_by, created_date)
VALUES 
-- Super Admin: admin / admin123
('admin', 'admin@globalven.com', '$2a$10$EixZaYVK1fsbw1ZfbX3OXe.pZyWJaWgYrEp4Kqkhx.1eKqKrEeQem', 
 'System', 'Administrator', 'SUPER_ADMIN', 'ACTIVE', 'IT', 'SYSTEM', NOW()),

-- HR Admin: hradmin / hr123  
('hradmin', 'hr@globalven.com', '$2a$10$dXJ3SW6G7P50lGm5MkYzPeJxND2C2cgGrM5rKjHJKPTfO5RtFVjvu', 
 'HR', 'Manager', 'HR', 'ACTIVE', 'HR', 'SYSTEM', NOW()),

-- Finance Admin: financeadmin / finance123
('financeadmin', 'finance@globalven.com', '$2a$10$RMuFXGQ5AtH4wqyHqz8m8OokEqXSx0jGmEGlKGggsQh9b7FJQvgxW', 
 'Finance', 'Manager', 'FINANCE', 'ACTIVE', 'Finance', 'SYSTEM', NOW())

ON DUPLICATE KEY UPDATE
password = VALUES(password),
updated_date = NOW();
