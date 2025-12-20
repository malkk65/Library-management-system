package com.library.common;

public class Book {
    private String title;
    private String author;
    private String category;
    private String isbn; // Used as Dewey/ID for now
    private String status; // "Available", "Borrowed"

    private boolean isEbook;
    private String filePath; // Null for physical books

    public Book(String title, String author, String isbn, String status, String category) {
        this(title, author, isbn, status, category, false, null);
    }

    public Book(String title, String author, String isbn, String status, String category, boolean isEbook,
            String filePath) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.status = status;
        this.category = category;
        this.isEbook = isEbook;
        this.filePath = filePath;
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
}
