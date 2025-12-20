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
import java.io.File;
import java.util.List;

public class EBookManagement extends StaffBasePage {

    private JPanel cardsContainer;
    private JTextField searchField;

    public EBookManagement(NavigationController controller) {
        super(controller);
        pageTitleLabel.setText("E-Book Management");
        highlightSidebarButton("E-Books");
    }

    @Override
    protected JPanel getContentPanel() {
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setOpaque(false);

        // Top Actions Bar
        JPanel topBar = new JPanel(new BorderLayout(20, 0));
        topBar.setOpaque(false);
        topBar.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));

        JButton uploadBtn = createStyledButton("Upload E-Book");
        uploadBtn.addActionListener(e -> showUploadDialog());
        topBar.add(uploadBtn, BorderLayout.WEST);

        // Search
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

        topBar.add(searchField, BorderLayout.CENTER);

        // Cards Grid
        // Cards Grid
        cardsContainer = new JPanel(new WrapLayout(FlowLayout.LEFT, 30, 30));
        cardsContainer.setOpaque(false);
        cardsContainer.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JScrollPane scroll = new JScrollPane(cardsContainer);
        scroll.setBorder(null);
        scroll.getViewport().setOpaque(false);
        scroll.setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

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
            books = BookService.getInstance().getEBooks();
        } else {
            books = BookService.getInstance().searchEBooks(query);
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
                g2.dispose();
            }
        };

        card.setOpaque(false);
        card.setPreferredSize(new Dimension(280, 230));
        card.setLayout(new BorderLayout());
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel bookIcon = new JLabel(Icons.getFileIcon(60, new Color(139, 69, 19)));
        bookIcon.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel titleLabel = new JLabel("<html><center>" + book.getTitle() + "</center></html>");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(new Color(101, 87, 78));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel authorLabel = new JLabel(book.getAuthor());
        authorLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        authorLabel.setForeground(new Color(120, 120, 120));
        authorLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel center = new JPanel(new BorderLayout());
        center.setOpaque(false);
        center.add(bookIcon, BorderLayout.NORTH);

        JPanel textP = new JPanel(new GridLayout(2, 1));
        textP.setOpaque(false);
        textP.add(titleLabel);
        textP.add(authorLabel);
        center.add(textP, BorderLayout.CENTER);

        card.add(center, BorderLayout.CENTER);

        // Footer with Delete
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.setOpaque(false);

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

        footer.add(deleteBtn);
        card.add(footer, BorderLayout.SOUTH);

        return card;
    }

    private void showUploadDialog() {
        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this), "Upload E-Book",
                Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setSize(450, 720);
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
        gbc.weightx = 1.0;

        // Title
        JLabel header = new JLabel("Upload New E-Book");
        header.setFont(new Font("Segoe UI", Font.BOLD, 22));
        header.setForeground(new Color(101, 87, 78));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 20, 0);
        content.add(header, gbc);

        gbc.gridwidth = 1;

        // Fields
        JTextField titleField = createModernField();
        JTextField authorField = createModernField();
        JTextField categoryField = createModernField();
        JTextField priceField = createModernField();
        priceField.setText("0.0");

        JLabel fileLabel = new JLabel("No file selected");
        fileLabel.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        fileLabel.setForeground(Color.GRAY);

        JButton fileBtn = new JButton("Select PDF/File");
        fileBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        fileBtn.setBackground(new Color(240, 235, 230));
        fileBtn.setBorder(BorderFactory.createLineBorder(new Color(139, 69, 19), 1));
        fileBtn.setPreferredSize(new Dimension(140, 40));
        fileBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        final File[] selectedFile = { null };

        fileBtn.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            if (chooser.showOpenDialog(dialog) == JFileChooser.APPROVE_OPTION) {
                selectedFile[0] = chooser.getSelectedFile();
                fileLabel.setText(selectedFile[0].getName());
                fileLabel.setForeground(new Color(0, 100, 50)); // Green when selected
            }
        });

        addFormRow(content, gbc, 1, "E-Book Title", titleField);
        addFormRow(content, gbc, 2, "Author Name", authorField);
        addFormRow(content, gbc, 3, "Category", categoryField);
        addFormRow(content, gbc, 4, "Price ($)", priceField);

        // File Row
        gbc.gridy = 9;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        JLabel fLabel = new JLabel("Attachment (PDF)");
        fLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        fLabel.setForeground(new Color(139, 69, 19));
        gbc.insets = new Insets(15, 0, 5, 0);
        content.add(fLabel, gbc);

        gbc.gridy = 10;
        gbc.insets = new Insets(0, 0, 20, 0);
        JPanel filePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        filePanel.setOpaque(false);
        filePanel.add(fileBtn);
        filePanel.add(fileLabel);
        content.add(filePanel, gbc);

        // Spacer to push buttons to bottom
        gbc.gridy = 11;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        content.add(Box.createVerticalGlue(), gbc);

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

        JButton saveBtn = createStyledButton("Upload Book");
        saveBtn.setPreferredSize(new Dimension(150, 45));

        saveBtn.addActionListener(e -> {
            if (titleField.getText().isEmpty() || authorField.getText().isEmpty() || selectedFile[0] == null) {
                UIUtils.showStyledMessage(this, "Wait!", "Please fill all fields and select a file.", false);
                return;
            }

            double priceValue = 0.0;
            try {
                priceValue = Double.parseDouble(priceField.getText());
            } catch (NumberFormatException ex) {
            }

            Book newBook = new Book(
                    0,
                    titleField.getText(),
                    authorField.getText(),
                    "E-" + System.currentTimeMillis(),
                    "Available",
                    categoryField.getText(),
                    true,
                    selectedFile[0].getAbsolutePath(),
                    priceValue);

            int generatedId = BookService.getInstance().addBook(newBook);
            if (generatedId > 0) {
                loadBooks("");
                dialog.dispose();
                UIUtils.showStyledMessage(this, "Success", "E-Book Uploaded!", true);
            } else {
                UIUtils.showStyledMessage(this, "Error", "Database error: Could not upload e-book.", false);
            }
        });

        btnPanel.add(cancelBtn);
        btnPanel.add(saveBtn);

        gbc.gridy = 12;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 0, 0);
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
