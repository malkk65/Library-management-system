package com.library.member;

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
import java.io.IOException;
import javax.swing.plaf.basic.BasicArrowButton;
import java.util.List;

public class MemberEBookView extends MemberBasePage {

    private JPanel selectedCard = null;
    private JPanel cardsContainer;
    private JTextField searchField;
    private JComboBox<String> categoryBox;

    public MemberEBookView(NavigationController controller) {
        super(controller);
        pageTitleLabel.setText("Digital Library");
        highlightSidebarButton("E-Books");
        loadBooks("");
    }

    @Override
    protected JPanel getContentPanel() {
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setOpaque(false);

        // Top Search Bar
        JPanel topBar = new JPanel(new BorderLayout(20, 0));
        topBar.setOpaque(false);
        topBar.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));

        searchField = new JTextField("Search for e-books...");
        searchField.setPreferredSize(new Dimension(520, 50));
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        searchField.setForeground(new Color(120, 120, 120));
        searchField.setBackground(Color.WHITE);
        searchField.setBorder(BorderFactory.createLineBorder(new Color(210, 210, 210), 1));

        searchField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (searchField.getText().equals("Search for e-books...")) {
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

        categoryBox = new JComboBox<>(
                new String[] { "All Categories", "Romance", "Fantasy", "Programming", "Horror" });
        categoryBox.setFont(new Font("Segoe UI", Font.BOLD, 14));
        categoryBox.setBackground(Color.WHITE);
        categoryBox.setForeground(new Color(139, 69, 19));
        categoryBox.setPreferredSize(new Dimension(200, 40));
        categoryBox.setFocusable(false);
        categoryBox.setOpaque(true);
        categoryBox.setBorder(BorderFactory.createLineBorder(new Color(139, 69, 19), 1));

        // Custom UI for smaller arrow and styling (Matching BookManagement)
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
                g.setColor(Color.WHITE);
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

        loadBooks("");

        return contentPanel;
    }

    private void loadBooks(String query) {
        cardsContainer.removeAll();
        List<Book> books;
        String category = (categoryBox != null) ? (String) categoryBox.getSelectedItem() : "All Categories";

        if (query == null || query.trim().isEmpty() || query.equals("Search for e-books...")) {
            books = BookService.getInstance().searchEBooks("", category);
        } else {
            books = BookService.getInstance().searchEBooks(query, category);
        }

        for (Book book : books) {
            cardsContainer.add(createBookCard(book));
        }

        cardsContainer.revalidate();
        cardsContainer.repaint();
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

                if (selectedCard == this) {
                    g2.setColor(new Color(210, 169, 110)); // Wood light
                    g2.setStroke(new BasicStroke(3));
                    g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 25, 25);
                }
                g2.dispose();
            }
        };

        card.setOpaque(false);
        card.setPreferredSize(new Dimension(240, 180));
        card.setLayout(new BorderLayout());
        card.setBorder(BorderFactory.createEmptyBorder(15, 15, 12, 15));

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

        JLabel bookIcon = new JLabel(Icons.getFileIcon(45, new Color(139, 69, 19)));
        bookIcon.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel titleLabel = new JLabel("<html><center>" + book.getTitle() + "</center></html>");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setForeground(new Color(80, 80, 80));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel authorLabel = new JLabel(book.getAuthor());
        authorLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        authorLabel.setForeground(new Color(120, 120, 120));
        authorLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel center = new JPanel(new BorderLayout(5, 5));
        center.setOpaque(false);
        center.add(bookIcon, BorderLayout.NORTH);

        JPanel textP = new JPanel(new GridLayout(2, 1));
        textP.setOpaque(false);
        textP.add(titleLabel);
        textP.add(authorLabel);
        center.add(textP, BorderLayout.CENTER);

        card.add(center, BorderLayout.CENTER);

        // Footer with Read Button
        JPanel footer = new JPanel(new BorderLayout());
        footer.setOpaque(false);
        footer.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));

        JButton readBtn = new JButton("Read");
        readBtn.setBackground(new Color(139, 69, 19));
        readBtn.setForeground(Color.WHITE);
        readBtn.setFont(new Font("Segoe UI", Font.BOLD, 11));
        readBtn.setFocusPainted(false);
        readBtn.setBorderPainted(false);
        readBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        readBtn.setPreferredSize(new Dimension(80, 28));

        readBtn.addActionListener(e -> {
            String filePath = book.getFilePath();
            if (filePath != null && !filePath.isEmpty()) {
                try {
                    File file = new File(filePath);
                    if (file.exists()) {
                        Desktop.getDesktop().open(file);
                    } else {
                        UIUtils.showStyledMessage(this, "Error", "File not found: " + filePath, false);
                    }
                } catch (IOException ex) {
                    UIUtils.showStyledMessage(this, "Error", "Could not open file: " + ex.getMessage(), false);
                }
            } else {
                UIUtils.showStyledMessage(this, "Error", "No file path available for this e-book.", false);
            }
        });

        footer.add(readBtn, BorderLayout.EAST);
        card.add(footer, BorderLayout.SOUTH);

        return card;
    }

    private void openEBook(Book book) {
        if (book.getFilePath() == null) {
            UIUtils.showStyledMessage(this, "Error", "This book has no file attached.", false);
            return;
        }

        File file = new File(book.getFilePath());
        if (!file.exists()) {
            UIUtils.showStyledMessage(this, "Error", "File not found: " + book.getFilePath(), false);
            return;
        }

        try {
            Desktop.getDesktop().open(file);
        } catch (IOException e) {
            e.printStackTrace();
            UIUtils.showStyledMessage(this, "Error", "Could not open file.", false);
        }
    }
}
