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
import java.util.List;

public class MemberEBookView extends MemberBasePage {

    private JPanel selectedCard = null;
    private JPanel cardsContainer;
    private JTextField searchField;

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

        loadBooks("");

        return contentPanel;
    }

    private void loadBooks(String query) {
        cardsContainer.removeAll();
        List<Book> books;

        if (query == null || query.trim().isEmpty() || query.equals("Search for e-books...")) {
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

                openEBook(book);
            }
        });

        JLabel bookIcon = new JLabel(Icons.getFileIcon(60, new Color(139, 69, 19)));
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

        JPanel textP = new JPanel(new GridLayout(2, 1));
        textP.setOpaque(false);
        textP.add(titleLabel);
        textP.add(authorLabel);
        center.add(textP, BorderLayout.CENTER);

        card.add(center, BorderLayout.CENTER);

        // footer
        JLabel statusTag = new JLabel(" CLICK TO OPEN ");
        statusTag.setFont(new Font("Segoe UI", Font.BOLD, 10));
        statusTag.setHorizontalAlignment(SwingConstants.CENTER);
        statusTag.setOpaque(true);
        statusTag.setBackground(new Color(240, 240, 250));
        statusTag.setForeground(new Color(100, 100, 180));

        card.add(statusTag, BorderLayout.SOUTH);

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
