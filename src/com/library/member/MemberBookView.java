package com.library.member;

import com.library.common.Book;
import com.library.common.BookService;
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

public class MemberBookView extends MemberBasePage {

    private JPanel selectedCard = null;
    private JPanel cardsContainer;
    private JTextField searchField;

    public MemberBookView(NavigationController controller) {
        super(controller);
        pageTitleLabel.setText("Library Collection");
        highlightSidebarButton("Books");
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

        searchField = new JTextField("Search for books...");
        searchField.setPreferredSize(new Dimension(520, 50));
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        searchField.setForeground(new Color(120, 120, 120));
        searchField.setBackground(Color.WHITE);
        searchField.setBorder(BorderFactory.createLineBorder(new Color(210, 210, 210), 1));

        searchField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (searchField.getText().equals("Search for books...")) {
                    searchField.setText("");
                    searchField.setForeground(Color.BLACK);
                }
            }
        });

        JComboBox<String> categoryBox = new JComboBox<>(
                new String[] { "All Categories", "Romance", "Fantasy", "Programming", "Horror" });

        categoryBox.setFont(new Font("Segoe UI", Font.BOLD, 14));
        categoryBox.setBackground(Color.WHITE);
        categoryBox.setForeground(new Color(139, 69, 19)); // Brown
        categoryBox.setPreferredSize(new Dimension(200, 40));
        categoryBox.setFocusable(false);
        categoryBox.setOpaque(true);
        categoryBox.setBackground(Color.WHITE);
        categoryBox.setBorder(BorderFactory.createLineBorder(new Color(139, 69, 19), 1));

        // Custom UI to style the arrow button
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

        // Custom Renderer
        categoryBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                    boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected,
                        cellHasFocus);
                label.setOpaque(true);

                if (isSelected) {
                    label.setBackground(new Color(210, 169, 110)); // Wood light selection
                    label.setForeground(Color.WHITE);
                } else {
                    label.setBackground(Color.WHITE);
                    label.setForeground(new Color(139, 69, 19)); // Brown text
                }
                return label;
            }
        });

        categoryBox.addActionListener(e -> {
            loadBooks(searchField.getText(), (String) categoryBox.getSelectedItem());
        });

        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                loadBooks(searchField.getText(), (String) categoryBox.getSelectedItem());
            }
        });

        JPanel searchWrapper = new JPanel(new BorderLayout());
        searchWrapper.setOpaque(false);
        searchWrapper.add(searchField, BorderLayout.CENTER);

        topBar.add(searchWrapper, BorderLayout.CENTER);
        topBar.add(categoryBox, BorderLayout.EAST);

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

    private void loadBooks(String query, String category) {
        cardsContainer.removeAll();
        List<Book> books;

        if (query.equals("Search for books...")) {
            query = "";
        }

        books = BookService.getInstance().searchPhysicalBooks(query, category);

        if (books.isEmpty()) {
            JLabel noBooks = new JLabel("No books found.");
            noBooks.setFont(new Font("Segoe UI", Font.ITALIC, 16));
            noBooks.setForeground(Color.GRAY);
            cardsContainer.add(noBooks);
        } else {
            for (Book book : books) {
                cardsContainer.add(createBookCard(book));
            }
        }

        cardsContainer.revalidate();
        cardsContainer.repaint();
    }

    private void loadBooks(String query) {
        loadBooks(query, "All Categories");
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
        card.setPreferredSize(new Dimension(280, 200));
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
        titleLabel.setForeground(new Color(80, 80, 80));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel authorLabel = new JLabel(book.getAuthor());
        authorLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        authorLabel.setForeground(new Color(120, 120, 120));
        authorLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel center = new JPanel(new BorderLayout());
        center.setOpaque(false);
        center.add(bookIcon, BorderLayout.NORTH);
        bookIcon.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel textP = new JPanel(new GridLayout(2, 1));
        textP.setOpaque(false);
        textP.add(titleLabel);
        textP.add(authorLabel);
        center.add(textP, BorderLayout.CENTER);

        card.add(center, BorderLayout.CENTER);

        // footer
        JLabel statusTag = new JLabel(" " + book.getStatus() + " ");
        statusTag.setFont(new Font("Segoe UI", Font.BOLD, 12));
        statusTag.setHorizontalAlignment(SwingConstants.CENTER);
        statusTag.setOpaque(true);
        statusTag.setBackground(
                book.getStatus().equals("Available") ? new Color(220, 252, 245) : new Color(255, 240, 240));
        statusTag.setForeground(book.getStatus().equals("Available") ? new Color(0, 100, 50) : new Color(200, 40, 40));

        card.add(statusTag, BorderLayout.SOUTH);

        return card;
    }
}
