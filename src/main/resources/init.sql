CREATE DATABASE IF NOT EXISTS member_management;
USE member_management;

CREATE TABLE members (
    id INT AUTO_INCREMENT PRIMARY KEY,
    member_id CHAR(6) UNIQUE NOT NULL,
    name VARCHAR(50) NOT NULL,
    phone VARCHAR(15) NOT NULL,
    type ENUM('金', '银') NOT NULL,
    consumption DECIMAL(10, 2) NOT NULL DEFAULT 0,
    points INT NOT NULL DEFAULT 0
);
