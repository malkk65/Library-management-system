package com.library.test;

import com.library.common.DatabaseConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class DbInspector {
    public static void main(String[] args) {
        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM Users");
            System.out.println("--- Users Table ---");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("UserID") +
                        " | Name: " + rs.getString("Name") +
                        " | Email: " + rs.getString("Email") +
                        " | Role: " + rs.getString("Role") +
                        " | Password: [" + rs.getString("Password") + "]");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
