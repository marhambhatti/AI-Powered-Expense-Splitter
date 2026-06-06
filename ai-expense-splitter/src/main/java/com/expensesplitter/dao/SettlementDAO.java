package com.expensesplitter.dao;

import com.expensesplitter.models.Settlement;
import com.expensesplitter.utils.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Settlements table.
 * Handles saving and retrieving settlement records.
 *
 * Methods:
 *   saveSettlement()        — INSERT settlement record
 *   getSettlementsByGroup() — SELECT all settlements for a group
 *   markAsSettled()         — UPDATE settled = true
 *   deleteSettlement()      — DELETE record
 *
 * Student : Muhammad Arham | L1F23BSSE0372
 * Package : com.expensesplitter.dao
 */
public class SettlementDAO {

    /** Saves a new settlement record to the database */
    public boolean saveSettlement(Settlement settlement) throws SQLException {
        // TODO: Implement on Day 8
        // SQL: INSERT INTO Settlements (group_id, from_user, to_user, amount, settled) VALUES (...)
        return false;
    }

    /** Returns all settlement records for a group */
    public List<Settlement> getSettlementsByGroup(int groupId) throws SQLException {
        // TODO: Implement on Day 8
        return new ArrayList<>();
    }

    /** Marks a settlement as paid/cleared */
    public boolean markAsSettled(int settlementId) throws SQLException {
        // TODO: Implement on Day 8
        // SQL: UPDATE Settlements SET settled=true WHERE id=?
        return false;
    }

    /** Deletes a settlement record */
    public boolean deleteSettlement(int id) throws SQLException {
        // TODO: Implement on Day 8
        return false;
    }
}
