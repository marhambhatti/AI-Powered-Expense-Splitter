package com.expensesplitter.models;

import java.util.List;
import java.util.ArrayList;

/**
 * Model class representing an expense group (e.g. Roommates, Trip Group).
 * Each group has its own member list and expense ledger.
 *
 * Student : Muhammad Arham | L1F23BSSE0372
 * Package : com.expensesplitter.models
 */
public class Group {
    private int id;
    private String name;
    private String description;
    private int createdBy;           // User ID of the group creator
    private List<User> members;      // List of members in this group

    /** Default constructor — initializes empty members list */
    public Group() {
        this.members = new ArrayList<>();
    }

    /** Parameterized constructor */
    public Group(int id, String name, String description, int createdBy) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.createdBy = createdBy;
        this.members = new ArrayList<>();
    }

    // ── Getters & Setters ────────────────────────
    public int getId()                  { return id; }
    public void setId(int id)           { this.id = id; }

    public String getName()             { return name; }
    public void setName(String name)    { this.name = name; }

    public String getDescription()           { return description; }
    public void setDescription(String desc)  { this.description = desc; }

    public int getCreatedBy()                { return createdBy; }
    public void setCreatedBy(int createdBy)  { this.createdBy = createdBy; }

    public List<User> getMembers()           { return members; }
    public void setMembers(List<User> m)     { this.members = m; }

    /** Adds a single member to this group */
    public void addMember(User user) {
        this.members.add(user);
    }

    @Override
    public String toString() {
        return "Group{id=" + id + ", name='" + name + "', members=" + members.size() + "}";
    }
}
