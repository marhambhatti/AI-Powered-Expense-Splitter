package com.expensesplitter.dao;

import com.expensesplitter.models.User;
import com.expensesplitter.utils.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for User table.
 * Handles all CRUD operations: Create, Read, Update, Delete.
 * DB logic is hidden inside DAO — demonstrates ENCAPSULATION.
 *
 * Methods:
 *   createUser()  — INSERT new user
 *   getUserByEmail() — SELECT by email (used for login)
 *   getUserById()    — SELECT by ID
 *   updateUser()     — UPDATE user details
 *   deleteUser()     — DELETE user record
 *   getAllUsers()     — SELECT all users
 *
 * Student : Muhammad Arham | L1F23BSSE0372
 * Package : com.expensesplitter.dao
 */
public class UserDAO {

    /** Inserts a new user into the Users table */
    public boolean createUser(User user) throws SQLException {
        // TODO: Implement on Day 2
        // SQL: INSERT INTO Users (name, email, password_hash) VALUES (?, ?, ?)
        return false;
    }

    /** Retrieves a user by email address — used during login */
    public User getUserByEmail(String email) throws SQLException {
        // TODO: Implement on Day 2
        // SQL: SELECT * FROM Users WHERE email = ?
        return null;
    }

    /** Retrieves a user by their unique ID */
    public User getUserById(int id) throws SQLException {
        // TODO: Implement on Day 2
        // SQL: SELECT * FROM Users WHERE id = ?
        return null;
    }

    /** Updates user profile information */
    public boolean updateUser(User user) throws SQLException {
        // TODO: Implement on Day 2
        // SQL: UPDATE Users SET name=?, email=? WHERE id=?
        return false;
    }

    /** Deletes a user record by ID */
    public boolean deleteUser(int id) throws SQLException {
        // TODO: Implement on Day 2
        // SQL: DELETE FROM Users WHERE id=?
        return false;
    }

    /** Returns all registered users */
    public List<User> getAllUsers() throws SQLException {
        // TODO: Implement on Day 2
        // SQL: SELECT * FROM Users
        return new ArrayList<>();
    }
}
