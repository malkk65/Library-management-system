package com.library.common;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BookService {
    private static BookService instance;
    private List<Book> books;

    private BookService() {
        books = new ArrayList<>();
        // Initialize with dummy data (Physical)
        books.add(new Book("Harry Potter", "J.K. Rowling", "005.11", "Borrowed", "Fantasy"));
        books.add(new Book("Clean Code", "Robert C. Martin", "005.1", "Available", "Programming"));
        books.add(new Book("The Witcher", "Andrzej Sapkowski", "005.18", "Borrowed", "Fantasy"));
        books.add(new Book("Twilight", "Stephenie Meyer", "005.12", "Available", "Romance"));
        books.add(new Book("Design Patterns", "Erich Gamma", "005.20", "Available", "Programming"));
        books.add(new Book("The Hobbit", "J.R.R. Tolkien", "005.21", "Available", "Fantasy"));

        // Dummy E-Books
        // Note: fitPath is null or a dummy path. Real usage requires valid paths.
        books.add(new Book("Java Concurrency", "Brian Goetz", "E001", "Available", "Programming", true,
                "dummy_path.pdf"));
        books.add(new Book("Effective Java", "Joshua Bloch", "E002", "Available", "Programming", true,
                "dummy_path_2.pdf"));
    }

    public static BookService getInstance() {
        if (instance == null) {
            instance = new BookService();
        }
        return instance;
    }

    public List<Book> getAllBooks() {
        return new ArrayList<>(books);
    }

    public List<Book> getPhysicalBooks() {
        return books.stream().filter(b -> !b.isEbook()).collect(Collectors.toList());
    }

    public List<Book> getEBooks() {
        return books.stream().filter(Book::isEbook).collect(Collectors.toList());
    }

    public void addBook(Book book) {
        books.add(book);
    }

    public void removeBook(Book book) {
        books.remove(book);
    }

    public List<Book> searchBooks(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getAllBooks();
        }
        String lowerQuery = query.toLowerCase();
        return books.stream()
                .filter(b -> b.getTitle().toLowerCase().contains(lowerQuery) ||
                        b.getAuthor().toLowerCase().contains(lowerQuery))
                .collect(Collectors.toList());
    }

    public List<Book> searchPhysicalBooks(String query, String category) {
        List<Book> filtered = searchBooks(query).stream().filter(b -> !b.isEbook()).collect(Collectors.toList());

        if (category != null && !category.equalsIgnoreCase("All Categories")) {
            filtered = filtered.stream()
                    .filter(b -> b.getCategory().equalsIgnoreCase(category))
                    .collect(Collectors.toList());
        }
        return filtered;
    }

    public List<Book> searchPhysicalBooks(String query) {
        return searchPhysicalBooks(query, "All Categories");
    }

    public List<Book> searchEBooks(String query) {
        return searchBooks(query).stream().filter(Book::isEbook).collect(Collectors.toList());
    }
}
