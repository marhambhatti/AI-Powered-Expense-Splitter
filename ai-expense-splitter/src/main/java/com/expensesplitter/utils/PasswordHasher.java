package com.expensesplitter.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utility class for password hashing using SHA-256 algorithm.
 * Passwords are NEVER stored in plain text in the database.
 *
 * Method:
 *   hashPassword() — converts plain text password to SHA-256 hash
 *
 * Student : Muhammad Arham | L1F23BSSE0372
 * Package : com.expensesplitter.utils
 */
public class PasswordHasher {

    /**
     * Hashes a plain text password using SHA-256 algorithm.
     * The result is a 64-character hex string stored in the DB.
     *
     * @param password  the plain text password from the user
     * @return SHA-256 hashed hex string, or null if hashing fails
     */
    public static String hashPassword(String password) {
        try {
            // Get SHA-256 MessageDigest instance
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // Convert password string to bytes and hash
            byte[] hashBytes = digest.digest(password.getBytes());

            // Convert byte array to hex string
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            System.err.println("SHA-256 algorithm not available: " + e.getMessage());
            return null;
        }
    }

    /**
     * Verifies a plain text password against its stored hash.
     *
     * @param plainPassword  the password entered by the user at login
     * @param storedHash     the SHA-256 hash stored in the database
     * @return true if the password matches the stored hash
     */
    public static boolean verifyPassword(String plainPassword, String storedHash) {
        String hashedInput = hashPassword(plainPassword);
        return hashedInput != null && hashedInput.equals(storedHash);
    }
}
