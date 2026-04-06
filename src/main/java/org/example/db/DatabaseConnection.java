package org.example.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.InputStream;

public class DatabaseConnection {
    // Note the protocol change to 'mariadb'
    private static final String URL = "jdbc:mariadb://localhost:3306/fuel_calculator_localization";
    private static String user;
    private static String password;

    static {
        // Look for db.properties in src/main/resources
        try (InputStream input = DatabaseConnection.class.getClassLoader().getResourceAsStream("db.properties")) {
            Properties prop = new Properties();
            if (input == null) {
                System.err.println("Error: Unable to find db.properties. Ensure it is in src/main/resources");
            } else {
                prop.load(input);
                user = prop.getProperty("db.user");
                password = prop.getProperty("db.password");

                // Debugging print (Optional: remove once working)
                System.out.println("Database configuration loaded successfully.");
            }
        } catch (Exception ex) {
            System.err.println("Critical error loading database properties:");
            ex.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        try {
            // Explicitly loading the driver can help in some environments
            Class.forName("org.mariadb.jdbc.Driver");
            return DriverManager.getConnection(URL, user, password);
        } catch (ClassNotFoundException e) {
            throw new SQLException("MariaDB JDBC Driver not found. Check your dependencies.", e);
        }
    }
}