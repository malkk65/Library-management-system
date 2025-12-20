package com.library.common;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MemberActivityService {
    private static MemberActivityService instance;

    private MemberActivityService() {
        // No longer using in-memory lists
    }

    public static MemberActivityService getInstance() {
        if (instance == null) {
            instance = new MemberActivityService();
        }
        return instance;
    }

    private int getCurrentUserId(Connection conn, String email) throws SQLException {
        String sql = "SELECT UserID FROM Users WHERE Email = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next())
                return rs.getInt("UserID");
        }
        return -1;
    }

    public List<BorrowedBook> getBorrowingHistory() {
        List<BorrowedBook> history = new ArrayList<>();
        String email = UserSession.getInstance().getUserEmail();
        if (email == null)
            return history;

        String sql = "SELECT b.Title, r.BorrowDate, r.DueDate, r.Status " +
                "FROM Borrow_Records r " +
                "JOIN Books b ON r.BookID = b.BookID " +
                "JOIN Users u ON r.UserID = u.UserID " +
                "WHERE u.Email = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                history.add(new BorrowedBook(
                        rs.getString("Title"),
                        rs.getString("BorrowDate"),
                        rs.getString("DueDate"),
                        rs.getString("Status")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return history;
    }

    public List<PurchasedEBook> getPurchaseHistory() {
        List<PurchasedEBook> history = new ArrayList<>();
        String email = UserSession.getInstance().getUserEmail();
        if (email == null)
            return history;

        String sql = "SELECT e.Title, p.PurchaseDate, e.Price " +
                "FROM EBook_Purchases p " +
                "JOIN EBooks e ON p.EBookID = e.EBookID " +
                "JOIN Users u ON p.UserID = u.UserID " +
                "WHERE u.Email = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                history.add(new PurchasedEBook(
                        rs.getString("Title"),
                        rs.getString("PurchaseDate"),
                        rs.getDouble("Price")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return history;
    }

    public List<Fine> getFines() {
        List<Fine> list = new ArrayList<>();
        String email = UserSession.getInstance().getUserEmail();
        if (email == null)
            return list;

        String sql = "SELECT f.Amount, f.FineDate, f.PaidStatus, b.Title " +
                "FROM Fines f " +
                "JOIN Borrow_Records r ON f.RecordID = r.RecordID " +
                "JOIN Books b ON r.BookID = b.BookID " +
                "JOIN Users u ON r.UserID = u.UserID " +
                "WHERE u.Email = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(new Fine(
                        "Late Return: " + rs.getString("Title"),
                        rs.getDouble("Amount"),
                        rs.getString("FineDate"),
                        rs.getString("PaidStatus")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Models
    public static class BorrowedBook {
        public String title, borrowDate, dueDate, status;

        public BorrowedBook(String title, String bDate, String dDate, String status) {
            this.title = title;
            this.borrowDate = bDate;
            this.dueDate = dDate;
            this.status = status;
        }
    }

    public static class PurchasedEBook {
        public String title, purchaseDate;
        public double price;

        public PurchasedEBook(String title, String pDate, double price) {
            this.title = title;
            this.purchaseDate = pDate;
            this.price = price;
        }
    }

    public static class Fine {
        public String reason, date, status;
        public double amount;

        public Fine(String reason, double amount, String date, String status) {
            this.reason = reason;
            this.amount = amount;
            this.date = date;
            this.status = status;
        }
    }
}
