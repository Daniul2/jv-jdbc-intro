-- This ensures you are in the correct database
USE bookstore;

-- Drop the table if it already exists to start fresh
DROP TABLE IF EXISTS books;

-- Create the table
CREATE TABLE books (
                       id BIGINT NOT NULL AUTO_INCREMENT,
                       title VARCHAR(255) NOT NULL,
                       price DECIMAL(10, 2) NOT NULL,
                       PRIMARY KEY (id)
);
