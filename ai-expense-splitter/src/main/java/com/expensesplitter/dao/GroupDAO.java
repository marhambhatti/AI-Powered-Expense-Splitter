package com.expensesplitter.dao;

import com.expensesplitter.models.Group;
import com.expensesplitter.models.User;
import com.expensesplitter.utils.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) for the Groups and GroupMembers tables.
 * Handles database operations for creating, retrieving, updating, and deleting groups,
 * as well as managing members within groups.
 *
 * Student : Muhammad Arham | L1F23BSSE0372
 * Package : com.expensesplitter.dao
 */
public class GroupDAO {

    /**
     * Creates a new expense group in the Groups table.
     * Uses a SQL transaction to automatically insert the group creator as the first member
     * of the group in the GroupMembers table.
     *
     * @param group the Group object containing name, description, and creator user ID
     * @return true if the group was successfully created and creator enrolled, false otherwise
     * @throws SQLException if a database access error occurs
     */
    public boolean createGroup(Group group) throws SQLException {
        String insertGroupSql = "INSERT INTO Groups (name, description, created_by) VALUES (?, ?, ?)";
        String insertMemberSql = "INSERT INTO GroupMembers (group_id, user_id) VALUES (?, ?)";
        
        Connection conn = null;
        try {
            conn = DatabaseConnection.getInstance();
            // Start transaction
            conn.setAutoCommit(false);
            
            try (PreparedStatement psGroup = conn.prepareStatement(insertGroupSql, Statement.RETURN_GENERATED_KEYS)) {
                psGroup.setString(1, group.getName());
                psGroup.setString(2, group.getDescription());
                psGroup.setInt(3, group.getCreatedBy());
                
                int affectedRows = psGroup.executeUpdate();
                
                if (affectedRows > 0) {
                    // Get the generated group ID
                    try (ResultSet rs = psGroup.getGeneratedKeys()) {
                        if (rs.next()) {
                            group.setId(rs.getInt(1));
                        }
                    }
                    
                    // Automatically add the creator as a group member
                    try (PreparedStatement psMember = conn.prepareStatement(insertMemberSql)) {
                        psMember.setInt(1, group.getId());
                        psMember.setInt(2, group.getCreatedBy());
                        psMember.executeUpdate();
                    }
                    
                    // Commit transaction
                    conn.commit();
                    return true;
                }
            } catch (SQLException e) {
                // Rollback transaction on failure
                if (conn != null) {
                    conn.rollback();
                }
                throw e;
            }
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
            }
        }
        return false;
    }

    /**
     * Retrieves a group from the database by its unique ID.
     * Automatically retrieves and joins all associated group members.
     *
     * @param id the unique primary key ID of the group
     * @return the Group object complete with its member list, or null if not found
     * @throws SQLException if a database access error occurs
     */
    public Group getGroupById(int id) throws SQLException {
        String groupSql = "SELECT id, name, description, created_by FROM Groups WHERE id = ?";
        String membersSql = "SELECT u.id, u.name, u.email, u.password_hash " +
                            "FROM Users u " +
                            "JOIN GroupMembers gm ON u.id = gm.user_id " +
                            "WHERE gm.group_id = ?";
        
        try (Connection conn = DatabaseConnection.getInstance();
             PreparedStatement psGroup = conn.prepareStatement(groupSql)) {
            
            psGroup.setInt(1, id);
            
            try (ResultSet rsGroup = psGroup.executeQuery()) {
                if (rsGroup.next()) {
                    Group group = new Group(
                        rsGroup.getInt("id"),
                        rsGroup.getString("name"),
                        rsGroup.getString("description"),
                        rsGroup.getInt("created_by")
                    );
                    
                    // Load all members of this group
                    try (PreparedStatement psMembers = conn.prepareStatement(membersSql)) {
                        psMembers.setInt(1, id);
                        try (ResultSet rsMembers = psMembers.executeQuery()) {
                            while (rsMembers.next()) {
                                group.addMember(new User(
                                    rsMembers.getInt("id"),
                                    rsMembers.getString("name"),
                                    rsMembers.getString("email"),
                                    rsMembers.getString("password_hash")
                                ));
                            }
                        }
                    }
                    return group;
                }
            }
        }
        return null;
    }

    /**
     * Retrieves all groups that the specified user is a registered member of.
     *
     * @param userId the unique ID of the user
     * @return a List containing Group objects the user belongs to
     * @throws SQLException if a database access error occurs
     */
    public List<Group> getGroupsByUser(int userId) throws SQLException {
        String sql = "SELECT g.id FROM Groups g JOIN GroupMembers gm ON g.id = gm.group_id WHERE gm.user_id = ?";
        List<Group> groups = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getInstance();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, userId);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int groupId = rs.getInt("id");
                    // Load the full Group object including its members list
                    Group group = getGroupById(groupId);
                    if (group != null) {
                        groups.add(group);
                    }
                }
            }
        }
        return groups;
    }

    /**
     * Adds a user to a group by inserting an association record in GroupMembers.
     *
     * @param groupId the unique ID of the group
     * @param userId  the unique ID of the user to add
     * @return true if the user was successfully added, false otherwise
     * @throws SQLException if a database access error occurs (e.g. user is already a member)
     */
    public boolean addMember(int groupId, int userId) throws SQLException {
        String sql = "INSERT INTO GroupMembers (group_id, user_id) VALUES (?, ?)";
        
        try (Connection conn = DatabaseConnection.getInstance();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, groupId);
            ps.setInt(2, userId);
            
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Removes a user from a group by deleting their association record in GroupMembers.
     *
     * @param groupId the unique ID of the group
     * @param userId  the unique ID of the user to remove
     * @return true if the user was successfully removed, false otherwise
     * @throws SQLException if a database access error occurs
     */
    public boolean removeMember(int groupId, int userId) throws SQLException {
        String sql = "DELETE FROM GroupMembers WHERE group_id = ? AND user_id = ?";
        
        try (Connection conn = DatabaseConnection.getInstance();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, groupId);
            ps.setInt(2, userId);
            
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Updates the name and description of an existing group.
     *
     * @param group the Group object containing the updated values and target ID
     * @return true if the update was successful, false otherwise
     * @throws SQLException if a database access error occurs
     */
    public boolean updateGroup(Group group) throws SQLException {
        String sql = "UPDATE Groups SET name = ?, description = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getInstance();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, group.getName());
            ps.setString(2, group.getDescription());
            ps.setInt(3, group.getId());
            
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Deletes a group from the Groups table. Related GroupMembers records are automatically
     * deleted due to foreign key cascade constraints.
     *
     * @param id the unique ID of the group to delete
     * @return true if the group was successfully deleted, false otherwise
     * @throws SQLException if a database access error occurs
     */
    public boolean deleteGroup(int id) throws SQLException {
        String sql = "DELETE FROM Groups WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getInstance();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            
            return ps.executeUpdate() > 0;
        }
    }
}
