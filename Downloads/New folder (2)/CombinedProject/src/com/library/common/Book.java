package com.library.common;

public class Book {
    private int id; // Database ID
    private String title;
    private String author;
    private String category;
    private String isbn;
    private String status; // "Available", "Borrowed"

    private boolean isEbook;
    private String filePath;
    private double price;

    // Constructor for Physical Books with ID
    public Book(int id, String title, String author, String isbn, String status, String category) {
        this(id, title, author, isbn, status, category, false, null, 0.0);
    }

    // Existing constructor (might be used for creating new books before DB
    // insertion)
    public Book(String title, String author, String isbn, String status, String category) {
        this(0, title, author, isbn, status, category, false, null, 0.0);
    }

    public Book(int id, String title, String author, String isbn, String status, String category, boolean isEbook,
            String filePath, double price) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.status = status;
        this.category = category;
        this.isEbook = isEbook;
        this.filePath = filePath;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getCategory() {
        return category;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isEbook() {
        return isEbook;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
