package com.expensesplitter.dao;

import com.expensesplitter.models.Group;
import com.expensesplitter.models.User;
import com.expensesplitter.utils.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Groups and GroupMembers tables.
 * Handles group creation, member management, and group queries.
 *
 * Methods:
 *   createGroup()      — INSERT new group
 *   getGroupById()     — SELECT group by ID with members
 *   getGroupsByUser()  — SELECT all groups a user belongs to
 *   addMember()        — INSERT into GroupMembers
 *   removeMember()     — DELETE from GroupMembers
 *   updateGroup()      — UPDATE group info
 *   deleteGroup()      — DELETE group record
 *
 * Student : Muhammad Arham | L1F23BSSE0372
 * Package : com.expensesplitter.dao
 */
public class GroupDAO {

    /** Creates a new expense group */
    public boolean createGroup(Group group) throws SQLException {
        // TODO: Implement on Day 4
        // SQL: INSERT INTO Groups (name, description, created_by) VALUES (?, ?, ?)
        return false;
    }

    /** Retrieves a group by ID, including all its members */
    public Group getGroupById(int id) throws SQLException {
        // TODO: Implement on Day 4
        // SQL: SELECT * FROM Groups WHERE id=? + JOIN GroupMembers + Users
        return null;
    }

    /** Returns all groups the specified user is a member of */
    public List<Group> getGroupsByUser(int userId) throws SQLException {
        // TODO: Implement on Day 4
        // SQL: SELECT g.* FROM Groups g JOIN GroupMembers gm ON g.id=gm.group_id WHERE gm.user_id=?
        return new ArrayList<>();
    }

    /** Adds a user to a group */
    public boolean addMember(int groupId, int userId) throws SQLException {
        // TODO: Implement on Day 4
        // SQL: INSERT INTO GroupMembers (group_id, user_id) VALUES (?, ?)
        return false;
    }

    /** Removes a user from a group */
    public boolean removeMember(int groupId, int userId) throws SQLException {
        // TODO: Implement on Day 4
        // SQL: DELETE FROM GroupMembers WHERE group_id=? AND user_id=?
        return false;
    }

    /** Updates group name and description */
    public boolean updateGroup(Group group) throws SQLException {
        // TODO: Implement on Day 4
        return false;
    }

    /** Deletes a group and all related records */
    public boolean deleteGroup(int id) throws SQLException {
        // TODO: Implement on Day 4
        return false;
    }
}
