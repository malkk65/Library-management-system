package com.library.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/library_db";
    private static final String USER = "root";
    private static final String PASSWORD = "Malk2005?"; 

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found. Add the connector JAR to your project.");
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /**
     * Optional: Helper to initialize the database if it doesn't exist.
     * Note: This assumes the user has created 'library_db' or can connect to
     * server.
     */
    public static void initializeDatabase() {
        String serverUrl = "jdbc:mysql://localhost:3306/";
        try (Connection conn = DriverManager.getConnection(serverUrl, USER, PASSWORD);
                Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS library_db");
            System.out.println("Database 'library_db' ensured.");
        } catch (SQLException e) {
            System.err.println("Could not ensure database exists. Please create 'library_db' manually.");
            e.printStackTrace();
        }
    }
}
