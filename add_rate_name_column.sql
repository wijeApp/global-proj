-- Migration script to add rate_name column to rates table
-- Run this script if you have an existing rates table

USE globalschema;

-- Add the rate_name column to the rates table
ALTER TABLE rates 
ADD COLUMN rate_name VARCHAR(255) AFTER emp_category;

-- Optional: Update existing records with default rate names
UPDATE rates 
SET rate_name = CONCAT(emp_category, ' Rate') 
WHERE rate_name IS NULL;

-- Show the updated table structure
DESCRIBE rates;
