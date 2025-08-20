-- Insert 5 sample employees into the employees table
USE globalschema;

INSERT INTO employees (first_name, last_name, email, department, salary, hire_date, join_date, position, phone_number, employee_image, rate_category, country)
VALUES
('John', 'Doe', 'john.doe@company.com', 'IT', 75000, '2023-01-15', '2023-02-01', 'Developer', '555-1234', 'photo1.jpg', 'Senior', 'US'),
('Jane', 'Smith', 'jane.smith@company.com', 'HR', 65000, '2023-03-01', '2023-03-15', 'HR Manager', '555-5678', 'photo2.jpg', 'Manager', 'UK'),
('Mike', 'Johnson', 'mike.johnson@company.com', 'Sales', 55000, '2023-04-10', '2023-04-20', 'Sales Executive', '555-8765', 'photo3.jpg', 'Junior', 'IN'),
('Sarah', 'Williams', 'sarah.williams@company.com', 'Marketing', 70000, '2023-05-05', '2023-05-10', 'Marketing Lead', '555-4321', 'photo4.jpg', 'Senior', 'CA'),
('David', 'Brown', 'david.brown@company.com', 'Finance', 80000, '2023-06-01', '2023-06-15', 'Finance Analyst', '555-2468', 'photo5.jpg', 'Manager', 'AU');
