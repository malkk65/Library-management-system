package com.library.staff;

import com.library.common.Book;
import com.library.common.BookService;
import com.library.common.UIUtils;
import com.library.common.Icons;
import com.library.common.WrapLayout;
import com.library.main.NavigationController;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.plaf.basic.BasicArrowButton;

public class BookManagement extends StaffBasePage {

    private JPanel selectedCard = null;
    private JPanel cardsContainer;
    private JTextField searchField;

    public BookManagement(NavigationController controller) {
        super(controller);
        pageTitleLabel.setText("Book Management");
        highlightSidebarButton("Books");
        loadBooks("");
    }

    @Override
    protected JPanel getContentPanel() {
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setOpaque(false);

        // Top Actions Bar
        JPanel topBar = new JPanel(new BorderLayout(20, 0));
        topBar.setOpaque(false);
        topBar.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));

        JButton add = createStyledButton("Add Book");
        add.addActionListener(e -> showAddBookDialog());
        topBar.add(add, BorderLayout.WEST);

        // search + category
        searchField = new JTextField("Search by title or author...");
        searchField.setPreferredSize(new Dimension(520, 50));
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        searchField.setForeground(new Color(120, 120, 120));
        searchField.setBackground(Color.WHITE);
        searchField.setBorder(BorderFactory.createLineBorder(new Color(210, 210, 210), 1));

        searchField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (searchField.getText().equals("Search by title or author...")) {
                    searchField.setText("");
                    searchField.setForeground(Color.BLACK);
                }
            }
        });

        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                loadBooks(searchField.getText());
            }
        });

        JComboBox<String> categoryBox = new JComboBox<>(
                new String[] { "All Categories", "Romance", "Fantasy", "Programming", "Horror" });
        categoryBox.setFont(new Font("Segoe UI", Font.BOLD, 14));
        categoryBox.setBackground(Color.WHITE);
        categoryBox.setForeground(new Color(139, 69, 19)); // Brown
        categoryBox.setPreferredSize(new Dimension(200, 40)); // Slightly smaller height
        categoryBox.setFocusable(false);
        categoryBox.setOpaque(true);
        categoryBox.setBackground(Color.WHITE);
        categoryBox.setBorder(BorderFactory.createLineBorder(new Color(139, 69, 19), 1));

        // Custom UI for smaller arrow and styling
        categoryBox.setUI(new javax.swing.plaf.basic.BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                JButton btn = new javax.swing.plaf.basic.BasicArrowButton(
                        BasicArrowButton.SOUTH,
                        new Color(139, 69, 19),
                        new Color(139, 69, 19),
                        Color.WHITE,
                        new Color(139, 69, 19));
                btn.setBorder(BorderFactory.createEmptyBorder());
                return btn;
            }

            @Override
            public void paintCurrentValueBackground(Graphics g, Rectangle bounds, boolean hasFocus) {
                g.setColor(Color.WHITE); // خلفية بيضاء ثابتة
                g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
            }
        });

        // Custom Renderer with White Background
        categoryBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                    boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected,
                        cellHasFocus);
                label.setOpaque(true);

                if (isSelected) {
                    label.setBackground(new Color(210, 169, 110));
                    label.setForeground(Color.WHITE);
                } else {
                    label.setBackground(Color.WHITE);
                    label.setForeground(new Color(139, 69, 19));
                }
                return label;
            }
        });

        categoryBox.addActionListener(e -> loadBooks(searchField.getText()));

        JPanel searchWrapper = new JPanel(new BorderLayout());
        searchWrapper.setOpaque(false);
        searchWrapper.add(searchField, BorderLayout.CENTER);

        topBar.add(searchWrapper, BorderLayout.CENTER);
        topBar.add(categoryBox, BorderLayout.EAST);

        // Cards Grid with WrapLayout
        cardsContainer = new JPanel(new WrapLayout(FlowLayout.LEFT, 30, 30));
        cardsContainer.setOpaque(false);
        cardsContainer.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JScrollPane scroll = new JScrollPane(cardsContainer); // Direct add
        scroll.setBorder(null);
        scroll.getViewport().setOpaque(false);
        scroll.setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER); // Force vertical

        contentPanel.add(topBar, BorderLayout.NORTH);
        contentPanel.add(scroll, BorderLayout.CENTER);

        // Initial Load
        loadBooks("");

        return contentPanel;
    }

    private void loadBooks(String query) {
        cardsContainer.removeAll();
        List<Book> books;

        if (query == null || query.trim().isEmpty() || query.equals("Search by title or author...")) {
            books = BookService.getInstance().getPhysicalBooks();
        } else {
            books = BookService.getInstance().searchPhysicalBooks(query);
        }

        for (Book book : books) {
            cardsContainer.add(createBookCard(book));
        }

        cardsContainer.revalidate();
        cardsContainer.repaint();
    }

    private JButton createStyledButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(139, 69, 19)); // Button Brown
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(14, 32, 14, 32));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(160, 80, 20));
            }

            public void mouseExited(MouseEvent e) {
                btn.setBackground(new Color(139, 69, 19));
            }
        });
        return btn;
    }

    private JPanel createBookCard(Book book) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(new Color(255, 255, 255, 240));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);

                // Active Border
                if (selectedCard == this) {
                    g2.setColor(new Color(139, 69, 19));
                    g2.setStroke(new BasicStroke(3));
                    g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 25, 25);
                }
                g2.dispose();
            }
        };

        card.setOpaque(false);
        card.setPreferredSize(new Dimension(280, 230));
        card.setLayout(new BorderLayout());
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                JPanel prev = selectedCard;
                selectedCard = card;
                if (prev != null)
                    prev.repaint();
                card.repaint();
            }
        });

        JLabel bookIcon = new JLabel(Icons.getBookIcon(50, new Color(139, 69, 19)));
        bookIcon.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel titleLabel = new JLabel("<html><center>" + book.getTitle() + "</center></html>");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(new Color(101, 87, 78));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel authorLabel = new JLabel(book.getAuthor());
        authorLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        authorLabel.setForeground(new Color(120, 120, 120));
        authorLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel center = new JPanel(new BorderLayout(10, 0));
        center.setOpaque(false);
        center.add(bookIcon, BorderLayout.WEST);

        JPanel textP = new JPanel(new GridLayout(2, 1));
        textP.setOpaque(false);
        textP.add(titleLabel);
        textP.add(authorLabel);
        center.add(textP, BorderLayout.CENTER);

        card.add(center, BorderLayout.CENTER);

        // footer
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        footer.setOpaque(false);

        JLabel statusTag = new JLabel(" " + book.getStatus() + " ");
        statusTag.setFont(new Font("Segoe UI", Font.BOLD, 11));
        statusTag.setOpaque(true);
        statusTag.setBackground(
                book.getStatus().equals("Available") ? new Color(220, 252, 245) : new Color(255, 240, 240));
        statusTag.setForeground(book.getStatus().equals("Available") ? new Color(0, 100, 50) : new Color(200, 40, 40));

        JButton deleteBtn = new JButton("🗑");
        deleteBtn.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
        deleteBtn.setBorderPainted(false);
        deleteBtn.setContentAreaFilled(false);
        deleteBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        deleteBtn.addActionListener(e -> {
            boolean confirm = UIUtils.showConfirmDialog(this, "Delete", "Delete " + book.getTitle() + "?");
            if (confirm) {
                BookService.getInstance().removeBook(book);
                loadBooks(searchField.getText());
                UIUtils.showStyledMessage(this, "Success", "Deleted " + book.getTitle(), true);
            }
        });

        footer.add(statusTag);
        footer.add(deleteBtn);

        card.add(footer, BorderLayout.SOUTH);

        return card;
    }

    private void showAddBookDialog() {
        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this), "Add Book",
                Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setSize(420, 580);
        dialog.setLocationRelativeTo(this);
        dialog.setUndecorated(true);
        dialog.setBackground(new Color(0, 0, 0, 0));

        JPanel content = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 35, 35);
                g2.setColor(new Color(139, 69, 19));
                g2.setStroke(new BasicStroke(3));
                g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 35, 35);
                g2.dispose();
            }
        };
        content.setOpaque(false);
        content.setBackground(new Color(250, 248, 245));
        content.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel header = new JLabel("Add New Book");
        header.setFont(new Font("Segoe UI", Font.BOLD, 22));
        header.setForeground(new Color(101, 87, 78));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 20, 0);
        content.add(header, gbc);

        gbc.gridwidth = 1;

        JTextField titleField = createModernField();
        JTextField authorField = createModernField();
        JTextField isbnField = createModernField();
        JTextField categoryField = createModernField();

        addFormRow(content, gbc, 1, "Book Title", titleField);
        addFormRow(content, gbc, 2, "Author Name", authorField);
        addFormRow(content, gbc, 3, "ISBN Number", isbnField);
        addFormRow(content, gbc, 4, "Book Category", categoryField);

        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        btnPanel.setOpaque(false);

        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        cancelBtn.setForeground(new Color(120, 120, 120));
        cancelBtn.setContentAreaFilled(false);
        cancelBtn.setBorderPainted(false);
        cancelBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelBtn.addActionListener(e -> dialog.dispose());

        JButton saveBtn = createStyledButton("Add Book");
        saveBtn.setPreferredSize(new Dimension(140, 45));

        saveBtn.addActionListener(e -> {
            if (titleField.getText().isEmpty() || authorField.getText().isEmpty()) {
                UIUtils.showStyledMessage(this, "Wait!", "Please fill in the title and author.", false);
                return;
            }

            Book newBook = new Book(
                    titleField.getText(),
                    authorField.getText(),
                    isbnField.getText(),
                    "Available",
                    categoryField.getText());

            int generatedId = BookService.getInstance().addBook(newBook);
            if (generatedId > 0) {
                loadBooks("");
                dialog.dispose();
                UIUtils.showStyledMessage(this, "Success", "Book added to collection!", true);
            } else {
                UIUtils.showStyledMessage(this, "Error", "Database error: Could not add book.", false);
            }
        });

        btnPanel.add(cancelBtn);
        btnPanel.add(saveBtn);

        gbc.gridy = 10;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 0, 0, 0);
        content.add(btnPanel, gbc);

        dialog.add(content);
        dialog.setVisible(true);
    }

    private void addFormRow(JPanel panel, GridBagConstraints gbc, int row, String labelText, Component comp) {
        gbc.gridy = row * 2 - 1;
        gbc.gridx = 0;
        gbc.gridwidth = 2;

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(new Color(139, 69, 19));
        gbc.insets = new Insets(8, 0, 4, 0);
        panel.add(label, gbc);

        gbc.gridy = row * 2;
        gbc.insets = new Insets(0, 0, 8, 0);
        panel.add(comp, gbc);
    }

    private JTextField createModernField() {
        JTextField field = new JTextField();
        field.setPreferredSize(new Dimension(330, 42));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(225, 220, 215), 1),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)));
        field.setBackground(new Color(252, 251, 249));
        return field;
    }
}
