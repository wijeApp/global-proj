-- SQL Commands to create globalschema database in MySQL

-- Create the database/schema
CREATE DATABASE IF NOT EXISTS globalschema 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

-- Use the database
USE globalschema;

-- Show that the database was created
SHOW DATABASES LIKE 'globalschema';

-- Grant permissions to the user 'sa' (if needed)
-- GRANT ALL PRIVILEGES ON globalschema.* TO 'sa'@'localhost';
-- FLUSH PRIVILEGES;

-- Optional: Show tables (will be empty initially)
SHOW TABLES;
