-- ──────────────────────────────────────────────────────────────────────
-- AI-Powered Expense Splitter — MySQL Database Schema
-- Student : Muhammad Arham | L1F23BSSE0372
-- GitHub  : https://github.com/marhambhatti
-- Run this file first to set up the database before launching the app.
-- ──────────────────────────────────────────────────────────────────────

CREATE DATABASE IF NOT EXISTS expense_splitter;
USE expense_splitter;

-- Users table: stores all registered users
CREATE TABLE IF NOT EXISTS Users (
    id           INT AUTO_INCREMENT PRIMARY KEY,
    name         VARCHAR(100)        NOT NULL,
    email        VARCHAR(150) UNIQUE NOT NULL,
    password_hash VARCHAR(64)        NOT NULL,  -- SHA-256 hash (64 hex chars)
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Groups table: each expense group (e.g. Roommates, Trip 2024)
CREATE TABLE IF NOT EXISTS Groups (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    description TEXT,
    created_by  INT NOT NULL,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (created_by) REFERENCES Users(id) ON DELETE CASCADE
);

-- GroupMembers: many-to-many between Users and Groups
CREATE TABLE IF NOT EXISTS GroupMembers (
    id        INT AUTO_INCREMENT PRIMARY KEY,
    group_id  INT NOT NULL,
    user_id   INT NOT NULL,
    joined_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (group_id) REFERENCES Groups(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id)  REFERENCES Users(id)  ON DELETE CASCADE,
    UNIQUE KEY unique_member (group_id, user_id)
);

-- Categories: lookup table for expense categories
CREATE TABLE IF NOT EXISTS Categories (
    id   INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL
);

-- Insert default categories
INSERT IGNORE INTO Categories (name) VALUES
('Food'), ('Travel'), ('Utilities'), ('Entertainment'), ('Shopping'), ('Other');

-- Expenses: each shared expense record
CREATE TABLE IF NOT EXISTS Expenses (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    title       VARCHAR(200)   NOT NULL,
    amount      DECIMAL(10,2)  NOT NULL,
    group_id    INT            NOT NULL,
    paid_by     INT            NOT NULL,        -- Which user paid
    category    VARCHAR(50)    DEFAULT 'Other', -- ML-predicted or user-selected
    split_type  ENUM('EQUAL','PERCENTAGE','CUSTOM') NOT NULL,
    date        DATE           NOT NULL,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (group_id) REFERENCES Groups(id)  ON DELETE CASCADE,
    FOREIGN KEY (paid_by)  REFERENCES Users(id)   ON DELETE CASCADE
);

-- Settlements: debt settlement records computed by SettlementEngine
CREATE TABLE IF NOT EXISTS Settlements (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    group_id    INT           NOT NULL,
    from_user   INT           NOT NULL,         -- Who owes money
    to_user     INT           NOT NULL,         -- Who receives money
    amount      DECIMAL(10,2) NOT NULL,
    settled     BOOLEAN       DEFAULT FALSE,    -- Has this been paid?
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (group_id)  REFERENCES Groups(id) ON DELETE CASCADE,
    FOREIGN KEY (from_user) REFERENCES Users(id)  ON DELETE CASCADE,
    FOREIGN KEY (to_user)   REFERENCES Users(id)  ON DELETE CASCADE
);
