package com.expensesplitter.models;

/**
 * Model class representing a registered user in the system.
 * Demonstrates ENCAPSULATION — all fields are private with public getters/setters.
 *
 * Student : Muhammad Arham | L1F23BSSE0372
 * Package : com.expensesplitter.models
 */
public class User {
    // Private fields — encapsulation
    private int id;
    private String name;
    private String email;
    private String passwordHash; // SHA-256 hashed password, never plain text

    /** Default constructor */
    public User() {}

    /** Parameterized constructor for clean object creation */
    public User(int id, String name, String email, String passwordHash) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
    }

    // ── Getters ──────────────────────────────────
    public int getId()             { return id; }
    public String getName()        { return name; }
    public String getEmail()       { return email; }
    public String getPasswordHash(){ return passwordHash; }

    // ── Setters ──────────────────────────────────
    public void setId(int id)                      { this.id = id; }
    public void setName(String name)               { this.name = name; }
    public void setEmail(String email)             { this.email = email; }
    public void setPasswordHash(String hash)       { this.passwordHash = hash; }

    @Override
    public String toString() {
        return "User{id=" + id + ", name='" + name + "', email='" + email + "'}";
    }
}
