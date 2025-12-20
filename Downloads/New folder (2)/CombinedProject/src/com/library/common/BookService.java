package com.library.common;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookService {
    private static BookService instance;

    private BookService() {
        // No longer using in-memory list
    }

    public static BookService getInstance() {
        if (instance == null) {
            instance = new BookService();
        }
        return instance;
    }

    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        books.addAll(getPhysicalBooks());
        books.addAll(getEBooks());
        return books;
    }

    public List<Book> getPhysicalBooks() {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM Books";
        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                books.add(new Book(
                        rs.getInt("BookID"),
                        rs.getString("Title"),
                        rs.getString("Author"),
                        rs.getString("ISBN"),
                        rs.getString("Status"),
                        rs.getString("Category")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    public List<Book> getEBooks() {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM EBooks";
        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                books.add(new Book(
                        rs.getInt("EBookID"),
                        rs.getString("Title"),
                        rs.getString("Author"),
                        rs.getString("Dewey"), // Or ISBN
                        "Available",
                        rs.getString("Category"),
                        true,
                        rs.getString("Format"),
                        rs.getDouble("Price")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    public int addBook(Book book) {
        String sql;
        if (book.isEbook()) {
            sql = "INSERT INTO EBooks (Title, Author, Format, Price, Category, Dewey) VALUES (?, ?, ?, ?, ?, ?)";
        } else {
            sql = "INSERT INTO Books (Title, Author, ISBN, Category, Status) VALUES (?, ?, ?, ?, ?)";
        }

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getAuthor());
            if (book.isEbook()) {
                pstmt.setString(3, book.getFilePath()); // Format column used for path
                pstmt.setDouble(4, book.getPrice());
                pstmt.setString(5, book.getCategory());
                pstmt.setString(6, book.getIsbn());
            } else {
                pstmt.setString(3, book.getIsbn());
                pstmt.setString(4, book.getCategory());
                pstmt.setString(5, book.getStatus());
            }
            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("=== Database Error Adding Book ===");
            System.err.println("Message: " + e.getMessage());
            System.err.println("SQL State: " + e.getSQLState());
            System.err.println("Error Code: " + e.getErrorCode());
            e.printStackTrace();
        }
        return -1;
    }

    public void borrowBook(int userId, int bookId) throws SQLException {
        String sqlRecord = "INSERT INTO Borrow_Records (UserID, BookID, BorrowDate, DueDate, Status) VALUES (?, ?, NOW(), DATE_ADD(NOW(), INTERVAL 14 DAY), 'Active')";
        String sqlUpdate = "UPDATE Books SET Status = 'Borrowed' WHERE BookID = ?";

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                try (PreparedStatement pk = conn.prepareStatement(sqlRecord)) {
                    pk.setInt(1, userId);
                    pk.setInt(2, bookId);
                    pk.executeUpdate();
                }
                try (PreparedStatement pu = conn.prepareStatement(sqlUpdate)) {
                    pu.setInt(1, bookId);
                    pu.executeUpdate();
                }
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }

    public void purchaseEBook(int userId, int ebookId) throws SQLException {
        String sql = "INSERT INTO EBook_Purchases (UserID, EBookID) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, ebookId);
            pstmt.executeUpdate();
        }
    }

    public boolean hasPurchased(int userId, int ebookId) {
        String sql = "SELECT 1 FROM EBook_Purchases WHERE UserID = ? AND EBookID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, ebookId);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            return false;
        }
    }

    public void removeBook(Book book) {
        String sql = book.isEbook() ? "DELETE FROM EBooks WHERE EBookID = ?"
                : "DELETE FROM Books WHERE BookID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, book.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Book> searchBooks(String query) {
        if (query == null || query.trim().isEmpty())
            return getAllBooks();
        List<Book> books = new ArrayList<>();
        String q = "%" + query.toLowerCase() + "%";

        String sqlP = "SELECT * FROM Books WHERE LOWER(Title) LIKE ? OR LOWER(Author) LIKE ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sqlP)) {
            pstmt.setString(1, q);
            pstmt.setString(2, q);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                books.add(new Book(rs.getInt("BookID"), rs.getString("Title"), rs.getString("Author"),
                        rs.getString("ISBN"),
                        rs.getString("Status"), rs.getString("Category")));
            }
        } catch (SQLException e) {
        }

        String sqlE = "SELECT * FROM EBooks WHERE LOWER(Title) LIKE ? OR LOWER(Author) LIKE ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sqlE)) {
            pstmt.setString(1, q);
            pstmt.setString(2, q);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                books.add(new Book(rs.getInt("EBookID"), rs.getString("Title"), rs.getString("Author"),
                        rs.getString("Dewey"), "Available",
                        rs.getString("Category"), true, rs.getString("Format"), rs.getDouble("Price")));
            }
        } catch (SQLException e) {
        }
        return books;
    }

    public List<Book> searchPhysicalBooks(String query, String category) {
        List<Book> results = new ArrayList<>();
        String sql = "SELECT * FROM Books WHERE (LOWER(Title) LIKE ? OR LOWER(Author) LIKE ?)";
        if (category != null && !category.equalsIgnoreCase("All Categories"))
            sql += " AND Category = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            String q = "%" + (query == null ? "" : query.toLowerCase()) + "%";
            pstmt.setString(1, q);
            pstmt.setString(2, q);
            if (category != null && !category.equalsIgnoreCase("All Categories"))
                pstmt.setString(3, category);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                results.add(new Book(rs.getInt("BookID"), rs.getString("Title"), rs.getString("Author"),
                        rs.getString("ISBN"),
                        rs.getString("Status"), rs.getString("Category")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    public List<Book> searchPhysicalBooks(String query) {
        return searchPhysicalBooks(query, "All Categories");
    }

    public List<Book> searchEBooks(String query, String category) {
        List<Book> results = new ArrayList<>();
        String sql = "SELECT * FROM EBooks WHERE (LOWER(Title) LIKE ? OR LOWER(Author) LIKE ?)";
        if (category != null && !category.equalsIgnoreCase("All Categories"))
            sql += " AND Category = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            String q = "%" + (query == null ? "" : query.toLowerCase()) + "%";
            pstmt.setString(1, q);
            pstmt.setString(2, q);
            if (category != null && !category.equalsIgnoreCase("All Categories"))
                pstmt.setString(3, category);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                results.add(new Book(rs.getInt("EBookID"), rs.getString("Title"), rs.getString("Author"),
                        rs.getString("Dewey"), "Available",
                        rs.getString("Category"), true, rs.getString("Format"), rs.getDouble("Price")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    public List<Book> searchEBooks(String query) {
        return searchEBooks(query, "All Categories");
    }
}
