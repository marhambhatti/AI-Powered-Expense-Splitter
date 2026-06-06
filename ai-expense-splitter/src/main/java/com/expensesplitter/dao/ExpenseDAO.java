package com.expensesplitter.dao;

import com.expensesplitter.models.Expense;
import com.expensesplitter.utils.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Data Access Object for Expenses table.
 * Handles all expense CRUD and search operations.
 *
 * Methods:
 *   addExpense()         — INSERT new expense
 *   getExpenseById()     — SELECT by ID
 *   getExpensesByGroup() — SELECT all for a group
 *   searchExpenses()     — Overloaded: by keyword / date range / both (METHOD OVERLOADING)
 *   updateExpense()      — UPDATE expense record
 *   deleteExpense()      — DELETE expense
 *   getMonthlyTotal()    — SELECT SUM for forecasting
 *
 * Student : Muhammad Arham | L1F23BSSE0372
 * Package : com.expensesplitter.dao
 */
public class ExpenseDAO {

    /** Saves a new expense to the database */
    public boolean addExpense(Expense expense) throws SQLException {
        // TODO: Implement on Day 6
        // SQL: INSERT INTO Expenses (title, amount, group_id, paid_by, category, split_type, date) VALUES (...)
        return false;
    }

    /** Retrieves an expense by its unique ID */
    public Expense getExpenseById(int id) throws SQLException {
        // TODO: Implement on Day 6
        return null;
    }

    /** Returns all expenses belonging to a specific group */
    public List<Expense> getExpensesByGroup(int groupId) throws SQLException {
        // TODO: Implement on Day 6
        return new ArrayList<>();
    }

    /**
     * METHOD OVERLOADING — search by keyword only
     * @param keyword  title search term
     */
    public List<Expense> searchExpenses(String keyword) throws SQLException {
        // TODO: Implement on Day 7
        // SQL: SELECT * FROM Expenses WHERE title LIKE ?
        return new ArrayList<>();
    }

    /**
     * METHOD OVERLOADING — search by date range only
     * @param from  start date
     * @param to    end date
     */
    public List<Expense> searchExpenses(Date from, Date to) throws SQLException {
        // TODO: Implement on Day 7
        // SQL: SELECT * FROM Expenses WHERE date BETWEEN ? AND ?
        return new ArrayList<>();
    }

    /**
     * METHOD OVERLOADING — search by keyword AND date range
     * @param keyword  title search term
     * @param from     start date
     * @param to       end date
     */
    public List<Expense> searchExpenses(String keyword, Date from, Date to) throws SQLException {
        // TODO: Implement on Day 7
        // SQL: SELECT * FROM Expenses WHERE title LIKE ? AND date BETWEEN ? AND ?
        return new ArrayList<>();
    }

    /** Updates an existing expense record */
    public boolean updateExpense(Expense expense) throws SQLException {
        // TODO: Implement on Day 6
        return false;
    }

    /** Deletes an expense by ID */
    public boolean deleteExpense(int id) throws SQLException {
        // TODO: Implement on Day 6
        return false;
    }

    /**
     * Returns total spending for a user in a given month.
     * Used by SpendingForecaster for Linear Regression training.
     *
     * @param userId  the user's ID
     * @param month   month number (1-12)
     * @param year    the year
     * @return total amount spent
     */
    public double getMonthlyTotal(int userId, int month, int year) throws SQLException {
        // TODO: Implement on Day 10
        return 0.0;
    }
}
