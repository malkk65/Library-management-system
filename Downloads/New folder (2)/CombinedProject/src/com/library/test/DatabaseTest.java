package com.library.test;

import com.library.common.DatabaseConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class DatabaseTest {
    public static void main(String[] args) {
        System.out.println("--- Starting Database Connection Test ---");

        // Optional: Run initialization
        DatabaseConnection.initializeDatabase();

        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn != null && !conn.isClosed()) {
                System.out.println("SUCCESS: Connected to MySQL database!");

                // Check if tables exist
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SHOW TABLES");
                System.out.println("Tables in library_db:");
                while (rs.next()) {
                    System.out.println("- " + rs.getString(1));
                }
            }
        } catch (Exception e) {
            System.err.println("FAILURE: Could not connect to database.");
            e.printStackTrace();
        }
    }
}
