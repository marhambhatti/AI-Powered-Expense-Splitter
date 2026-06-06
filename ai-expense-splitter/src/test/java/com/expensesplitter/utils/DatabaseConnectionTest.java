package com.expensesplitter.utils;

import org.junit.Test;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import static org.junit.Assert.*;

/**
 * Unit tests for the DatabaseConnection utility class.
 * Tests Singleton behavior, private constructor enforcement, and safe closing.
 *
 * Student : Muhammad Arham | L1F23BSSE0372
 * Package : com.expensesplitter.utils
 */
public class DatabaseConnectionTest {

    /**
     * Verifies that the private constructor throws an exception when accessed via reflection,
     * ensuring strict enforcement of the Singleton pattern.
     *
     * @throws Exception if reflection access fails
     */
    @Test
    public void testPrivateConstructorReflection() throws Exception {
        // Retrieve the private constructor of DatabaseConnection
        Constructor<DatabaseConnection> constructor = DatabaseConnection.class.getDeclaredConstructor();
        // Make the constructor accessible temporarily for the test
        constructor.setAccessible(true);

        try {
            // Attempt to instantiate the class using reflection
            constructor.newInstance();
            // If instantiation succeeds, the Singleton pattern is violated
            fail("Constructor should not be accessible and should prevent multiple instances.");
        } catch (InvocationTargetException e) {
            // Verify that the root cause is an AssertionError
            assertTrue(e.getTargetException() instanceof AssertionError);
        }
    }

    /**
     * Verifies that calling closeConnection does not throw an exception when no connection is active.
     */
    @Test
    public void testCloseConnectionSafelyWhenNull() {
        try {
            // Attempting to close a null/uninitialized connection should fail gracefully
            DatabaseConnection.closeConnection();
        } catch (Exception e) {
            fail("closeConnection() should not throw any exception when instance is null.");
        }
    }
}
