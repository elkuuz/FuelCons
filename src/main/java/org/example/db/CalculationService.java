package org.example.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CalculationService {

    /**
     * Saves a calculation record to the database.
     * @param distance Trip distance
     * @param consumption Fuel consumption rate
     * @param price Fuel price per unit
     * @param totalFuel Calculated total fuel required
     * @param totalCost Calculated total trip cost
     * @param language The language code used during calculation
     */

    public void saveCalculation(double distance, double consumption, double price,
                                double totalFuel, double totalCost, String language) {

        // SQL query matches the calculation_records table schema
        String sql = "INSERT INTO calculation_records (distance, consumption, price, total_fuel, total_cost, language) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Mapping parameters to the prepared statement
            pstmt.setDouble(1, distance);
            pstmt.setDouble(2, consumption);
            pstmt.setDouble(3, price);
            pstmt.setDouble(4, totalFuel);
            pstmt.setDouble(5, totalCost);
            pstmt.setString(6, language);

            pstmt.executeUpdate();
            System.out.println("Calculation record saved successfully.");

        } catch (SQLException e) {
            // Proper error handling for connection or SQL failures
            System.err.println("Database error while saving record: " + e.getMessage());
        }
    }
}
