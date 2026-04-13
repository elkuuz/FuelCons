package org.example.db;

import org.junit.Test;

import static org.junit.Assert.*;

public class DatabaseConnectionTest {

    @Test
    public void testDatabaseConnectionClassExists() {
        // Assert that DatabaseConnection class is available
        assertNotNull(DatabaseConnection.class);
    }

    @Test
    public void testGetConnectionMethodExists() {
        // Assert that getConnection method exists
        assertNotNull(DatabaseConnection.class.getMethods());
        boolean methodExists = java.util.Arrays.stream(DatabaseConnection.class.getMethods())
                .anyMatch(m -> m.getName().equals("getConnection"));
        assertTrue("getConnection method should exist", methodExists);
    }

    @Test
    public void testMariaDBDriverAvailable() {
        // Test that MariaDB driver is available in classpath
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            assertTrue(true);
        } catch (ClassNotFoundException e) {
            fail("MariaDB JDBC Driver should be available in classpath");
        }
    }

    @Test
    public void testDatabaseConnectionClassInitializes() {
        // This test verifies that the DatabaseConnection class initializes properly
        try {
            Class.forName("org.example.db.DatabaseConnection");
            assertTrue("DatabaseConnection should initialize", true);
        } catch (ClassNotFoundException e) {
            fail("DatabaseConnection class should be found");
        }
    }

    @Test
    public void testGetConnectionIsStaticMethod() {
        // Verify getConnection is a static method
        try {
            java.lang.reflect.Method method = DatabaseConnection.class.getMethod("getConnection");
            int modifiers = method.getModifiers();
            assertTrue("getConnection should be static", java.lang.reflect.Modifier.isStatic(modifiers));
        } catch (NoSuchMethodException e) {
            fail("getConnection method should exist");
        }
    }
}

